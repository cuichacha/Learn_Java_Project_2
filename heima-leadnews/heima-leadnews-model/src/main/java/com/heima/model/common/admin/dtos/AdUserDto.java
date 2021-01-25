package com.heima.model.common.admin.dtos;

import lombok.Data;

/**
 * @author cuichacha
 * @date 1/25/21 16:11
 */
@Data
public class AdUserDto {
    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;
}
