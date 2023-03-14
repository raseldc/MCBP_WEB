/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import org.springframework.stereotype.Component;

/**
 *
 * @author icvgd
 */

@Component
public class StringModification {
    
      public static String AddSpacesToSentence(String text, boolean preserveAcronyms) {
        if (text.isEmpty()) {
            return "";
        }
        StringBuilder newText = new StringBuilder(text.length() * 2);
        newText.append(text.charAt(0));
        for (int i = 1; i < text.length(); i++) {
            if ( Character.isUpperCase(text.charAt(i)))
                if ((text.charAt(i-1) != ' ' && !Character.isUpperCase(text.charAt(i - 1))) ||
                    (preserveAcronyms && Character.isUpperCase(text.charAt(i - 1)) && 
                     i< text.length() - 1 && !Character.isUpperCase(text.charAt(i +1)            )))
                    newText.append(' ');
            newText.append(text.charAt(i));
        }
        return newText.toString();
    }

}
