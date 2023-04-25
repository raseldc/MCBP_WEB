/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperToFIleCreate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author rasel
 */
public class BseFileCreate {

    /**
     * @param args the command line arguments
     */
    public static int getStringVal(String s) {
        for (int i = 0; i < s.length(); i++) {
            int a = s.charAt(i) - 'a';
            int b = a;
        }
        return 0;
    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public static class node_ {

        int a;
        public node_ next;

        public node_(int a, node_ next) {
            this.a = a;
            this.next = next;
        }

    }

    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {

        List classes = new ArrayList();

        if (!directory.exists()) {

            return classes;

        }

        File[] files = directory.listFiles();

        for (File file : files) {

            if (file.isDirectory()) {

                assert !file.getName().contains(".");

                classes.addAll(findClasses(file, packageName + "." + file.getName()));

            } else if (file.getName().endsWith(".class")) {

                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));

            }

        }

        return classes;

    }

    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        assert classLoader != null;

        String path = packageName.replace('.', '/');

        Enumeration resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<File>();

        while (resources.hasMoreElements()) {

            URL resource = (URL) resources.nextElement();

            dirs.add(new File(resource.getFile()));

        }

        ArrayList classes = new ArrayList();

        for (File directory : dirs) {

            classes.addAll(findClasses(directory, packageName));

        }

        return (Class[]) classes.toArray(new Class[classes.size()]);

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Class[] c = getClasses("HelperToFIleCreate.baseFile");

        for (int i = 0; i < c.length; i++) {
            Class<?> clazz = Class.forName(c[i].getName());
            Object pasreObj = clazz.newInstance();

            String canonicalPath = new File(".").getCanonicalPath();

            String classNmae = pasreObj.getClass().getSimpleName().substring(0, 1).toUpperCase() + pasreObj.getClass().getSimpleName().substring(1);

            FileWrite.fileWrite(canonicalPath + "\\src\\main\\java\\HelperToFIleCreate\\Parse\\converter\\", converter(pasreObj), classNmae + "Converter.java");
            FileWrite.fileWrite(canonicalPath + "\\src\\main\\java\\HelperToFIleCreate\\Parse\\detail\\", deail(pasreObj), classNmae + "Detail.java");
            FileWrite.fileWrite(canonicalPath + "\\src\\main\\java\\HelperToFIleCreate\\Parse\\service\\", service(pasreObj), classNmae + "Service.java");
            FileWrite.fileWrite(canonicalPath + "\\src\\main\\java\\HelperToFIleCreate\\Parse\\serviceImpl\\", serviceImpl(pasreObj), classNmae + "ServiceImpl.java");
            FileWrite.fileWrite(canonicalPath + "\\src\\main\\java\\HelperToFIleCreate\\Parse\\dao\\", dao(pasreObj), classNmae + "Dao.java");
            FileWrite.fileWrite(canonicalPath + "\\src\\main\\java\\HelperToFIleCreate\\Parse\\daoImp\\", daoImpl(pasreObj), classNmae + "DaoImpl.java");

        }
        
        // TODO code application logic here
    }

    public static <E> String converter(E dbsRole) {
        //testCalss dbsRole = new TestCalss();
        Field[] fields = dbsRole.getClass().getDeclaredFields();

        String converterClass = "public class %1$sConverter { "
                + " public static %1$s getEntity(%1$sDetail detail) \n"
                + "{\n"
                + " if (detail == null) {\n"
                + "            return null;\n"
                + "        }\n"
                + "        %1$s entity = new %1$s();\n";
        String entity = "";
        String detail = "";

        for (Field field : fields) {
            String name = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            entity = entity + " entity.set" + name + "(detail.get" + name + "());\n";
            detail = detail + " detail.set" + name + "(entity.get" + name + "());\n";
           
        }
        converterClass = converterClass + entity;
        converterClass = converterClass + "\n return entity;}\n"
                + "\n"
                + "    public static %1$sDetail  getDetail(%1$s entity) \n"
                + "{\n"
                + "if (entity == null) {\n"
                + "            return null;\n"
                + "        }\n"
                + "        %1$sDetail detail = new %1$sDetail();\n"
                + detail + " \n return detail;}\n }";

        String classNmae = dbsRole.getClass().getSimpleName().substring(0, 1).toUpperCase() + dbsRole.getClass().getSimpleName().substring(1);
        converterClass = String.format(converterClass, classNmae);
        return converterClass;

    }

    public static <E> String deail(E dbsRole) {
        //testCalss dbsRole = new TestCalss();
        Field[] fields = dbsRole.getClass().getDeclaredFields();

        String detailClass = "public class %1$sDetail { \n ";

        String name = "";
        String getset = "";
        for (Field field : fields) {

            String fileName = field.getName();
            String getName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            String dataType = field.getType().getSimpleName();
            String setName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            name = name + " private  " + dataType + " " + field.getName() + ";\n";
            getset = getset + " public " + dataType + " " + getName + "() {\n"
                    + "        return " + fileName + ";\n"
                    + "    }\n"
                    + "public void " + setName + "(" + dataType + " " + fileName + ") {\n"
                    + "        this." + fileName + " = " + fileName + ";\n"
                    + "    }\n";

        }
        detailClass = detailClass + " " + name + " " + getset + "}";
        String classNmae = dbsRole.getClass().getSimpleName().substring(0, 1).toUpperCase() + dbsRole.getClass().getSimpleName().substring(1);
        detailClass = String.format(detailClass, classNmae);
        return detailClass;

    }

    public static <E> String service(E dbsRole) {
        //testCalss dbsRole = new TestCalss();
        Field[] fields = dbsRole.getClass().getDeclaredFields();

        String serviceCLass = "public interface %1$sService extends GenericService<%1$s, Integer> { }\n ";
        String classNmae = dbsRole.getClass().getSimpleName().substring(0, 1).toUpperCase() + dbsRole.getClass().getSimpleName().substring(1);
        serviceCLass = String.format(serviceCLass, classNmae);
        return serviceCLass;

    }

    public static <E> String serviceImpl(E dbsRole) {
        //testCalss dbsRole = new TestCalss();
        Field[] fields = dbsRole.getClass().getDeclaredFields();

        String serviceCLass = "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.beans.factory.annotation.Qualifier;\n"
                + "import org.springframework.stereotype.Service;\n"
                + "@Service\n"
                + "public class %1$sServiceImpl extends GenericServiceImpl<%1$s, Integer>\n"
                + "        implements %1$sService {\n"
                + "private %1$sDao %1$sDao;\n"
                + "\n"
                + "    @Autowired\n"
                + "    public %1$sServiceImpl(\n"
                + "            @Qualifier(\"%1$sDaoImpl\") GenericDao<%1$s, Integer> genericDao) {\n"
                + "\n"
                + "        super(genericDao);\n"
                + "        this.%1$sDao = (%1$sDao) genericDao;\n"
                + "\n"
                + "    }\n"
                + "}";
        String classNmae = dbsRole.getClass().getSimpleName().substring(0, 1).toUpperCase() + dbsRole.getClass().getSimpleName().substring(1);
        serviceCLass = String.format(serviceCLass, classNmae);
        return serviceCLass;

    }

    public static <E> String dao(E dbsRole) {
        //testCalss dbsRole = new TestCalss();
        Field[] fields = dbsRole.getClass().getDeclaredFields();

        String serviceCLass = "public interface %1$sDao extends GenericDao<%1$s, Integer> { }\n ";
        String classNmae = dbsRole.getClass().getSimpleName().substring(0, 1).toUpperCase() + dbsRole.getClass().getSimpleName().substring(1);
        serviceCLass = String.format(serviceCLass, classNmae);
        return serviceCLass;

    }

    public static <E> String daoImpl(E dbsRole) {
        //testCalss dbsRole = new TestCalss();

        String serviceCLass = "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.beans.factory.annotation.Qualifier;\n"
                + "import org.springframework.stereotype.Repository;\n"
                + "@Repository\n"
                + "public class %1$sDaoImpl extends GenericDaoImpl<%1$s, Integer>\n"
                + "        implements %1$sDao {\n"
                + "}";
        String classNmae = dbsRole.getClass().getSimpleName().substring(0, 1).toUpperCase() + dbsRole.getClass().getSimpleName().substring(1);
        serviceCLass = String.format(serviceCLass, classNmae);
        return serviceCLass;

    }

    public static <E> String htmlFileCreation(E htmlFile) {

        String template = "<%@page contentType=\"text/html\" pageEncoding=\"UTF-8\"%>\n"
                + "<%@taglib uri=\"http://www.springframework.org/tags/form\" prefix=\"sf\"%>\n"
                + "<%@taglib uri=\"http://tiles.apache.org/tags-tiles\" prefix=\"tiles\" %>\n"
                + "<%@taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\"%>\n"
                + "<%@taglib uri=\"http://www.springframework.org/tags\" prefix=\"spring\"%>\n"
                + "<tiles:insertDefinition name=\"DefaultTemplate\">\n"
                + "    <tiles:putAttribute name=\"body\">\n"
                + "        $body\n"
                + "        \n"
                + "\n"
                + "    </tiles:putAttribute>\n"
                + "</tiles:insertDefinition>\n"
                + "        ";
        
        return "";
    }
}
