package util;

import java.io.*;

/**
 * @author lurenjia
 * @date 2018/10/30
 */
public class FileUtil {

    public static String readToBuffer(String filePath) throws IOException {
        StringBuffer buffer = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        while (line != null) {
            buffer.append(line);
            buffer.append("\n");
            line = reader.readLine();
        }
        reader.close();
        is.close();
        return buffer.toString();
    }
}
