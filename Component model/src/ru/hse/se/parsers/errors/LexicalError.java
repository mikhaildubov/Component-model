package ru.hse.se.parsers.errors;

import java.util.ArrayList;
import java.util.HashSet;

public class LexicalError extends ParsingError {

    public LexicalError(String error, int line, String badToken,
                        HashSet<String> possibleSubstitutions) {

        super(error + 
              suggestSubstitutions(badToken, possibleSubstitutions), line);
    }
    
    private static String suggestSubstitutions(String badToken,
                        HashSet<String> possibleSubstitutions) {

        if (possibleSubstitutions == null) {
            return "";
        }

        // !!! Suggesting substitutions !!!
        ArrayList<String> toSuggest = new ArrayList<String>();
        String subst;
        
        // 1. Transposing of adjacent letters
        for (int i = 0; i < badToken.length()-1; i++) {
            subst = badToken.substring(0, i) +
                        badToken.charAt(i+1) +
                        badToken.charAt(i) +
                        badToken.substring(i+2);
            if (possibleSubstitutions.contains(subst)) {
                toSuggest.add(subst);
            }
        }
        
        // 2. Removal of each letter
        for (int i = 0; i < badToken.length(); i++) {
            subst = badToken.substring(0, i) + badToken.substring(i+1);
            if (possibleSubstitutions.contains(subst) &&
                    (i == 0 || badToken.charAt(i) != badToken.charAt(i-1))) {
                toSuggest.add(subst);
            }
        }
        
        // 3. Replacement of each letter
        for (int i = 0; i < badToken.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                subst = badToken.substring(0, i) +
                        c + badToken.substring(i+1);
                if (possibleSubstitutions.contains(subst)) {
                    toSuggest.add(subst);
                }
            }
        }
        
        // 4. Inserting any letter at any position in a word
        for (int i = 0; i < badToken.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                subst = badToken.substring(0, i) +
                        c + badToken.substring(i);
                if (possibleSubstitutions.contains(subst) &&
                        subst.charAt(i) != subst.charAt(i+1)) {
                    toSuggest.add(subst);
                }
            }
        }
        
        
        if (! toSuggest.isEmpty()) {
            
            String res = " (did you mean ";
            for (String s : toSuggest) {
                res += ("'" + s + "'/");
            }
            res = res.substring(0, res.length()-1) + "?)";
            
            return res;
        } else {
            return "";
        }
    }

    private static final long serialVersionUID = 1L;
}
