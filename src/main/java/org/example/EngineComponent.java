package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EngineComponent {

	private static String cnfFilePath = "src/main/resources/CNF.txt";

	public static void solveCNF() {
		System.out.println("\n******** Resolution ********  \n");
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
		
		
		Extractor extractor =  new Extractor();
		extractor.extract(statements);

		Boolean[] result = new Boolean[queries.length];
		
		for ( int i = 0; i < queries.length; i++){
			HashMap<String,List<String>> predicateMap =  extractor.getPredicateMap();

			Set<String> clausemap = extractor.getclausemap();
			
			Resolver resolver =  new Resolver( clausemap, predicateMap);
			result[i] = resolver.resolution(queries[i]);
		}

		for ( int i = 0; i < result.length; i++){
			if (result[i]){
				System.out.println("\nGoal is true");
			} else {
				System.out.println("\nGoal is false");
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
