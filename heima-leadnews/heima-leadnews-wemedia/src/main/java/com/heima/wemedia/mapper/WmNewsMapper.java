package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.vo.WmNewsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cuichacha
 * @date 1/30/21 09:46
 */
@Mapper
public interface WmNewsMapper extends BaseMapper<WmNews> {

    /**
     * 查询包含作者名称的文章列表集合
     * @return
     */
    public abstract List<WmNewsVo> findList(NewsAuthDto newsAuthDto);
}
