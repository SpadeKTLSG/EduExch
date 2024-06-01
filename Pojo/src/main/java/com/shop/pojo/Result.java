package com.shop.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回结果
 *
 * @author SK
 * @date 2024/06/01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean success;
    private String errorMsg;
    private Object data;
    private Long total;

    /**
     * 返回成功结果
     *
     * @return {@link Result }
     */
    public static Result success() {
        return new Result(true, null, null, null);
    }

    /**
     * 返回成功结果
     *
     * @param data 数据
     * @return {@link Result }
     */
    public static Result success(Object data) {
        return new Result(true, null, data, null);
    }

    /**
     * 返回成功结果
     *
     * @param data  数据
     * @param total 总数
     * @return {@link Result }
     */
    public static Result success(List<?> data, Long total) {
        return new Result(true, null, data, total);
    }

    /**
     * 返回失败结果
     *
     * @param errorMsg 错误信息
     * @return {@link Result }
     */
    public static Result error(String errorMsg) {
        return new Result(false, errorMsg, null, null);
    }
}
