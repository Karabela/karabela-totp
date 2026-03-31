package org.karabela.totp;

import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtils {

    public static void writeFile(byte[] contents, String filePath) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(filePath)) {
            stream.write(contents);
        }
    }
}
