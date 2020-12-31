package com.iproject.crowd.utils;

/**
 *
 * @param <T> 装载消息 Entity 的类型。
 */
public class ResultEntity<T> {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    // 封装当前请求的处理结果。
    private String result;

    // 请求处理失败时返回的错误消息。
    private String message;

    // 要返回的数据。
    private T data;

    public static <T> ResultEntity<T> successWithoutData(){
        return new ResultEntity<>(SUCCESS,null,null);
    }

    public static <T> ResultEntity<T> successWithData(T data){
        return new ResultEntity<>(SUCCESS,null,data);
    }


    public static <T> ResultEntity<T> failure(String msg){
        return new ResultEntity<>(FAILURE,msg,null);
    }

    public ResultEntity(){}

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
