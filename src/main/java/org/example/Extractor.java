package org.example;

import java.util.*;

public class Extractor {
	private static List<HashMap<String,String>> mapList = new ArrayList<HashMap<String,String>>();
	private static Set<String> clausemap = new HashSet<String>();
	private static HashMap<String,List<String>> predicateMap = new HashMap<String,List<String>>();
	

	public Set<String> getclausemap() {
		return clausemap;
	}

	public void setclausemap(HashSet<String> clausemap) {
		this.clausemap = clausemap;
	}

	public HashMap<String, List<String>> getPredicateMap() {
		return predicateMap;
	}

	public void setPredicateMap(HashMap<String, List<String>> predicateMap) {
		this.predicateMap = predicateMap;
	}

	public String[] extract(String[] statements) {
		for ( int i = 0; i < statements.length; i++){
			statements[i] = findPredicates(statements[i]);
			findAllClassesList(statements[i],i);
		}
		//System.out.println();
		Set<String> clausemapTemp = new HashSet<String>();
		int  i = 0;
		for ( String clause : clausemap){
			clause = variableStandardization(clause, i);
			clausemapTemp.add(clause);
			i++;
		}
		clausemap = clausemapTemp;
		
		for (String clause : clausemap){
			fillKB(clause);
		}
		
		return statements;		
	}

	private void fillKB(String clause) {
		String[] predicatesList = clause.split("\\|");
		
		for (int i = 0; i < predicatesList.length; i++){
			predicatesList[i] = getPredicate(predicatesList[i]);
		}
		
		for (int j = 0; j < predicatesList.length; j++){
			while(predicatesList[j].charAt(predicatesList[j].length()-1) == '%'){
				predicatesList[j] = predicatesList[j].substring(0, predicatesList[j].length()-1);
			}
		}
		for (int j = 0; j < predicatesList.length; j++){
			if (predicateMap.get(predicatesList[j]) != null){
				predicateMap.get(predicatesList[j]).add(clause);
			} else {
				List<String> tokens = new ArrayList<String>();
				tokens.add(clause);
				predicateMap.put(predicatesList[j], tokens);
			}
		}
		
	}
	
	private static String getPredicate(String query){
		String[] split = query.split("\\(");
		return split[0];
	}

	private void findAllClassesList(String string, int statementNumber) {
		HashMap<String,String> map = mapList.get(statementNumber);
		if (string.charAt(0) == '('){
			//int open = 1;
			int start = 1;
			for ( int i = 1; i < string.length(); i++ ){
				if (string.charAt(i) == ')'){
					//open--;
					if (i == string.length()-1){
						String tokenString = string.substring(start, i);

						String[] predicates = tokenString.split("\\|");
						tokenString = "";
						for ( int j = 0; j < predicates.length; j++){
							String predicateString = predicates[j].trim();
							if (predicateString.charAt(0) == '('){
								predicateString = predicateString.substring(1);
							}
							if (predicateString.charAt(predicateString.length()-1) == ')'){
								predicateString = predicateString.substring(0, predicateString.length()-1);
							}
							predicateString = map.get(predicateString);
							
							tokenString = tokenString + predicateString;
							if (j != predicates.length-1){
								tokenString =  tokenString + "|";
							}
						}
						clausemap.add(tokenString);
					}
				}
				
				if (string.charAt(i) == '&'){
					String tokenString = string.substring(start, i);
					
					start = i+1;
					String[] predicates = tokenString.split("\\|");
					tokenString = "";
					for ( int j = 0; j < predicates.length; j++){
						String predicateString = predicates[j].trim();
						if (predicateString.charAt(0) == '('){
							predicateString = predicateString.substring(1);
						}
						if (predicateString.charAt(predicateString.length()-1) == ')'){
							predicateString = predicateString.substring(0, predicateString.length()-1);
						}
						
						predicateString = map.get(predicateString);
						
						tokenString = tokenString+predicateString;
						if (j != predicates.length-1){
							tokenString =  tokenString + "|";
						}
					}

					clausemap.add(tokenString);
				}
				
			}
		} else {
			String[] classesList = string.split("&");
			for ( int i = 0; i < classesList.length; i++){
				String tokenString = classesList[i];

				String[] predicates = tokenString.split("\\|");
				tokenString = "";
				for ( int j = 0; j < predicates.length; j++){
					String predicateString = predicates[j].trim();
					if (predicateString.charAt(0) == '('){
						predicateString = predicateString.substring(1);
					}
					if (predicateString.charAt(predicateString.length()-1) == ')'){
						predicateString = predicateString.substring(0, predicateString.length()-1);
					}
					
					predicateString = map.get(predicateString);
					
					tokenString = tokenString+predicateString;
					if (j != predicates.length-1){
						tokenString =  tokenString+"|";
					}
				}

				clausemap.add(tokenString);
			}
			
		}
		
		//System.out.println(statementNumber+" "+clausemap);
		
	}

	
	private String findPredicates(String string) {
		HashMap<String, String> map = new HashMap<String,String>();
		for ( int i = 0; i < string.length(); i++){
			if ( string.charAt(i) >= 65 && string.charAt(i) <= 90){
				int openIndex = -1;
				 
				int j = i;
				while (string.charAt(j)!= ')'){
					if (string.charAt(j) == '('){
						openIndex = j;
					}
					j++;
				}
				String predicateString = string.substring(i,openIndex);
				String operandString = string.substring(i,j+1);
				String additions = "";
				while (map.get(predicateString)!= null){
					if (map.get(predicateString).equals(operandString)){
						break;
					} else {
						predicateString = predicateString+"%";
						additions = additions+"%";
					}
				}
				
				map.put(predicateString,operandString);
				map.put("~"+predicateString, "~"+operandString);

				string = string.substring(0, openIndex)+additions+string.substring(j+1);
				i = openIndex;
			}
		}
		mapList.add(map);
		return string;
	}
	
	private String variableStandardization(String string, int index) {
		int rightIndex = -1;
		boolean isVariable =  false;
		for ( int i = 0; i < string.length(); i++){
			if (string.charAt(i) == '('){
				rightIndex = i+1;
				isVariable = true;
			} else if (string.charAt(i) == ')'){
				if (isVariable){
					while ( rightIndex <= i){
						if (string.charAt(rightIndex) == ',' || string.charAt(rightIndex) == ')'){
							if (isVariable){
								
								if (string.charAt(rightIndex) == ')'){
									isVariable =  false;
								}
								
								int len = rightIndex-1;
								while (len >= 0){
									if (string.charAt(len) == ',' || string.charAt(len) == '(' ){
										break;
									}
									len--;
								}
								if (string.charAt(len+1) >= 97 && string.charAt(len+1) <= 122){
									int stringLen = string.length();
									string = string.substring(0, rightIndex)+index+string.substring(rightIndex);
									i = i + string.length() - stringLen;
									rightIndex = rightIndex + string.length() - stringLen;
								}
								
							}
						}
						rightIndex++;
					}
				}
			} else {
				if (string.charAt(i) == '|' || string.charAt(i) == '&' || string.charAt(i) == '~'){
					isVariable =  false;
				}
			}
		}
		return string;
	}
}
