package sourceManager;

import java.io.IOException;

public class SourceManagerImplEficiente extends SourceManagerImpl {
    public SourceManagerImplEficiente() {
        super();
    }

    @Override
    public char getNextChar() throws IOException {
        int codePoint;
        char currentChar;

        if(mustReadNextLine) {
            lineNumber++;
            lineIndexNumber = 0;
            mustReadNextLine = false;
        }

        codePoint = reader.read();

        if (codePoint == -1) {
            currentChar = END_OF_FILE;
        } else {
            currentChar = (char) codePoint;

            if (currentChar == '\n') {
                mustReadNextLine = true;
            } else {
                lineIndexNumber++;
            }
        }

        return currentChar;
    }
}