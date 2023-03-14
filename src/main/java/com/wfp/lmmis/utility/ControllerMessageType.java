/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

/**
 *
 * @author Philip
 */
public enum ControllerMessageType
{

    SUCCESS, INFO, WARNING, DANGER;

    @Override
    public String toString()
    {
        switch (this)
        {
            case SUCCESS:
                return "Success";
            case INFO:
                return "Info";
            case WARNING:
                return "Warning";
            case DANGER:
                return "Danger";
            default:
                throw new AssertionError();
        }
    }
}
