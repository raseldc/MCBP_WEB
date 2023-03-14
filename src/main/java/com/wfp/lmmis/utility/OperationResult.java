/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

/**
 *
 * @author shamiul
 */
public class OperationResult {

    Boolean success;
    NIDResponseError error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public NIDResponseError getError() {
        return error;
    }

    public void setError(NIDResponseError error) {
        this.error = error;
    }

}
