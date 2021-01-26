package com.heima.model.common.user.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author cuichacha
 * @date 1/26/21 16:38
 */
@Data
public class AuthDto extends PageRequestDto {

    /**
     * id
     */
    private Integer id;
    /**
     * 驳回的信息
     */
    private String msg;
    /**
     * 状态
     */
    private Short status;
}
