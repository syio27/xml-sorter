package ey.xmlsorter;

import java.io.*;
import java.time.Instant;

public class WhiteSpaceFormatter {
    public static void handleWhiteSpaceFormatting(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter("sorted_file.xml"));

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                writer.write(line);
                writer.newLine();
            }
        }

        reader.close();
        writer.close();
    }
}
