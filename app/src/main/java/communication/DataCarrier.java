package communication;

import java.io.Serializable;

public class DataCarrier <T extends Serializable> implements  Serializable {

    private String info;
    private T data;
    private boolean request;
    private static final boolean REQUEST = true;
    private static final boolean RESPONSE = false;

    private DataCarrier(){

    }

    public DataCarrier(boolean request){
        this("noinfo", null, request);
    }

    public DataCarrier(String info, boolean request){
        this(info, null, request);
    }

    public DataCarrier(T data, boolean request){
        this("noinfo", data, request);
    }

    public DataCarrier(String info, T data, boolean request){
        this.info = info;
        this.data = data;
        this.request = request;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }
}
