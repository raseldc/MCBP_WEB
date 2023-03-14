/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.util.List;

/**
 *
 * @author icvgd
 */
public class JsonResult<E> extends BaseModel {

    boolean isError;
    String errorMsg; 
    int errorCode; // 500 server error, 505 session time out , 0 success 
    List<E> returnObject;

    E returnSingleObject;

    public List<E> getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(List<E> returnObject) {
        this.returnObject = returnObject;
    }

    public JsonResult(boolean isError, String errorMsg) {
        this.isError = isError;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public E getReturnSingleObject() {
        return returnSingleObject;
    }

    public void setReturnSingleObject(E returnSingleObject) {
        this.returnSingleObject = returnSingleObject;
    }
    

}
