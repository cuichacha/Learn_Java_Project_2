package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.vo.WmNewsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cuichacha
 * @date 1/30/21 09:46
 */
@Mapper
public interface WmNewsMapper extends BaseMapper<WmNews> {

    /**
     * 查询包含作者名称的文章列表集合(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    public abstract List<WmNewsVo> findWmNewsList(@Param("newsAuthDto") NewsAuthDto newsAuthDto);

    /**
     * 查询包含作者名称的文章列表集合数量(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    public abstract Integer findWmNewsListCount(@Param("newsAuthDto") NewsAuthDto newsAuthDto);
}
