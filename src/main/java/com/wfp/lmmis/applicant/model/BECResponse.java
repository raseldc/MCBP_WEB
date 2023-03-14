/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author user
 */
public class BECResponse implements Serializable
{
    private boolean matchFound;
    private BECNidResponse nidData;
    private String errorCode;
    private boolean operationResult;
    private String errorMsg;

    public boolean isMatchFound()
    {
        return matchFound;
    }

    /**
     *
     * @param matchFound
     */
    public void setMatchFound(boolean matchFound)
    {
        this.matchFound = matchFound;
    }

    /**
     *
     * @return
     */
    public BECNidResponse getNidData()
    {
        return nidData;
    }

    public void setNidData(BECNidResponse nidData)
    {
        this.nidData = nidData;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public boolean isOperationResult()
    {
        return operationResult;
    }

    public void setOperationResult(boolean operationResult)
    {
        this.operationResult = operationResult;
    }

    /**
     *
     * @return
     */
    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }
    
    
}
