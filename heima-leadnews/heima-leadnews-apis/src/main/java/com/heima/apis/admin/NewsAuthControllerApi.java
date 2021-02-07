package com.heima.apis.admin;

import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 2/7/21 15:08
 */
@Api(value = "人工审核自媒体文章", tags = "WmNewsAuthAdmin", description = "人工审核自媒体文章")
public interface NewsAuthControllerApi {
    /**
     * 人工审核自媒体文章列表(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    @ApiOperation("人工审核自媒体文章列表(远程调用)")
    public ResponseResult findNews(NewsAuthDto newsAuthDto);

    /**
     * 人工审核自媒体文章详情(远程调用)
     *
     * @param id
     * @return
     */
    @ApiOperation("人工审核自媒体文章详情(远程调用)")
    public ResponseResult findOne(Integer id);

    /**
     * 人工审核自媒体文章成功(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    @ApiOperation("人工审核自媒体文章成功(远程调用)")
    public ResponseResult authPass(NewsAuthDto newsAuthDto);

    /**
     * 人工审核自媒体文章失败(远程调用)
     *
     * @param newsAuthDto
     * @return
     */
    @ApiOperation("人工审核自媒体文章失败(远程调用)")
    public ResponseResult authFail(NewsAuthDto newsAuthDto);
}
