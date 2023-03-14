/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class RecapchaResponse {

    private boolean success;
    private String challenge_ts;
    private String hostname;

    public boolean isSuccess() {
        return success;
    }

    /**
     *
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallenge_ts() {
        return challenge_ts;
    }

    /**
     *
     * @param challenge_ts
     */
    public void setChallenge_ts(String challenge_ts) {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname() {
        return hostname;
    }

    /**
     *
     * @param hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
    
    
}
