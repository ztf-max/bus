package com.qt.bus.config;

import lombok.Data;

@Data
public final class ApiResponse<T> {

  private Integer code;
  private String errorMsg;
  private T data;

  private ApiResponse(int code, String errorMsg, T data) {
    this.code = code;
    this.errorMsg = errorMsg;
    this.data = data;
  }
  
  public static <T> ApiResponse<T> ok() {
    return new ApiResponse<T>(0, "", null);
  }

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<T>(0, "", data);
  }

  public static <T> ApiResponse<T> error(String errorMsg) {
    return new ApiResponse<T>(0, errorMsg, null);
  }
}
