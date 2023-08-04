package org.example;

import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.FOLKnowledgeBase;

public class Utils{
    
    public static FOLDomain getOntologyBasicDomain(){
        FOLDomain domain = new FOLDomain();
        domain.addPredicate("Member");
        domain.addPredicate("Subset");
        domain.addPredicate("Disjoint");
        domain.addPredicate("ExhaustiveDecomposition");
        domain.addPredicate("Partition");
        domain.addFunction("Intersection");
        domain.addConstant("Phi");
        return domain;
    }
    
    public static FOLDomain getFiveExampleDomain(){
        FOLDomain domain = Utils.getOntologyBasicDomain();
        // Add the category basketballs to the domain
        domain.addConstant("Basketballs");
        // Add the basketball BB9 to the domain
        domain.addConstant("BB9");
        // Add the category Balls
        domain.addConstant("Balls");
        domain.addConstant("Nine");// because our knowledge doesnot include real numbers
        //Add relevant properties
        domain.addPredicate("Spherical");
        domain.addPredicate("Round");
        domain.addPredicate("Orange");
        domain.addPredicate("Diameter");
        domain.addConstant("Dogs");
        domain.addConstant("DomesticatedSpecies");
        return domain;
    }
    
    public static FOLKnowledgeBase getFiveExampleKnowledgeBase(){
        FOLKnowledgeBase kb = new FOLKnowledgeBase(Utils.getOntologyBasicDomain());

        //BB9 ∈ Basketballs
        kb.tell("Member(BB9, Basketballs)");

        // Basketballs ⊂ Balls
        kb.tell("Subset(Basketballs,Balls)");

        // ( x ∈ Basketballs ) ⇒ Spherical(x) 
        kb.tell("(Member(x,Basketballs) => Spherical(x))");

        // Orange(x) ∧ Round(x) ∧ Diameter(x) = 9.5″ ∧ x ∈ Balls ⇒ x ∈ Basketballs
        kb.tell("((((Orange(x) AND Round(x)) AND Diameter(x,Nine) AND Member(x, Balls))) => Member(x, Basketballs))");

        // Dogs ∈ DomesticatedSpecies
        kb.tell("Member(Dogs,DomesticatedSpecies)");
        
        return kb;

    }
    
    public static FOLKnowledgeBase getCategoryKnowledgeBase(FOLDomain domain){
        domain.addPredicate("Member");
        domain.addPredicate("Subset");
        domain.addPredicate("Disjoint");
        domain.addPredicate("ExhaustiveDecomposition");
        domain.addPredicate("Partition");
        domain.addFunction("Intersection");
        domain.addConstant("Phi");
        domain.addPredicate("PartOf");
        domain.addPredicate("LessThan");
        domain.addPredicate("GreaterThan");
        
        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain);
        // equality axioms
        // Reflexivity Axiom
        kb.tell("x = x");
        // Symmetry Axiom
        kb.tell("(x = y => y = x)");
        // Transitivity Axiom
        kb.tell("((x = y AND y = z) => x = z)");
        // Function Intersection Substitution Axiom
        kb.tell("(( x = y AND w = z) => ( Intersection(x,w) = Intersection(w,z) ))");
        // Predicate Substitution Axioms
        kb.tell("((x = y AND v = w AND Member(x,v)) => Member(y,w))");
        kb.tell("((x = y AND v = w AND Disjoint(x,v)) => Disjoint(y,w))");
        kb.tell("((x = y AND v = w AND ExhaustiveDecomposition(x,v)) => ExhaustiveDecomposition(y,w))");
        kb.tell("((x = y AND v = w AND Partition(x,v)) => Partition(y,w))");

        // Definition of disjoint Disjoint(s) ⟺ ( ∀ c1,c2 c1 ∈ s ∧ c2 ∈ s  ∧ c1 ≠ c2 ⟹ Intersection(c1,c2) = {})
        kb.tell(" ( Disjoint(s) <=> (FORALL x,y (( Member(x,s) AND Member(y,s) AND (NOT ( x = y ))) => (Intersection(x,y) = Phi)))) ");

        // Definition of ExhaustiveDecomposition ExhaustiveDecomposition(s,c) ⟺ ( ∀i i ∈ c ⟺ ∃ c2 c2 ∈ s ∧ i ∈ c2 )
        kb.tell("(ExhaustiveDecomposition(s,c) <=> ( FORALL i ( Member(i,c) => (EXISTS c2 (Member(c2,s) AND Member(i,c2))))))");

        // Definition of Partition
        kb.tell("(Partition(s,c) <=> ( Disjoint(s) AND ExhaustiveDecomposition(s)))");

        // Definition for intersection
        kb.tell("( Member(x, Intersection(s,b)) <=> (Member(x,a) AND Member(x,b)))");
    
        //Definition of Phi
        kb.tell("(FORALL x  NOT Member(x,Phi))");
        
        kb.tell("(Subset(a,b) <=> (FORALL x (Member(x,a) => Member(x,b))))");
        
        // now add the partof relation
        kb.tell("(FORALL x (PartOf(x,x)))");
        kb.tell("((PartOf(x,y) AND PartOf(y,z)) => PartOf(x,z))");
        
        //ordering axioms
        kb.tell("((LessThan(x,y) AND LessThan(y,z)) => LessThan(x,z))");
        kb.tell("((GreaterThan(x,y) AND GreaterThan(y,z)) => GreaterThan(x,z))");
        kb.tell("(LessThan(x,y) <=> GreaterThan(y,x))");
        
        return kb;
    }
}