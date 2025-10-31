package outputManager;

import java.io.FileWriter;
import java.io.IOException;

public class OutputManager {
    final FileWriter file_writer;

    public OutputManager(String file_name) {
        try {
            file_writer = new FileWriter(file_name);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void gen(String line) {
        try {
            file_writer.write(line);
            file_writer.write("\n");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void close() {
        try {
            file_writer.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}