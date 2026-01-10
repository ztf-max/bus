package com.qt.bus.model.response;

import com.qt.bus.constants.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

/**
 * @description: 返回类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Boolean isSucceed;

    private Integer code;

    private String msg;

    private Long total;

    private String traceId = MDC.get(CommonConstant.TRACE_ID);

    private T data;

    public static <E> Result<E> success(E result) {
        Result<E> response = new Result<>();
        response.setIsSucceed(true);
        response.setCode(200);
        response.setMsg("操作成功!");
        response.setData(result);

        return response;
    }

    public static <E> Result<E> success(E result, Long total) {
        Result<E> response = new Result<>();
        response.setIsSucceed(true);
        response.setCode(200);
        response.setMsg("操作成功!");
        response.setTotal(total);
        response.setData(result);

        return response;
    }

    public static <E> Result<E> error(String msg) {
        Result<E> response = new Result<>();
        response.setIsSucceed(false);
        response.setCode(500);
        response.setMsg(msg);
        return response;
    }

    public static <E> Result<E> error(Integer code, String msg) {
        Result<E> response = new Result<>();
        response.setIsSucceed(false);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }


}