/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

/**
 *
 * @author Philip
 */
public class ControllerMessage
{

    private ControllerMessageType messageType;
    private String message;

    public ControllerMessage()
    {
    }
    
    public ControllerMessage(ControllerMessageType messageType, String message)
    {
        this.messageType = messageType;
        this.message = message;
    }

    /**
     *
     * @return
     */
    public ControllerMessageType getMessageType()
    {
        return messageType;
    }

    public void setMessageType(ControllerMessageType messageType)
    {
        this.messageType = messageType;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
