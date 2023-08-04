package org.example;


import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Sentence;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App
{
    public static Set<String> predefined = new HashSet<String>(Arrays.asList("AND", "OR", "EXISTS", "FORALL", "NOT"));
    public static Set<String> sentences = new HashSet<String>(Arrays.asList(
            "(EXISTS x (Dog(x) AND Owns(JACK,x)))",
            "(FORALL x (EXISTS y (Dog(y) AND Owns(x,y) => AnimalLover(x))))",
            "(FORALL x (AnimalLover(x) => (FORALL y (Animal(y) => NOT Kills(x,y)))))",
            "(Kills(JACK,TUNA) OR Kills(CURIOSITY,TUNA))",
            "(Cat(TUNA))",
            "(FORALL x (Cat(x) => Animal(x)))",
            "(Kills(CURIOSITY,TUNA))"
    ));
    public static Set<String> folSentences = new HashSet<String>();
    public static void main( String[] args )
    {
        Set<String> predicates = extractPredicates();
        Set<String> variables = extractVariables();
        Set<String> constants = extractConstants();
        String temp;
        predicates.removeAll(constants);

        System.out.println("Predicates: " + predicates);
        System.out.println("Variables: " + variables);
        System.out.println("Constants: " + constants);

        removeCapitals(constants);
        for (String s: folSentences) {
            // System.out.println(s);
        }

        FOLDomain domain = new FOLDomain();
        for(String c: constants) {
           domain.addConstant(c.charAt(0) + c.toLowerCase().substring(1));
        }
        for(String p: predicates) {
            domain.addPredicate(p);
        }
//        domain.addConstant("D");
//        domain.addConstant("Jack");
//        domain.addConstant("Curiosity");
//        domain.addConstant("Tuna");
//        domain.addPredicate("Cat");
//        domain.addPredicate("Animal");
//        domain.addPredicate("Subset");
//        domain.addPredicate("Dog");
//        domain.addPredicate("AnimalLover");
//        domain.addPredicate("Kills");
//        domain.addPredicate("Owns");

        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);

        for (String s: folSentences) {
            kb.tell(s);
        }

//        kb.tell("(EXISTS x (Dog(x) AND Owns(Jack,x)))");
//        kb.tell("(FORALL x (EXISTS y (Dog(y) AND Owns(x,y) => AnimalLover(x) ) ))");
//        kb.tell("(FORALL x (AnimalLover(x) => (FORALL y (Animal(y) => NOT Kills(x,y)))))");
//
//        kb.tell("(Kills(Jack,Tuna) OR Kills(Curiosity,Tuna))");
//        kb.tell("(Cat(Tuna))");
//        kb.tell("(FORALL x(Cat(x) => Animal(x)))");
//        kb.tell("(Kills(Curiosity,Tuna))");

        FOLParser folParser = new FOLParser(domain);
        CNFConverter cnfConverter = new CNFConverter(folParser);

        List<Sentence> originalSentences = kb.getOriginalSentences();

        for (Sentence originalSentence : kb.getOriginalSentences()) {
            CNF cnf = cnfConverter.convertToCNF(originalSentence);
            System.out.println(cnf);
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
