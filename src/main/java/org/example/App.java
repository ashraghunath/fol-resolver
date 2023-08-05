package org.example;


import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Sentence;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App
{
    public static Set<String> predefined = new HashSet<String>(Arrays.asList("AND", "OR", "EXISTS", "FORALL", "NOT"));
    public static Set<String> sentences = new HashSet<>();
    public static Set<String> querySentences = new HashSet<>();
    public static Set<String> folSentences = new HashSet<String>();
    public static Set<String> folQuerySentences = new HashSet<String>();

    static int folQueriesCount, folStatementsCount;
    static String[] folInputqueries = null, folInputstatements = null;

    static String filepath ="src/main/FOL1.txt";
    public static void main( String[] args )
    {
        //Read FOL input
        readFOL(filepath);

        //Parse fol to accurate cnf
        Set<String> predicates = extractPredicates();
        Set<String> variables = extractVariables();
        Set<String> constants = extractConstants();
        String temp;
        predicates.removeAll(constants);

        removeCapitals(constants);

        FOLDomain domain = new FOLDomain();
        for(String c: constants) {
           domain.addConstant(c.charAt(0) + c.toLowerCase().substring(1));
        }
        for(String p: predicates) {
            domain.addPredicate(p);
        }

        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);
        FOLParser folParser = new FOLParser(domain);
        CNFConverter cnfConverter = new CNFConverter(folParser);


        for (String folInputquery : folQuerySentences) {
            kb.tell(folInputquery);
        }

        List<String> cnfQueries = convertToOutputClause(kb.getOriginalSentences(), cnfConverter);
        kb.clear();

        for (String s: folSentences) {
            kb.tell(s);
        }

        List<String> cnf = convertToOutputClause(kb.getOriginalSentences(), cnfConverter);


        outputCNF("src/main/CNF.txt",cnfQueries,cnf);

        Solution.solveCNF();
    }

    public static List<String> convertToOutputClause(List<Sentence> originalSentences, CNFConverter cnfConverter)
    {
        List<String> clausesBefore = new ArrayList<>();
        for (Sentence originalSentence : originalSentences) {
            CNF cnf = cnfConverter.convertToCNF(originalSentence);

            for (Clause conjunctionOfClause : cnf.getConjunctionOfClauses()) {

                StringBuilder sb = new StringBuilder();

                for (Literal literal : conjunctionOfClause.getLiterals()) {
                    sb.append(literal).append(" | ");
                }
                String clauseBeforeTrim = sb.toString();
                clausesBefore.add(clauseBeforeTrim.substring(0,clauseBeforeTrim.length()-3));
            }
        }

        return clausesBefore;
    }

    private static void outputCNF(String filepath, List<String> cnfQueries, List<String> cnf) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {

//            //TODO convert query and add
            writer.write(""+cnfQueries.size());
            writer.newLine();
            System.out.println(cnfQueries.size());
            for (String folInputquery : cnfQueries) {
                writer.write(folInputquery);
                writer.newLine();
                System.out.println(folInputquery);
            }

            writer.write(""+cnf.size());
            System.out.println(cnf.size());
            writer.newLine();
            for (String line : cnf) {
                writer.write(line);
                writer.newLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

    }

    public static void readFOL(String filepath)
    {
        Scanner scan = null;
        try {
            File file =  new File(filepath);
            scan =  new Scanner(file);
            String string =  scan.nextLine();
            folQueriesCount = Integer.parseInt(string);
            folInputqueries = new String[folQueriesCount];
            for ( int i = 0; i < folQueriesCount; i++){
                folInputqueries[i] = scan.nextLine();
            }

            querySentences.addAll(Arrays.asList(folInputqueries));

            folStatementsCount = Integer.parseInt(scan.nextLine());
            folInputstatements = new String[folStatementsCount];
            for (int i = 0; i < folStatementsCount; i++){
                folInputstatements[i] = scan.nextLine();
            }

            sentences.addAll(Arrays.asList(folInputstatements));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            scan.close();
        }
    }
    public static void removeCapitals(Set<String> constants) {
        String temp, s;
        for (String sentence : sentences) {
            s = sentence;
            for (String c : constants) {
                if (s.contains(c)) {
                    String temp1 = c.charAt(0) + (c.toLowerCase().substring(1));
                    s = s.replaceAll(c, temp1);
                }
            }
            folSentences.add(s);
        }

        for (String sentence : querySentences) {
            s = sentence;
            for (String c : constants) {
                if (s.contains(c)) {
                    String temp1 = c.charAt(0) + (c.toLowerCase().substring(1));
                    s = s.replaceAll(c, temp1);
                }
            }
            folQuerySentences.add(s);
        }
    }
    // Extracts predicates from the sentence
    public static Set<String> extractPredicates() {
        Set<String> predicates = new HashSet<String>();
        Pattern pattern = Pattern.compile("\\b[A-Z][a-zA-Z0-9]*\\b");
        for (String s: sentences) {
            Matcher matcher = pattern.matcher(s);
            String temp;

            while (matcher.find()) {
                temp = matcher.group();
                if (!App.predefined.contains(temp))
                    predicates.add(temp);
            }
        }
        return predicates;
    }

    // Extracts variables from the sentence
    public static Set<String> extractVariables() {
        Set<String> variables = new HashSet<String>();
        Pattern pattern = Pattern.compile("\\b[a-z][a-zA-Z0-9]*\\b");
        for (String s: sentences) {
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                variables.add(matcher.group());
            }
        }

        return variables;
    }

    // Extracts constants from the sentence
    public static Set<String> extractConstants() {
        Set<String> constants = new HashSet<String>();
        Pattern pattern = Pattern.compile("\\b[A-Z0-9]*\\b");
        for (String s: sentences) {
            Matcher matcher = pattern.matcher(s);

            while (matcher.find()) {
                String match = matcher.group();
                if (!App.predefined.contains(match) && match.length()!=0)
                    constants.add(match);
            }
        }
        return constants;
    }
}
