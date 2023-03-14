package com.wfp.lmmis.exception;

public class ExceptionWrapper extends Exception {

    private Object[] messageArray;
    private String message = "";

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the value of combinedMessage
     *
     * @return the value of message
     */
    public String getCombinedMessage() {
        String combinedMessage = "";
        if (messageArray != null) {
            for (Object msg : messageArray) {
                combinedMessage += msg + "\n";
            }
        }
        combinedMessage += this.message;
        return combinedMessage;
    }

    /**
     * Get the value of messageArray
     *
     * @return the value of messageArray
     */
    public Object[] getMessageArray() {
        return messageArray;
    }

    /**
     * Set the value of messageArray
     *
     * @param message new value of messageArray
     */
    public void setMessageArray(Object[] messageArray) {
        this.messageArray = messageArray;

    }

    /**
     *
     * @param messageArray
     */
    public ExceptionWrapper(Object[] messageArray) {
        this.messageArray = messageArray;
    }

    public ExceptionWrapper(String message) {

        this.message = message;
    }
}
