package utils.messages;

public class LexicalConsoleMessage {
    private String errorMessage;
    private String successMessage;

    public LexicalConsoleMessage() {
        errorMessage = "";
        successMessage = "";
    }

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() { return successMessage + "\n" + "[SinErrores]"; }

    public void appendErrorMessage(String errorMessage) {
        this.errorMessage = this.errorMessage + errorMessage + '\n';
    }
    public void appendSuccessMessage(String successMessage) {
        this.successMessage = this.successMessage + successMessage + '\n';
    }
}
