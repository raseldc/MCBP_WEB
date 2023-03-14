/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sarwar
 */
public class JsonParserUtility
{

    public static Map<String, String> getStringData(String jsonData)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public static Map<String, String> getStringData(String jsonData)
//    {
//        System.out.println("jsonData=" + jsonData);
//        Map<String, String> result = null;
//        try
//        {
//            result = new ObjectMapper().readValue(jsonData, TypeFactory.mapType(HashMap.class, String.class, String.class));
//            System.out.println("result=" + result);
//            return result;
//        }
//        catch (IOException ex)
//        {
//            //logger.getlogger(JsonParserUtility.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return result;
//    }
//    public static String getJsonArrayFromList(List list)
//    {
//        try
//        {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            // Convert Java Object to Json
//            String jsonArray = gson.toJson(list);
//            //Return Json in the format required by jTable plugin\
//            //jsonArray = "{\"success\":\"true\",\"menu\":" + jsonArray + "}";
//            return jsonArray;
//        }
//        catch (Exception e)
//        {
//            System.out.println("exc in converting to json = " + e.getMessage());
//        }
//        return "";
//    }
//
//    public static String getJSONFromObject(Object object)
//    {
//        try
//        {
//            GsonBuilder gb = new GsonBuilder();
//            gb.serializeNulls();
//            Gson gson = gb.create();
//            String stringJson = gson.toJson(object);
//            System.out.println("stringJson=" + stringJson);
//            return stringJson;
//        }
//        catch (Exception e)
//        {
//            System.out.println("e = " + e.getMessage());
//        }
//        return "";
//    }

//    public static <T extends Object> T getJavaObjectFromJSONObject(String jSONObject, Class<T> clazz)
//    {
//        Gson gson = new GsonBuilder().registerTypeAdapter(Calendar.class, new JsonDeserializer<Calendar>()
//        {
//            @Override
//            public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//                    throws JsonParseException
//            {
//                String calendarSt = json.getAsString();
//                Calendar calendar = DateUtil.stringToCalendar(calendarSt);
//                return calendar;
//            }
//        }).create();
//
//        return (T) gson.fromJson(jSONObject, clazz);
//    }
}
