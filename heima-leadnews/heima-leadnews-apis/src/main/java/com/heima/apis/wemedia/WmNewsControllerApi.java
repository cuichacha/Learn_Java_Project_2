package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.model.common.wemedia.pojos.WmNews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/30/21 08:49
 */
@Api(value = "自媒体文章", tags = "WmNews", description = "自媒体文章管理API")
public interface WmNewsControllerApi {

    /**
     * 查询自媒体文章列表
     *
     * @param wmNewsPageDto
     * @return
     */
    @ApiOperation(value = "自媒体文章列表")
    public abstract ResponseResult wmNewsList(WmNewsPageDto wmNewsPageDto);

    /**
     * 发布、修改、保存草稿自媒体文章
     *
     * @param wmNewsDto
     * @return
     */
    @ApiOperation(value = "自媒体文章发布、修改、保存草稿")
    public abstract ResponseResult updateNews(WmNewsDto wmNewsDto);

    /**
     * 更新文章状态(远程调用)
     *
     * @param wmNews
     * @return
     */
    @ApiOperation(value = "自媒体文章修改(远程调用)")
    public abstract ResponseResult updateWmNews(WmNews wmNews);

    /**
     * 根据Id查询自媒体文章
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "自媒体文章查询")
    public abstract ResponseResult findNewsById(Integer id);

    /**
     * 根据Id查询自媒体文章(远程调用)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "自媒体文章查询(远程调用)")
    public abstract ResponseResult findById(Integer id);

    /**
     * 删除自媒体文章
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "自媒体文章删除")
    public abstract ResponseResult deleteNews(Integer id);

    /**
     * 上架或下架自媒体文章
     *
     * @param wmNewsDto
     * @return
     */
    @ApiOperation(value = "自媒体文章上架或下架")
    public abstract ResponseResult publishOrWithdrawNews(WmNewsDto wmNewsDto);
}
