package com.carambla;

import java.io.*;
import java.util.*;

/**
 *
 * Does not detect label templates that are used as a exception (e.g.: throw new errorException("error",code);
 * Does not detect label templates that are generated as a list with a groovy template
 */
public class LabelMessageChecker {

    public final static char sep = File.separatorChar;
    public static String readException;
    public static List<String> unDecectabels;
    public static BufferedReader bufferedReader;
    public static File file;
    public static File homeFolder;
    public static BufferedReader eng;
    public static BufferedReader fra;
    public static BufferedReader nl;
    public static String buffer;
    public static String message;
    public static Set<String> foundEng = new TreeSet<>();
    public static Set<String> foundFra = new TreeSet<>();
    public static Set<String> foundNl = new TreeSet<>();
    public static Set<String> notFoundEng = new TreeSet<>();
    public static Set<String> notFoundFra = new TreeSet<>();
    public static Set<String> notFoundNl = new TreeSet<>();
    public static Set<String> notFoundInTemplateEng = new TreeSet<>();
    public static Set<String> notFoundInTemplateFra = new TreeSet<>();
    public static Set<String> notFoundInTemplateNl = new TreeSet<>();
    public static Set<String> totalEng = new TreeSet<>();
    public static Set<String> totalFra = new TreeSet<>();
    public static Set<String> totalNl = new TreeSet<>();
    public static List<String> filesChecked = new ArrayList<>();
    public static boolean notFound = false;
    public static boolean readLine = true;
    public static boolean readFile = true;

    public static void main(String[] args) throws IOException {

//        System.out.println(System.getProperty("user.dir"));
        file = new File(System.getProperty("user.dir"));

        File config = new File(file.getPath() + sep + "application.conf");
        if(!config.exists()){
            BufferedWriter FW = new BufferedWriter(new FileWriter(config));
            FW.write("#Project folder directory =\r\n");
            FW.write("#label names excluded from search =\r\n");
            FW.write("#labels that are there but are not detected =\r\n");
            FW.flush();

        }
        BufferedReader BR = new BufferedReader(new FileReader(config));
        String line = BR.readLine();
        String projectDirectory = line.substring(line.indexOf("=")+1);
        homeFolder = new File(projectDirectory);
        line = BR.readLine();
        readException = line.substring(line.indexOf("=")+1);
        line = BR.readLine();
        unDecectabels = Arrays.asList(line.substring(line.indexOf("=")+1));

        for (String s : unDecectabels){
//            System.out.println("ignored when not found: " + s);
            foundEng.add(s);
            foundFra.add(s);
            foundNl.add(s);
        }

        getMessagesFiles();
        file = new File(homeFolder.getPath()+ sep + "app");
        searchThroughDirectory(file);
        searchForUnusedLabel();

    }

    public static void searchThroughDirectory (File dir) throws IOException {
        for (File f : dir.listFiles()){
            if (f.isDirectory()){

                searchThroughDirectory(f);


            }else{
//                System.out.println(f.getPath());
                bufferedReader = new BufferedReader(new FileReader(f));
                //read the file till end
                readFile = true;
                while(readFile){
                    try{
                        buffer = bufferedReader.readLine();
                        readLine = true;
                        // read the line until te end
                        while (readLine) {

                            if (buffer.contains("&{\'") || buffer.contains("Messages.get(\"") || buffer.contains("#{errors [\'") || (buffer.contains("#{") && buffer.contains("/}") && (buffer.substring(1).contains("\'")) ) ){
                                int i = 1000000;
                                int j = 1000000;
                                if (buffer.contains("&{\'")) {
                                    i = buffer.indexOf('&') + 3;
                                    j = buffer.indexOf('\'', i);
                                }
                                if (buffer.contains("Messages.get")) {
                                    if (i > (buffer.indexOf("Messages.get") + 14)) {
                                        i = buffer.indexOf("Messages.get") + 14;
                                        j = buffer.indexOf("\"", i);
                                    }


                                }
                                if(buffer.contains("#{errors [\'")){
                                    if(i>(buffer.indexOf("#{errors [\'") + 11)){
                                        i = buffer.indexOf("#{errors [\'") + 11;
                                        buffer = "#{errors [\'" + buffer.substring(i);
                                        i = buffer.indexOf("[\'") + 2;
                                        j = buffer.indexOf("\'",i);
                                        buffer = buffer.substring(0 , j+2) + "#{errors [" + buffer.substring(j + 2);
                                    }

                                }
                                if (buffer.contains("#{") && buffer.contains("/}") && !buffer.contains("#{errors [")){
                                    if(i>(buffer.indexOf("#{"))){
                                        boolean noComma = true;
                                        boolean commaIsBefore = true;
//                                        System.out.println("before: " + buffer);
                                        i = buffer.indexOf("#{");
                                        j = buffer.indexOf("/}", i);
                                        if(buffer.contains(":") && buffer.contains(",")){
                                            commaIsBefore = buffer.indexOf(",") < buffer.indexOf(":");
                                            noComma = false;
                                        }

                                        if(!commaIsBefore || !buffer.contains("\'")){
                                            buffer = "STOP" + buffer.substring(j);
                                            i = 0;
                                            j = 4;
                                        }
                                        else{
                                            if(commaIsBefore || noComma){
                                                i = buffer.indexOf("\'") + 1;
                                                j = buffer.indexOf("\'", i);
//                                                System.out.println(i + " " +j);
                                            }

                                        }
//                                        System.out.println("after: " + buffer);
                                    }


                                }
//                                System.out.println(buffer);
//                                System.out.println(i + " " + j);
                                message = buffer.substring(i, j);
//                                System.out.println(message);

                                if (!readException.contains(message)) {

                                    getMessagesFiles();
                                    try {
                                        while (true) {
                                            if (eng.readLine().contains(message)) {
//                                                System.out.println(message);
                                                foundEng.add(message);
                                                break;
                                            }
                                        }
                                    } catch (Exception m) {
                                        notFoundInTemplateEng.add(message);
//                                        System.out.println("not in eng: " + message);
                                        notFound = true;
                                    }
                                    try {
                                        while (true) {
                                            if (fra.readLine().contains(message)) {
                                                foundFra.add(message);
                                                break;
                                            }
                                        }
                                    } catch (Exception m) {
                                        notFoundInTemplateFra.add(message);
//                                        System.out.println("not in fra: " + message);
                                        notFound = true;
                                    }
                                    try {
                                        while (true) {
                                            if (nl.readLine().contains(message)) {
                                                foundNl.add(message);
                                                break;
                                            }
                                        }
                                    } catch (Exception m) {
                                        notFoundInTemplateNl.add(message);
//                                        System.out.println("not in nl: " + message);
                                        notFound = true;
                                    }
                                }
                                buffer = buffer.substring(j);
                            } else {
                                readLine = false;
                            }

                        }

                    }catch (NullPointerException e){
                        if(notFound){
                            filesChecked.add(f.getPath());
//                            System.out.println("file checked: "+ f.getPath());
//                            System.out.println(e);
                        }
                        notFound = false;
                        readFile = false;
                    }
                }
            }
        }



    }

    public static void searchForUnusedLabel() throws IOException {
        getMessagesFiles();
        String label;
        String subLabel;
        while (true) {
            try {
                label = eng.readLine();
                if (label.contains("=") && !label.contains("#")){
                    subLabel = label.substring(0,label.indexOf("=")).trim();
                    if (!foundEng.contains(subLabel)) {
                        notFoundEng.add(label);
                    }
                    totalEng.add(label);
                }
            } catch (Exception m) {
                break;
            }
        }


        while (true) {
            try {

                label = fra.readLine();
                if (label.contains("=") && !label.contains("#")) {
                    subLabel = label.substring(0,label.indexOf("=")).trim();
                    if (!foundFra.contains(subLabel)) {
                        notFoundFra.add(label);
                    }
                    totalFra.add(label);
                }


            } catch (Exception m) {
                break;
            }
        }
        while (true) {
            try {
                label = nl.readLine();
                if (label.contains("=") && !label.contains("#")) {
                    subLabel = label.substring(0,label.indexOf("=")).trim();
                    if (!foundNl.contains(subLabel)) {
                        notFoundNl.add(label);
                    }
                    totalNl.add(label);
                }


            } catch (Exception m) {
                break;
            }
        }
    }

    public static void getMessagesFiles() throws IOException {
        eng = new BufferedReader(new FileReader(new File(homeFolder.getPath()+ sep + "conf" + sep + "messages")));
        fra = new BufferedReader(new FileReader(new File(homeFolder.getPath()+ sep + "conf" + sep + "messages.fr")));
        nl = new BufferedReader(new FileReader(new File(homeFolder.getPath()+ sep + "conf" + sep + "messages.nl")));

    }
}
