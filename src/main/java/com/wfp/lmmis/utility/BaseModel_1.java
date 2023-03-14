/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author icvgd
 */
public class BaseModel_1 {

    static Gson gson = null;

    public BaseModel_1() {
        buildGsonInstance();
    }

    public static Gson getGson() {
        if (gson == null) {
            buildGsonInstance();
        }
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    /**
     *
     * @return
     */
    public String toJsonString() {
        try {
            String toJson = getGson().toJson(this);
            return toJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return getGson().fromJson(json, classOfT);
    }

    private static void buildGsonInstance() {
        if (gson == null) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();

            gson = gsonBuilder.create();
        }
    }
}
