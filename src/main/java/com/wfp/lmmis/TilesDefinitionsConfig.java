/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.request.Request;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author icvgd
 */
/* <code>Apache tiles configuration class  

 . Implements DefinitionsFactory to provide programmatic configuration for Apache tiles.</code>
 *
 */
public final class TilesDefinitionsConfig implements DefinitionsFactory {

    private static final Map<String, Definition> tilesDefinitions = new HashMap<String, Definition>();
    private static final Attribute BASE_TEMPLATE = new Attribute("/WEB-INF/jsp/template/SiteTemplate.jsp") {
    };

    /**
     * @param name <code>Name of the view</code>
     * @param title <code>Page title</code>
     * @param body <code>Body JSP file path</code>
     *
     * <code>Adds default layout definitions</code>
     */
    private static void addDefaultLayoutDef(String name, String title, String body) {
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();

        attributes.put("title", new Attribute(title) {
        });
        attributes.put("header", new Attribute("/WEB-INF/jsp/template/header.jsp"));
        //attributes.put("menu", new Attribute("/WEB-INF/views/layout/menu.jsp"));
        attributes.put("body", new Attribute(body));
        attributes.put("footer", new Attribute("/WEB-INF/jsp/template/footer.jsp"));

        tilesDefinitions.put(name, new Definition(name, BASE_TEMPLATE, attributes));
    }

    /**
     * <code>Add Apache tiles definitions</code>
     */
    public static void addDefinitions() {
        addDefaultLayoutDef("home", "Home", "/WEB-INF/jsp/template/home.jsp");
    }

    @Override
    public Definition getDefinition(String string, Request rqst) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
