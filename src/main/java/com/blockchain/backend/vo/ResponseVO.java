package com.blockchain.backend.vo;

/**
 * @author 听取WA声一片
 */
public class ResponseVO {

    private Boolean isSuccess;

    private String message;

    private Object data;

    public static ResponseVO buildSuccess(String message, Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.isSuccess = true;
        responseVO.message = message;
        responseVO.data = data;
        return responseVO;
    }

    public static ResponseVO buildSuccess(String message) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.isSuccess = true;
        responseVO.message = message;
        return responseVO;
    }

    public static ResponseVO buildSuccess(Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.isSuccess = true;
        responseVO.data = data;
        return responseVO;
    }

    public static ResponseVO buildFailure(String message, Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.isSuccess = true;
        responseVO.message = message;
        responseVO.data = data;
        return responseVO;
    }

    public static ResponseVO buildFailure(String message) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.isSuccess = false;
        responseVO.message = message;
        return responseVO;
    }

    public static ResponseVO buildFailure(Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.isSuccess = true;
        responseVO.data = data;
        return responseVO;
    }

}
