package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {

	private static String cnfFilePath = "src/main/resources/CNF.txt";

	public static void solveCNF() {
		System.out.println("\nResolution : \n");
		File file =  new File(cnfFilePath);

		int queriesCount, statementsCount;
		String[] queries = null, statements = null;
		try(Scanner scan =  new Scanner(file)) {

			String string =  scan.nextLine();
			queriesCount = Integer.parseInt(string);
			queries = new String[queriesCount];
			removeSpaces(scan, queriesCount, queries);

			statementsCount = Integer.parseInt(scan.nextLine());
			statements = new String[statementsCount];
			removeSpaces(scan, statementsCount, statements);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		CNFConverter cnfConverter =  new CNFConverter();
		statements = cnfConverter.convertToCNF(statements);

		Boolean[] result = new Boolean[queries.length];
		
		for ( int i = 0; i < queries.length; i++){
			HashMap<String,List<String>> predicateMap =  cnfConverter.getPredicateMap();

			Set<String> classMap = cnfConverter.getClassMap();
			
			Resolver resolver =  new Resolver( classMap, predicateMap);
			result[i] = resolver.resolution(queries[i]);
		}

		for ( int i = 0; i < result.length; i++){
			if (result[i]){
				System.out.println("Goal is true");
			} else {
				System.out.println("Goal is false");
			}
		}

	}

	private static void removeSpaces(Scanner scan, int queriesCount, String[] queries) {
		for ( int i = 0; i < queriesCount; i++){
			StringBuilder builder = new StringBuilder(scan.nextLine());
			for (int j = 0 ; j < builder.length(); j++){
				if (builder.charAt(j) == ' ' || builder.charAt(j) == '\t' || builder.charAt(j) == '\n'){
					builder.replace(j, j+1, "");
					j--;
				}
			}
			queries[i] = builder.toString();
		}
	}
}
