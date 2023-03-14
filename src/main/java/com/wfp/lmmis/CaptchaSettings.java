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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "google.recaptcha")
public class CaptchaSettings {

    private String url;
    private String key;
    private String secret;

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     */
    public String getSecret() {
        return secret;
    }

    /**
     *
     * @param secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
