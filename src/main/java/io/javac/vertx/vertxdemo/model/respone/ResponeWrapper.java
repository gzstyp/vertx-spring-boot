package io.javac.vertx.vertxdemo.model.respone;

public class ResponeWrapper<V> {

    private int code;
    private V data;
    private String message;

    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public V getData(){
        return data;
    }

    public void setData(V data){
        this.data = data;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public ResponeWrapper(final int code,final V data,final String message){
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static final ResponeWrapper<String> RESPONE_SUCCESS = new ResponeWrapper<>(0, null, "操作成功");
    public static final ResponeWrapper<String> RESPONE_FAIL = new ResponeWrapper<>(10001, null, "操作失败");
}