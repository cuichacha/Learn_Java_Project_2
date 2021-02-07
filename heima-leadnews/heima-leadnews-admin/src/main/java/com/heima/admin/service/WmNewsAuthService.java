package com.heima.admin.service;

import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 2/7/21 17:49
 */
public interface WmNewsAuthService {

    /**
     * 人工审核文章列表(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    public abstract ResponseResult findNews(NewsAuthDto newsAuthDto);

    /**
     * 人工肾很文章详情(远程调用)
     *
     * @param id
     * @return
     */
    public abstract ResponseResult findOne(Integer id);

    /**
     * 人工审核通过/拒绝(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    public abstract ResponseResult updateNewsStatus(NewsAuthDto newsAuthDto, Short status);
}
