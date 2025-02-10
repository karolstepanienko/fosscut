package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;

public class DuplicatesAreNotAllowedException extends FosscutException {

    private static String staticMessage = "Duplicates detected while generating ";
    private static String staticSuggestionStart = "Duplicates can be allowed using: '--allow-";
    private static String staticSuggestionEnd = "-type-duplicates'.";

    public DuplicatesAreNotAllowedException(String duplicateType) {
        super(staticMessage + duplicateType + " elements.\n"
            + staticSuggestionStart + duplicateType + staticSuggestionEnd);
    }

}
