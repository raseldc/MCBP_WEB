/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperToFIleCreate;

import java.lang.reflect.Field;

/**
 *
 * @author rasel
 */
public class ConverterClass<E> {
     public static <E>  void  converter(E dbsRole) {
        //testCalss dbsRole = new testCalss();
        Field[] fields = dbsRole.getClass().getDeclaredFields();
        
        String converterClass = "public class %s {";
         
        
        for(Field field : fields)
        {
            System.out.println("Name : "+field.getName());
            System.out.println("type : "+field.getType().toString());
        }
        

    }
}
