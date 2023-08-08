package org.example;

import java.util.*;

public class Resolver {
	private Set<String> classMap ;
	private List<String> classList;
	private Map<String,List<String>> predicateMap;
	
	public Resolver(Set<String> classMap, Map<String,List<String>> predicateMap) {
		this.classMap = classMap;
		this.predicateMap = predicateMap;
		classList =  new ArrayList<String>(classMap);
		
	}
	
	public boolean resolution(String query){
		String negatedQuery = negateQuery(query);
		
		classMap.add(negatedQuery);
		classList.add(0, negatedQuery);
		
		
		int sizeDiff = -1;
		while(sizeDiff != 0 ){
			int size = classList.size();
			for (int i = 0; i < classList.size()  ; i++){
				if (classList.size() > 50000){
					return false;
				}

				String clause = classList.get(i);
				if(unify(clause)){
					return true;
				}
			}
			sizeDiff = classMap.size() - size;
			
		}
		
		return false;
	}
	
	/*
	 * Unify scans through the predicate map in the knowledge base (KB) to identify any clauses containing the negation
	 * of a specific token from aClause. It then attempts to unify aClause with each of these identified clauses as much as possible.
	 * If an empty clause is encountered during this process, the function returns true.
	 * If all potential unification have been completed for aClause without finding an empty clause, the function returns false.
	 * */
	private boolean unify(String aClause) {
		String[] aTokens = aClause.split("\\|");

		// if length is 1 then check in the clause list only
		if(aTokens.length == 1){
			String check = negateQuery(aTokens[0]);
			if (classMap.contains(check)){
				return true;
			}
		}
		
		//Looping through each of the tokens in aClause
		for ( int i = 0 ; i < aTokens.length; i++){
			String aToken = negateQuery(aTokens[i]);

			// Get arguments, predicate of ~aToken
			String[] aArguments = getArguments(aToken);
			String aPredicate = getPredicate(aToken);
			
			// Getting all clauses which have ~aToken
			List<String> clauseList = predicateMap.get(aPredicate);
			
			// If aClause belongs to clauseList, move on to next aToken. 
			// DO NOT UNIFY
			boolean aBelongsToClauseList = false;
			
			if (clauseList != null){
				for ( int j = 0; j < clauseList.size(); j++){
					if(aClause.equals(clauseList.get(j))){
						aBelongsToClauseList = true;
					}
				}
			}
		
			if (aBelongsToClauseList){
				continue;
			}
			
			
			// Looping through all list of clauses that contain ~aToken
			if (clauseList != null){				
				for ( int  j = 0; j < clauseList.size(); j++){
					String bclause = clauseList.get(j);
					
					
					
					//Map to hold all the unification mappings
					HashMap<String,String> argMap =  null;
					String[] bTokens = bclause.split("\\|");
					
					//Looping through all the tokens of bClause to find ~aToken
					String bToken = null;
					boolean mayResolve = false;
					boolean willResolve = false;
					
					for ( int m = 0; m < bTokens.length; m++){
						bToken =  bTokens[m];
						String[] bArguments = null;
						String bPredicate = getPredicate(bToken);

						// if predicates are equal then unification possible
						if (bPredicate.equals(aPredicate)){
							// Found ~aToken in bclause
							// Main part of Unification begins
							argMap = new HashMap<String,String>();
							bArguments = getArguments(bToken);
							for ( int k = 0; k < bArguments.length; k++){
								if (bArguments[k].equals(aArguments[k])){
									// SAME variable or Constant
									if (isAConstant(bArguments[k]))
										mayResolve = true;
									else
										mayResolve = false;
									argMap.put(bArguments[k], bArguments[k]);
									continue;
								} else {
									if (isAConstant(aArguments[k]) && isAConstant(bArguments[k])){
										// This token will not unify with aToken
										willResolve = false;
										mayResolve = false;
										break;
									} else if (isAConstant(aArguments[k])){
										// This will unify, unless something bad happens in the later arguments
										willResolve = true;
										argMap.put(bArguments[k], aArguments[k]);
									} else if (isAConstant(bArguments[k])){
										// This will unify, unless something bad happens in the later arguments
										willResolve = true;
										argMap.put(aArguments[k], bArguments[k]);
									} else {
										// both are variables, so replace one of them to another one
										argMap.put(aArguments[k], bArguments[k]);
										if (bTokens.length == 1 || aTokens.length == 1)
											willResolve = true;
									}
								}
							}
							if (willResolve){
								break;
							} else if (mayResolve){
								if (aTokens.length == 1){
									//System.out.println("Here "+aToken+" "+bToken);
									willResolve = true;
									break;
								} else {
									continue;
								}
							}
						}
					}

					// unification is possible
					if (willResolve){
						//System.out.println("UNIFY"+" "+aClause+" "+bclause);
						String newA = new String(aClause);
						String newB = new String(bclause);
						
						String[] newATokens = newA.split("\\|");
						String[] newBTokens = newB.split("\\|");
						newA = "";

						// make the new A token (formatting)
						for ( int m = 0; m < newATokens.length; m++){
							String newToken = newATokens[m];
							if (!newToken.equals(negateQuery(aToken))){
								String[] newArgs = getArguments(newToken);
								newToken = getPredicate(newToken)+"(";
								// replacement of all the unification
								for ( int n = 0; n < newArgs.length; n++){
									if (argMap.get(newArgs[n]) != null){
										newToken = newToken+argMap.get(newArgs[n]);
									} else {
										newToken = newToken+newArgs[n];
									}
									if (n !=  newArgs.length-1){
										newToken = newToken+",";
									}
								}
								newA  =  newA + newToken+")";
								if (m !=  newATokens.length-1){
									newA = newA+"|";
								}
							}
						}

						// make the new B token (formatting)
						newB = "";
						for ( int m = 0; m < newBTokens.length; m++){
							String newToken = newBTokens[m];
							// replacement of all the unification
							if (!newToken.equals(bToken)){
								String[] newArgs = getArguments(newToken);
								newToken = getPredicate(newToken)+"(";
								for ( int n = 0; n < newArgs.length; n++){
									if (argMap.get(newArgs[n]) != null){
										newToken = newToken+argMap.get(newArgs[n]);
									} else {
										newToken = newToken+newArgs[n];
									}
									if (n !=  newArgs.length-1){
										newToken = newToken+",";
									}
								}
								newB =  newB + newToken+")";
								if (m !=  newBTokens.length-1){
									newB = newB+"|";
								}
							}
						}

						// make the new clause which comes once positive and negative literal clauses are in or operation
						String newString = null;
						if (newA.isEmpty() && newB.isEmpty()){
							return true;
						} else if (newA.isEmpty()){
							if(newB.charAt(newB.length()-1) == '|'){
								newB = newB.substring(0, newB.length()-1);
							}
							newString = newB;
						} else if (newB.isEmpty()){
							if(newA.charAt(newA.length()-1) == '|'){
								newA = newA.substring(0, newA.length()-1);
							}
							newString = newA;
						} else {
							if(newA.charAt(newA.length()-1) == '|'){
								newA = newA.substring(0, newA.length()-1);
							}
							if(newB.charAt(newB.length()-1) == '|'){
								newB = newB.substring(0, newB.length()-1);
							}
							newString = newA+"|"+newB;
						}
						
						String[] args = newString.split("\\|");
						HashMap<String,Boolean> argsMap =  new HashMap<String,Boolean>();
						for ( int m = 0; m < args.length; m++){
									argsMap.put(args[m], true);
						}
						newString = "";
						Set<String> keys = argsMap.keySet();
						if (!keys.isEmpty()){
							for ( String key : keys){
									newString = newString+key+"|";
							}
							newString = newString.substring(0, newString.length()-1);
							if (newString.isEmpty()){
								return true;
							}
						}
						if (newString.isEmpty() || newString.equals("")){
							continue;
						}

						// add the new string to the classmap
						if(classMap.add(newString)){
							
							String[] tokens = newString.split("\\|");

							//if the length of the new string is 1 and if the negated literal is present in the clause then it is nullified
							// and empty clause is achieved
							if (tokens.length == 1){
								if (classMap.contains(negateQuery(newString))){
									return true;
								}
							}

							// if not then add it to the list of class
							classList.add(newString);
						

							// update the predicate map
							// for each token in the new string, add the list of clauses to the predicate map containing specific predicate
							String[] tokenArray = newString.split("\\|");
							for ( int m = 0; m < tokenArray.length; m++){
								if (predicateMap.get(getPredicate(tokenArray[m])) != null){
									predicateMap.get(getPredicate(tokenArray[m])).add(newString);
								} else {
									List<String> list = new ArrayList<String>();
									list.add(newString);
									predicateMap.put(getPredicate(tokenArray[m]), list);
								}
							}
						} else {
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isAConstant(String string) {
		if (string.charAt(0) >= 65 && string.charAt(0) <= 90){
			return true;
		}
		return false;
	}

	private String[] getArguments(String token) {
		token = token.split("\\(")[1].split("\\)")[0];
		
		return token.split(",");
	}

	private static String getPredicate(String query){
		String[] split = query.split("\\(");
		return split[0];
	}

	/*
	Negate the query
	 */
	private static String negateQuery(String predicate){
		if (predicate.charAt(0) == '('){
			predicate = predicate.substring(1, predicate.length()-1);
		}
		predicate = "~"+predicate;
		if (predicate.substring(0, 2).equals("~~")){
			predicate = predicate.substring(2);
		}
		return predicate;
	}
	
	
	

}
