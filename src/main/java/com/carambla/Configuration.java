package com.carambla;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Configuration {
    public File homeFolder;

    /**
        labels or false positives that should not be recorded
     */
    public String readException;

    /**
    labels that are not found by the checker but are used
    if those labels are not in the template files, then they will be reported missing(notFoundInTemplate)
     */
    public List<String> unDetectables ;

    Configuration() {
        this.readException = "";
        this.unDetectables = new ArrayList<>();
    }
}
