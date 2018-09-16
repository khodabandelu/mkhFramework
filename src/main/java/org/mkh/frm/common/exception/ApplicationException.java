package org.mkh.frm.common.exception;

import java.util.List;

public class ApplicationException extends RuntimeException {

    private String message = "";

    public ApplicationException(String exceptionKey) {
        this.message = exceptionKey;
    }

    public ApplicationException(List<String> exceptionsKey) {
        for (String expMsg : exceptionsKey) {
            message += expMsg + "<br/>";
        }
    }

    public String getMessage() {
        return this.message;
    }


}
