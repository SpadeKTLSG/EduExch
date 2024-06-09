package com.shop.common.context;

import com.shop.pojo.dto.EmployeeLocalDTO;

/**
 * 管理员上下文
 *
 * @author SK
 * @date 2024/06/05
 */
public class EmployeeHolder {


    /**
     * 管理员ThreadLocal
     */
    private static final ThreadLocal<EmployeeLocalDTO> employeeTL = new ThreadLocal<>();

    /**
     * 保存用户
     */
    public static void saveEmployee(EmployeeLocalDTO employeeLocalDTO) {
        employeeTL.set(employeeLocalDTO);
    }

    /**
     * 获取用户
     */
    public static EmployeeLocalDTO getEmployee() {
        return employeeTL.get();
    }

    /**
     * 移除用户
     */
    public static void removeEmployee() {
        employeeTL.remove();
    }
}
