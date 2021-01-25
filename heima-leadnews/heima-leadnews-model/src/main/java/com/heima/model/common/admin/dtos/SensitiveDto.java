package com.heima.model.common.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author cuichacha
 * @date 1/25/21 11:30
 */
@Data
public class SensitiveDto extends PageRequestDto {
    /**
     * 敏感词名称
     */
    private String name;
}
