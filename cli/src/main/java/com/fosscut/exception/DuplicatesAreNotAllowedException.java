package com.fosscut.exception;

public class DuplicatesAreNotAllowedException extends Exception {

    private static String staticMessage = "Duplicates detected while generating ";
    private static String staticSuggestionStart = "Duplicates can be allowed using: '--allow-";
    private static String staticSuggestionEnd = "-type-duplicates'.";

    public DuplicatesAreNotAllowedException(String duplicateType) {
        super(staticMessage + duplicateType + " elements.\n"
            + staticSuggestionStart + duplicateType + staticSuggestionEnd);
    }

}
