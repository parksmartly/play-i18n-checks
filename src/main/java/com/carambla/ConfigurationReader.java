package com.carambla;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ConfigurationReader {

    public void writeDefaultConfigurationIfMissing(File config) throws IOException {
        if(!config.exists()){
            // TODO proper stream closing
            BufferedWriter writer = new BufferedWriter(new FileWriter(config));
            writer.write("#Project folder directory =\r\n");
            writer.write("#label names excluded from search =\r\n");
            writer.write("#labels that are there but are not detected =\r\n");
            writer.flush();
            writer.close();
        }
    }

    public Configuration read(File config) throws IOException {
        Configuration configuration = new Configuration();
        // FIXME proper stream handling!
        BufferedReader BR = new BufferedReader(new FileReader(config));
        String line = BR.readLine();
        String projectDirectory = line.substring(line.indexOf("=") + 1);
        configuration.homeFolder = new File(projectDirectory);
        line = BR.readLine();
        line = line.substring(line.indexOf("=") + 1);
        configuration.readException = line;
        line = BR.readLine();
        line = line.substring(line.indexOf("=") + 1);
        configuration.unDetectables = line.isEmpty()? new ArrayList<String>() : Arrays.asList(line);

        return configuration;
    }
}
