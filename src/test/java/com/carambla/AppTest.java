package com.carambla;



import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class AppTest{


    @Test
    public void testapp() throws IOException {
        Configuration configuration = new Configuration();
        configuration.homeFolder = new File("src/test/resources/testPlayOneProject");

        LabelMessageChecker checker = new LabelMessageChecker();
        LabelMessageChecker.Result result = checker.run(configuration);
        assertEquals(5, result.totalEng.size());
        assertEquals(1, result.notFoundEng.size());
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
}
