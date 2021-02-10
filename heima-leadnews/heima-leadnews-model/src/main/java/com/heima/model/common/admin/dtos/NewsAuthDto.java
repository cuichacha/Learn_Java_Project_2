package com.heima.model.common.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewsAuthDto extends PageRequestDto {

    /**
     * 标题
     */
    private String title;

    private Integer id;

    /**
     * 审核失败原因
     */
    private String msg;

}
