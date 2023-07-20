package ey.xmlsorter;

import java.io.IOException;
import java.nio.file.*;

public class RawFileDeleter {

    public static void handleDeletion(String fileName) {
        Path path = Paths.get(fileName);

        try {
            Files.delete(path);
            System.out.println("File deleted successfully");
        } catch (NoSuchFileException e) {
            System.out.println("No such file/directory exists");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        } catch (IOException e) {
            System.out.println("Invalid permissions.");
        }
    }
}
