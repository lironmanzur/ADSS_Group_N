package Backend.ServiceLayer;


public class Response {
    public String errorMessage;


    public Response() {
    }
    public Response(String msg)
    {
        this.errorMessage = msg;
    }

    public boolean errorOccurred() {
        return errorMessage!=null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return "Response [errorMessage=" + errorMessage + "]";
    }
}


