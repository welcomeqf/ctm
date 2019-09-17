package com.yq.data;

import com.yq.constanct.CodeType;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回的结果封装
 * @Author qf
 * @Date 2019/8/20
 * @Version 1.0
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 结果描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 跳转链接
     */
//    private String href;

    public Result(){
    }

    public Result(int code, String msg) {
        this(code,msg,null);
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

//    public Result(int code, String msg, T data, String href){
//        this.code = code;
//        this.msg = msg;
//        this.data = data;
//        this.href = href;
//    }

    public Result(CodeType codeType){
        this(codeType.getCode(),codeType.getMsg(),null);
    }



    /**
     * 返回结果操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> SUCCESS(T data){
        Result<T> result = new Result<>();
        result.setCode(CodeType.SUCCESS.getCode());
        result.setData(data);
        result.setMsg(CodeType.SUCCESS.getMsg());
        return result;
    }


    /**
     * 错误返回
     * @param codeType
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(CodeType codeType) {
        Result<T> result = new Result<>();
        result.setCode(codeType.getCode());
        result.setMsg(codeType.getMsg());
        result.setData(null);
        return result;
    }

    /**
     * 没有数据，只返回链接
     * @param href
     * @param <T>
     * @return
     */
//    public static <T> Result<T> HREF(String href) {
//        Result<T> result = new Result<>();
//        result.setCode(CodeType.SUCCESS.getCode());
//        result.setMsg(CodeType.SUCCESS.getMsg());
//        result.setData(null);
//        result.setHref(href);
//        return result;
//    }

    /**
     * 既有数据，又有链接
     * @param href
     * @param data
     * @param <T>
     * @return
     */
//    public static <T> Result<T> DATA_HREF(String href, T data) {
//        Result<T> result = new Result<>();
//        result.setCode(CodeType.SUCCESS.getCode());
//        result.setMsg(CodeType.SUCCESS.getMsg());
//        result.setData(data);
//        result.setHref(href);
//        return result;
//    }
}
