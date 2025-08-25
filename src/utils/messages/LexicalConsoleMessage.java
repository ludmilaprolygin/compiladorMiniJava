package utils.messages;

import java.util.Objects;

public class LexicalConsoleMessage {
    private String errorMessage;
    private String successMessage;

    public LexicalConsoleMessage() {
        errorMessage = "";
        successMessage = "";
    }

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() {
        if (errorMessage.isEmpty()) {
            appendSuccessMessage("\n" + "[SinErrores]");
        }
        return successMessage;
    }

    public void appendErrorMessage(String errorMessage) {
        this.errorMessage = this.errorMessage + errorMessage + '\n';
    }
    public void appendSuccessMessage(String successMessage) {
        this.successMessage = this.successMessage + successMessage + '\n';
    }
}
