package com.heima.model.common.wemedia.vo;

import com.heima.model.common.wemedia.pojos.WmNews;
import lombok.Data;

@Data
public class WmNewsVo extends WmNews {

    /**
     * 文章作者名称
     */
    private String authorName;
}
