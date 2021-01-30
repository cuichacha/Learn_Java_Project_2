package com.heima.model.common.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

/**
 * @author cuichacha
 * @date 1/30/21 08:45
 */
@Data
public class WmNewsPageDto extends PageRequestDto {
    /**
     * 状态
     */
    private Short status;
    /**
     * 开始时间
     */
    private Date beginPublishDate;
    /**
     *
     */
    private Date endPublishDate;
    /**
     * 所属频道ID
     */
    private Integer channelId;
    /**
     * 关键字
     */
    private String keyword;
}
