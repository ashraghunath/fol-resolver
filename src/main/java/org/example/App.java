package org.example;


import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Sentence;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {

//        FOLDomain domain = new FOLDomain();
//        domain.addPredicate("Member");
//        domain.addPredicate("Subset");
//        domain.addPredicate("Spherical");
//        domain.addPredicate("Spherical2");
//        System.out.println(domain);
//        domain.addConstant("BB9");
//        domain.addConstant("Basketballs");
//
//        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);
//        kb.tell("((Member(x,Basketballs) OR Spherical(x)) => Spherical2(x))");
//
//        List<Sentence> originalSentences = kb.getOriginalSentences();
//
//        FOLParser folParser = new FOLParser(domain);
//        CNFConverter cnfConverter = new CNFConverter(folParser);
//
//        CNF cnf = cnfConverter.convertToCNF(originalSentences.get(0));
//
//        System.out.println(cnf);


        FOLDomain domain = new FOLDomain();
        domain.addConstant("D");
        domain.addConstant("Jack");
        domain.addConstant("Curiosity");
        domain.addConstant("Tuna");
        domain.addPredicate("Cat");
        domain.addPredicate("Animal");
        domain.addPredicate("Subset");
        domain.addPredicate("Dog");
        domain.addPredicate("AnimalLover");
        domain.addPredicate("Kills");
        domain.addPredicate("Owns");

        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);

        kb.tell("(EXISTS x (Dog(x) AND Owns(Jack,x)))");
        kb.tell("(FORALL x (EXISTS y (Dog(y) AND Owns(x,y) => AnimalLover(x) ) ))");
        kb.tell("(FORALL x (AnimalLover(x) => (FORALL y (Animal(y) => NOT Kills(x,y)))))");

        kb.tell("(Kills(Jack,Tuna) OR Kills(Curiosity,Tuna))");
        kb.tell("(Cat(Tuna))");
        kb.tell("(FORALL x(Cat(x) => Animal(x)))");
        kb.tell("(Kills(Curiosity,Tuna))");

        FOLParser folParser = new FOLParser(domain);
        CNFConverter cnfConverter = new CNFConverter(folParser);

        List<Sentence> originalSentences = kb.getOriginalSentences();

        for (Sentence originalSentence : kb.getOriginalSentences()) {
            CNF cnf = cnfConverter.convertToCNF(originalSentence);

            System.out.println(cnf);
        }






    }
}
