package com.heima.model.common.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author cuichacha
 * @date 1/23/21 20:18
 */
@Data
public class ChannelDto extends PageRequestDto {
    /**
     * 频道名称
     */
    private String name;
}
