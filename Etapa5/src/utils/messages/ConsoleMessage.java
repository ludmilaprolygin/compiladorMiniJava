package utils.messages;

public abstract class ConsoleMessage {
    private String errorMessage;
    private String successMessage;

    public ConsoleMessage() {
        errorMessage = "";
        successMessage = "";
    }

    public void appendErrorMessage(String errorMessage) {
        this.errorMessage = this.errorMessage + errorMessage + '\n';
    }
    public void appendSuccessMessage(String successMessage) {
        this.successMessage = this.successMessage + successMessage + '\n';
    }

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() {
        if (errorMessage.isEmpty()) {
            appendSuccessMessage("\n" + "[SinErrores]");
        }
        return successMessage;
    }
}
