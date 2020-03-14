package com.jacjos.qvs.common;

public class QVSException extends Exception {

    public QVSException(String msg, Exception e){
        super(msg,e);
    }
}
