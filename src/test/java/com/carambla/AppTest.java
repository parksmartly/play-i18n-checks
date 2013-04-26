package com.carambla;


import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest{
    private static final char SEPARATOR = File.separatorChar;


    @Test
    public void testapp() throws IOException {
        Configuration configuration = new Configuration();
        configuration.homeFolder = new File("src/test/resources/testPlayOneProject");

        LabelMessageChecker checker = new LabelMessageChecker();
        LabelMessageChecker.Result result = checker.run(configuration);
        assertEquals(7, result.totalEng.size());
        assertEquals(3, result.notFoundEng.size());
        assertEquals(1, result.notFoundInTemplateEng.size());
        assertEquals(4, result.foundEng.size());

        assertEquals(4, result.totalFra.size());
        assertEquals(0, result.notFoundFra.size());
        assertEquals(1, result.notFoundInTemplateFra.size());
        assertEquals(4, result.foundFra.size());

        assertEquals(4, result.totalNl.size());
        assertEquals(1, result.notFoundNl.size());
        assertEquals(2, result.notFoundInTemplateNl.size());
        assertEquals(3, result.foundNl.size());
    }
    @Test
    public void testMessagesTranslateToPlay2() throws IOException{
        Configuration configuration = new Configuration();
        configuration.homeFolder = new File("src" + SEPARATOR + "test" + SEPARATOR + "resources" + SEPARATOR + "testPlayOneProject");

        LabelMessageChecker checker = new LabelMessageChecker();
        Map<String,File> map = checker.translateMessages(configuration);

        BufferedReader bf = new BufferedReader(new FileReader(new File(configuration.homeFolder.getPath()+ SEPARATOR + "conf" + SEPARATOR + "messages_play2")));
        assertTrue(map.containsKey("messages.translated"));
        File file = map.get("messages.translated");
        BufferedReader bf2 = new BufferedReader(new FileReader(file));
        String line = bf.readLine();
        while(line != null){
            assertTrue(line.equals(bf2.readLine()));
            line = bf.readLine();
        }
    }
    @Test
    public void testRouteToPlay2() throws IOException{
        Configuration configuration = new Configuration();
        configuration.homeFolder = new File("src" + SEPARATOR + "test" + SEPARATOR + "resources" + SEPARATOR + "testPlayOneProject");

        LabelMessageChecker checker = new LabelMessageChecker();
        File file = checker.translateRoute(configuration);

        BufferedReader bf = new BufferedReader(new FileReader(new File(configuration.homeFolder.getPath()+ SEPARATOR + "conf" + SEPARATOR + "routes_play2")));
        BufferedReader bf2 = new BufferedReader(new FileReader(file));
        String line = bf.readLine();
        while(line != null){
            assertTrue(line.equals(bf2.readLine()));
            line = bf.readLine();
        }
    }
}
