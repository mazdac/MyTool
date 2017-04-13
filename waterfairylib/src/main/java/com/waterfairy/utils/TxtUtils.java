package com.waterfairy.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by water_fairy on 2017/4/13.
 */

public class TxtUtils {

    public static void writeTxt(File file, String txt) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(txt);
        fileWriter.close();
    }
}
