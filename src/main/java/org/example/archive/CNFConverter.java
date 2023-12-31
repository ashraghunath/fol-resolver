//package org.example;
//
//import java.util.*;
//
//public class CNFConverter {
//    private static List<HashMap<String,String>> mapList = new ArrayList<HashMap<String,String>>();
//    private static Set<String> clausemap = new HashSet<String>();
//    private static HashMap<String,List<String>> predicateMap = new HashMap<String,List<String>>();
//
//
//    public Set<String> getclausemap() {
//        return clausemap;
//    }
//
//    public void setclausemap(HashSet<String> clausemap) {
//        this.clausemap = clausemap;
//    }
//
//    public HashMap<String, List<String>> getPredicateMap() {
//        return predicateMap;
//    }
//
//    public void setPredicateMap(HashMap<String, List<String>> predicateMap) {
//        this.predicateMap = predicateMap;
//    }
//
//    public String[] extract(String[] statements) {
//        for ( int i = 0; i < statements.length; i++){
//            statements[i] = findPredicates(statements[i]);
////			statements[i] = findAllImplications(statements[i]);
////			statements[i] = standardiseParanthesis(statements[i]);
////			statements[i] = MoveNegationsInward(statements[i]);
////			statements[i] = handleDistribution(statements[i]);
////			statements[i] = eliminateBraces(statements[i]);
////			statements[i] = eliminateDuplicates(statements[i]);
//            //System.out.println(statements[i]);
//            findAllClassesList(statements[i],i);
//        }
//        //System.out.println();
//        Set<String> clausemapTemp = new HashSet<String>();
//        int  i = 0;
//        for ( String clause : clausemap){
//            clause = variableStandardization(clause, i);
//            clausemapTemp.add(clause);
//            i++;
//        }
//        clausemap = clausemapTemp;
//
//        for (String clause : clausemap){
//            fillKB(clause);
//        }
//
//        return statements;
//    }
//
//
//
//
//    private String eliminateDuplicates(String string) {
//        String[] tokensList = string.split("\\|");
//        Set<String> tokenSet = new HashSet<String>();
//        String tokenString = "";
//        for ( int i = 0; i < tokensList.length; i++ ){
//            if (tokenSet.add(tokensList[i])){
//                tokenString  = tokenString + tokensList[i]+"|";
//            }
//        }
//        tokenString = tokenString.substring(0, tokenString.length()-1);
//        return tokenString;
//    }
//
//    private void fillKB(String clause) {
//        String[] predicatesList = clause.split("\\|");
//
//        for (int i = 0; i < predicatesList.length; i++){
//            predicatesList[i] = getPredicate(predicatesList[i]);
//        }
//
//        for (int j = 0; j < predicatesList.length; j++){
//            while(predicatesList[j].charAt(predicatesList[j].length()-1) == '%'){
//                predicatesList[j] = predicatesList[j].substring(0, predicatesList[j].length()-1);
//            }
//        }
//        for (int j = 0; j < predicatesList.length; j++){
//            if (predicateMap.get(predicatesList[j]) != null){
//                predicateMap.get(predicatesList[j]).add(clause);
//            } else {
//                List<String> tokens = new ArrayList<String>();
//                tokens.add(clause);
//                predicateMap.put(predicatesList[j], tokens);
//            }
//        }
//
//    }
//
//    private static String getPredicate(String query){
//        String[] split = query.split("\\(");
//        return split[0];
//    }
//
//    private String eliminateBraces(String string){
//        while (string.charAt(0) == '('){
//            string = string.substring(1);
//        }
//        while (string.charAt(string.length()-1) == '('){
//            string = string.substring(0,string.length()-1);
//        }
//        for ( int i = 0; i < string.length(); i++){
//            if (string.charAt(i) == '(' || string.charAt(i) == ')'){
//                string = string.substring(0, i)+string.substring(i+1);
//            }
//        }
//        return string;
//    }
//
//
//    private void findAllClassesList(String string, int statementNumber) {
//        HashMap<String,String> map = mapList.get(statementNumber);
//        if (string.charAt(0) == '('){
//            //int open = 1;
//            int start = 1;
//            for ( int i = 1; i < string.length(); i++ ){
//                if (string.charAt(i) == ')'){
//                    //open--;
//                    if (i == string.length()-1){
//                        String tokenString = string.substring(start, i);
//
//                        String[] predicates = tokenString.split("\\|");
//                        tokenString = "";
//                        for ( int j = 0; j < predicates.length; j++){
//                            String predicateString = predicates[j].trim();
//                            if (predicateString.charAt(0) == '('){
//                                predicateString = predicateString.substring(1);
//                            }
//                            if (predicateString.charAt(predicateString.length()-1) == ')'){
//                                predicateString = predicateString.substring(0, predicateString.length()-1);
//                            }
//                            predicateString = map.get(predicateString);
//
//                            tokenString = tokenString + predicateString;
//                            if (j != predicates.length-1){
//                                tokenString =  tokenString + "|";
//                            }
//                        }
//                        clausemap.add(tokenString);
//                    }
//                }
//
//                if (string.charAt(i) == '&'){
//                    String tokenString = string.substring(start, i);
//
//                    start = i+1;
//                    String[] predicates = tokenString.split("\\|");
//                    tokenString = "";
//                    for ( int j = 0; j < predicates.length; j++){
//                        String predicateString = predicates[j].trim();
//                        if (predicateString.charAt(0) == '('){
//                            predicateString = predicateString.substring(1);
//                        }
//                        if (predicateString.charAt(predicateString.length()-1) == ')'){
//                            predicateString = predicateString.substring(0, predicateString.length()-1);
//                        }
//
//                        predicateString = map.get(predicateString);
//
//                        tokenString = tokenString+predicateString;
//                        if (j != predicates.length-1){
//                            tokenString =  tokenString + "|";
//                        }
//                    }
//
//                    clausemap.add(tokenString);
//                }
//
//            }
//        } else {
//            String[] classesList = string.split("&");
//            for ( int i = 0; i < classesList.length; i++){
//                String tokenString = classesList[i];
//
//                String[] predicates = tokenString.split("\\|");
//                tokenString = "";
//                for ( int j = 0; j < predicates.length; j++){
//                    String predicateString = predicates[j].trim();
//                    if (predicateString.charAt(0) == '('){
//                        predicateString = predicateString.substring(1);
//                    }
//                    if (predicateString.charAt(predicateString.length()-1) == ')'){
//                        predicateString = predicateString.substring(0, predicateString.length()-1);
//                    }
//
//                    predicateString = map.get(predicateString);
//
//                    tokenString = tokenString+predicateString;
//                    if (j != predicates.length-1){
//                        tokenString =  tokenString+"|";
//                    }
//                }
//
//                clausemap.add(tokenString);
//            }
//
//        }
//
//        //System.out.println(statementNumber+" "+clausemap);
//
//    }
//
//
//    private String handleDistribution(String string){
//        if (string.isEmpty()){
//            return string;
//        }
//        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')'){
//            string = string.substring(1,string.length()-1);
//        }
//        if (string.split("&").length == 1){
//            return string;
//        }
//
//        for ( int i = 0; i < string.length(); i++){
//            //System.out.println(string.charAt(i));
//            if (string.charAt(i) == '|'){
//                List<String> previousTokens = new ArrayList<String>();
//                List<String> nextTokens = new ArrayList<String>();
//                int rightIndex = 0, leftIndex = string.length()-1;
//                // LEFT TOKENS
//                if (string.charAt(i-1) == ')'){
//                    int close = 1;
//
//                    int  j = i-2;
//                    while (j >= 0){
//                        if (string.charAt(j) == '('){
//                            close--;
//                            if (close == 0){
//                                break;
//                            }
//                        } else if (string.charAt(j) == ')'){
//                            close++;
//                        }
//                        j--;
//                    }
//                    rightIndex = j;
//                    String clause = string.substring(j+1, i-1);
//                    clause = handleDistribution(clause);
//
//                    String[] tokens = clause.split("&");
//                    for (String token : tokens){
//                        previousTokens.add(token);
//                    }
//                } else {
//                    int j = i-1;
//                    while ( j >= 0){
//                        if(string.charAt(j) == '('){
//                            break;
//                        }
//                        j--;
//                    }
//                    rightIndex = j+1;
//                    previousTokens.add(string.substring(j+1,i));
//                }
//
//
//                // RIGHT TOKENS
//
//                if (string.charAt(i+1) == '('){
//                    int open = 1;
//                    int j = i+2;
//                    while (j < string.length()){
//                        if (string.charAt(j) == ')'){
//                            open--;
//                            if ( open == 0){
//                                break;
//                            }
//                        } else if (string.charAt(j) == '('){
//                            open++;
//                        }
//                        j++;
//                    }
//                    leftIndex = j;
//                    String clause = string.substring(i+2,leftIndex);
//                    clause = handleDistribution(clause);
//
//                    String[] tokens = clause.split("&");
//                    for (String token : tokens){
//                        nextTokens.add(token);
//                    }
//                } else {
//                    int  j = i+1;
//                    while (j < string.length()){
//                        if (string.charAt(j) == ')'){
//                            break;
//                        }
//                        j++;
//                    }
//                    leftIndex = j;
//                    if (j+1 >= string.length()){
//                        leftIndex = string.length();
//                    }
//                    nextTokens.add(string.substring(i+1,leftIndex));
//                }
//
//                String rightString = null;
//                String leftString = null;
//                if (rightIndex == -1){
//                    rightString = "";
//                } else {
//                    rightString = string.substring(0,rightIndex);
//                }
//                if (leftIndex == -1){
//                    leftString = "";
//                } else {
//                    leftString = string.substring(leftIndex);
//                }
//
//                String distribution = "";
//                for ( int j = 0; j < previousTokens.size(); j++){
//                    String previous =  previousTokens.get(j);
//                    for ( int k = 0; k < nextTokens.size(); k++){
//                        String token = previous+"|"+nextTokens.get(k);
//                        distribution = distribution + token + "&";
//                        if (j == previousTokens.size()-1){
//                            if (k == nextTokens.size()-1)
//                                distribution = distribution.substring(0, distribution.length()-1);
//                        }
//                    }
//                }
//                distribution = distribution + "";
//                string = rightString+distribution+leftString;
//                string = standardiseParanthesis(string);
//
//            }
//        }
//
//        return string;
//    }
//
//    private String MoveNegationsInward(String string) {
//        // Move through the string looking for negations
//        for ( int i = 0; i < string.length(); i++){
//
//            // Remove double negation
//            if (string.charAt(i) == '~' && string.charAt(i+1) == '~'){
//                string = string.substring(0,i) + string.substring(i+2);
//            }
//
//            // Distribute negations
//            if (string.charAt(i) == '~' && string.charAt(i+1) == '('){
//
//                //Remove the negation
//                string = string.substring(0,i) + "(~"+string.substring(i+2);
//
//                int right = 1;
//                int rightIndex = i+2;
//                //Distribute the negation over the next expression according to DeMorgan's laws
//                int j = 0;
//                for (  j = 1; i+j < string.length() && right != 0; j++){
//                    if (string.charAt(i+j) == '('){
//                        right++;
//                        continue;
//                    }
//                    if (right == 1){
//                        //Move within the brackets and change | to & and vice versa. Also add ~
//                        if (string.charAt(i+j) == '|' || string.charAt(i+j) == '&'){
//                            //string = string.substring(0,rightIndex)+MoveNegationsInward(string.substring(rightIndex,i+j))+string.substring(i+j);
//                            if (string.charAt(i+j) == '|'){
//                                string = string.substring(0,i+j)+"&"+string.substring(i+j+1);
//                            } else if (string.charAt(i+j) == '&'){
//                                string = string.substring(0,i+j)+"|"+string.substring(i+j+1);
//                            }
//                            string = string.substring(0, i+j+1)+"~"+string.substring(i+j+1);
//                            string = string.substring(0,rightIndex)+MoveNegationsInward(string.substring(rightIndex,i+j+1))+string.substring(i+j+1);
//                        }
//                    }
//                    if (string.charAt(i+j) == ')'){
//                        right--;
//                        continue;
//                    }
//
//                }
//            }
//        }
//        //System.out.println("Returning: "+string);
//        return string;
//    }
//
//    private String standardiseParanthesis(String string) {
//        int closingIndex = -1;
//        for ( int i = 0; i < string.length(); i++){
//            if (string.charAt(i) == '('){
//                int openCount = 1;
//                int  j = i+1;
//                boolean isUseless = true;
//                while (openCount > 0 && j < string.length()){
//                    if (string.charAt(j) == ')' && openCount == 1){
//                        //closes = true;
//                        openCount--;
//                        closingIndex = j;
//                        //System.out.println("Closing "+closingIndex);
//                        j++;
//                        continue;
//                    } else if (string.charAt(j) == ')'){
//                        openCount--;
//                        j++;
//                        continue;
//                    }
//                    if (string.charAt(j) == '('){
//                        openCount++;
//                        j++;
//                        continue;
//                    }
//
//                    if (openCount == 1){
//                        if (string.charAt(j) == '|' || string.charAt(j) == '&'){
//                            //System.out.println("Don't Remove");
//                            isUseless = false;
//                            //break;
//                        }
//                    }
//                    j++;
//
//                }
//                if (isUseless){
//                    string = string.substring(0, i)+string.substring(i+1);
//                    if ( closingIndex != -1){
//                        string = string.substring(0,closingIndex-1)+string.substring(closingIndex);
//                    }
//                    i = i-1;
//                }
//            }
//        }
//        return string;
//    }
//
//    private String findAllImplications(String string) {
//        for ( int i = 0 ; i < string.length(); i++){
//            if (string.charAt(i) == '=' && string.charAt(i+1) == '>'){
//                int right = 0, left = 0;
//                boolean foundRight = false;
//                boolean foundLeft = false;
//                int rightIndex = -1,leftIndex = -1;
//                for ( int j = 1; i-j >= 0; j++){
//                    // Moving to the right
//                    if (!foundRight){
//                        if (string.charAt(i-j) == '(' ){
//                            if (right != 0){
//                                right--;
//                            } else {
//                                foundRight = true;
//                                rightIndex = i-j+1;
//                                break;
//                            }
//                        }
//                        if (string.charAt(i-j) == ')' ){
//                            right++;
//                        }
//                    }
//                }
//
//                for(int j = 0;  i+j < string.length();j++){
//                    // Moving to the left
//                    if (!foundLeft){
//                        if (string.charAt(i+j) == ')' ){
//                            if (left != 0){
//                                left--;
//                            } else {
//                                foundLeft = true;
//                                leftIndex = i+j;
//                                break;
//                            }
//                        }
//                        if (string.charAt(i+j) == '(' ){
//                            left++;
//                        }
//                    }
//                }
//
//                if (rightIndex == -1)
//                    rightIndex = 0;
//                if (leftIndex == -1)
//                    leftIndex = string.length();
//                string = replaceImplication(string,rightIndex,leftIndex,i);
//            }
//        }
//
//        return string;
//    }
//
//    private String replaceImplication(String string, int rightIndex, int leftIndex, int center) {
//        String temp =string;
//        StringBuilder builder = new StringBuilder(temp.substring(0,rightIndex));
//        string = string.substring(rightIndex,leftIndex);
//
//
//        String rightTokenString = "~"+string.substring(0, center-rightIndex).trim();
//        String leftTokenString = string.substring(center-rightIndex+2);
//        string = rightTokenString+"|"+leftTokenString;
//
//        builder.append(string);
//        builder.append(temp.substring(leftIndex));
//
//        return builder.toString();
//    }
//
//    private String findPredicates(String string) {
//        HashMap<String, String> map = new HashMap<String,String>();
//        for ( int i = 0; i < string.length(); i++){
//            if ( string.charAt(i) >= 65 && string.charAt(i) <= 90){
//                int openIndex = -1;
//
//                int j = i;
//                while (string.charAt(j)!= ')'){
//                    if (string.charAt(j) == '('){
//                        openIndex = j;
//                    }
//                    j++;
//                }
//                String predicateString = string.substring(i,openIndex);
//                String operandString = string.substring(i,j+1);
//                String additions = "";
//                while (map.get(predicateString)!= null){
//                    if (map.get(predicateString).equals(operandString)){
//                        break;
//                    } else {
//                        predicateString = predicateString+"%";
//                        additions = additions+"%";
//                    }
//                }
//
//                map.put(predicateString,operandString);
//                map.put("~"+predicateString, "~"+operandString);
//
//                string = string.substring(0, openIndex)+additions+string.substring(j+1);
//                i = openIndex;
//            }
//        }
//        mapList.add(map);
//        return string;
//    }
//
//    private String variableStandardization(String string, int index) {
//        int rightIndex = -1;
//        boolean isVariable =  false;
//        for ( int i = 0; i < string.length(); i++){
//            if (string.charAt(i) == '('){
//                rightIndex = i+1;
//                isVariable = true;
//            } else if (string.charAt(i) == ')'){
//                if (isVariable){
//                    while ( rightIndex <= i){
//                        if (string.charAt(rightIndex) == ',' || string.charAt(rightIndex) == ')'){
//                            if (isVariable){
//
//                                if (string.charAt(rightIndex) == ')'){
//                                    isVariable =  false;
//                                }
//
//                                int len = rightIndex-1;
//                                while (len >= 0){
//                                    if (string.charAt(len) == ',' || string.charAt(len) == '(' ){
//                                        break;
//                                    }
//                                    len--;
//                                }
//                                if (string.charAt(len+1) >= 97 && string.charAt(len+1) <= 122){
//                                    int stringLen = string.length();
//                                    string = string.substring(0, rightIndex)+index+string.substring(rightIndex);
//                                    i = i + string.length() - stringLen;
//                                    rightIndex = rightIndex + string.length() - stringLen;
//                                }
//
//                            }
//                        }
//                        rightIndex++;
//                    }
//                }
//            } else {
//                if (string.charAt(i) == '|' || string.charAt(i) == '&' || string.charAt(i) == '~'){
//                    isVariable =  false;
//                }
//            }
//        }
//        return string;
//    }
//}
