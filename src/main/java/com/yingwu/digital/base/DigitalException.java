package com.yingwu.digital.base;

public class DigitalException extends RuntimeException{

    private static final long serialVersionUID = -6107121310339669978L;

    private String errorCode;

    private String errorMessage;

    public DigitalException(String errorCode){
        this.errorCode = errorCode;
    }

    public DigitalException(String errorCode,Throwable e){
        super(e);
        this.errorCode = errorCode;
    }

    public DigitalException(String errorCode, String errorMessage) {
        super("errorCode="+errorCode+"|errorMessage="+errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
