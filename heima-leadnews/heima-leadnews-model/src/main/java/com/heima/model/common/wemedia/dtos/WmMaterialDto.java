package com.heima.model.common.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author cuichacha
 * @date 1/29/21 21:10
 */
@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 1 查询收藏的
     */
    private Short isCollected;
}
