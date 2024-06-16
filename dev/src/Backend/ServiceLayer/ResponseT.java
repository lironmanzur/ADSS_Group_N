package Backend.ServiceLayer;

public class ResponseT<T> extends Response{

    public T returnValue;
    public ResponseT(String msg,boolean errorOccurred)
    {
        super(msg);
        this.returnValue = null;
    }

    public ResponseT(T value)
    {
        super(null);
        this.returnValue = value;
    }

    public T getValue() {
        return returnValue;
    }
    public void setValue(T value) {
        this.returnValue = value;
    }

    public String toString() {
        return "BaseResponse [errorMessage=" + errorMessage + "]";
    }

}

