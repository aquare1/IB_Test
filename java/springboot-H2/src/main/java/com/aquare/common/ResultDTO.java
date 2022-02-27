package com.aquare.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author dengtao aquare@163.com
 * */
@ApiModel("ResultDTO")
@Log4j2
public class ResultDTO <Data extends Object> {

    @ApiModelProperty("code 0:failure,success")
    private Integer code;

    @ApiModelProperty("msg")
    private String msg;

    @ApiModelProperty("data")
    private Data data;

    public static ResultDTO success() {
        ResultDTO result = new ResultDTO();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static <Data> ResultDTO<Data> success(Data data) {
        ResultDTO<Data> result =  new ResultDTO<Data>();
        result.setResultCode(ResultCode.SUCCESS);
        result.data = data;
        return result;
    }

    public static ResultDTO failure() {
        ResultDTO result = new ResultDTO();
        result.setCode(ResultCode.FAILURE.code());
        return result;
    }

    public static ResultDTO failure(Exception e) {
        ResultDTO result = new ResultDTO();
        result.setCode(ResultCode.FAILURE.code());
        String errorStr = ResultDTO.getErrer(e);
        result.setData(errorStr);
        log.error(errorStr);
        return result;
    }
    public static ResultDTO failure(ResultCode code) {
        ResultDTO result = new ResultDTO();
        result.setCode(code.code());
        result.setMsg(code.message());
        return result;
    }

    public static ResultDTO failure(ResultCode code, Object data) {
        ResultDTO result = new ResultDTO();
        result.setCode(code.code());
        result.setMsg(code.message());

        result.setData(data);
        return result;
    }

    public static ResultDTO failure(ResultCode code, Exception e) {
        ResultDTO result = new ResultDTO();
        result.setCode(code.code());
        result.setMsg(code.message());
        String errorStr = ResultDTO.getErrer(e);
        result.setData(errorStr);
        log.error(errorStr);
        return result;
    }

    public static ResultDTO failure(ResultCode code, String msg, Exception e) {
        ResultDTO result = new ResultDTO();
        result.setCode(code.code());
        result.setMsg(msg);
        String errorStr = ResultDTO.getErrer(e);
        result.setData(errorStr);
        log.error(errorStr);
        return result;
    }

    public static String getErrer(Exception e){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    public void setResultCode(ResultCode code){
        this.code = code.code();
        this.msg = code.message();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
