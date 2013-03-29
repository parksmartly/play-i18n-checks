package com.carambla;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

public class AppTest extends TestCase{


    public void testapp() throws IOException {
        LabelMessageChecker lmc = new LabelMessageChecker();
        lmc.run();
        System.out.println(lmc.homeFolder);
        assertTrue(lmc.totalEng.size() == 5);
        assertTrue(lmc.notFoundEng.size() == 0);
        assertTrue(lmc.notFoundInTemplateEng.size() == 0);
        assertTrue(lmc.foundEng.size() == 4);

        assertTrue(lmc.totalFra.size() == 4);
        assertTrue(lmc.notFoundFra.size() == 0);
        assertTrue(lmc.notFoundInTemplateFra.size() == 1);
        assertTrue(lmc.foundFra.size() == 3);

        assertTrue(lmc.totalNl.size() == 4);
        assertTrue(lmc.notFoundNl.size() == 1);
        assertTrue(lmc.notFoundInTemplateNl.size() == 2);
        assertTrue(lmc.foundNl.size() == 2);


    }
}
