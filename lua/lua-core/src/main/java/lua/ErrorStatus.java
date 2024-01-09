package lua;

public class ErrorStatus {
    private ErrorStatus() {}

    static ErrorStatus errorStatus = new ErrorStatus();
    public static ErrorStatus get() { errorStatus.reset(); return errorStatus; }

    int code;
    String error;

    public boolean isValid() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    void reset() {
        code = 0;
        error = "";
    }
}
