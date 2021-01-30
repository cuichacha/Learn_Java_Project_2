package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.model.common.wemedia.pojos.WmNews;

/**
 * @author cuichacha
 * @date 1/30/21 09:46
 */
public interface WmNewsService extends IService<WmNews> {
    /**
     * 查询自媒体文章列表
     * @param wmNewsPageDto
     * @return
     */
    public abstract ResponseResult WmNewsList(WmNewsPageDto wmNewsPageDto);

    /**
     * 发布、修改、保存草稿自媒体文章
     * @param wmNewsDto
     * @param saveType
     * @return
     */
    public abstract ResponseResult updateNews(WmNewsDto wmNewsDto, Short saveType);

    /**
     * 根据Id查询自媒体文章
     * @param id
     * @return
     */
    public abstract ResponseResult findNewsById(Integer id);

    /**
     * 删除自媒体文章
     * @param id
     * @return
     */
    public abstract ResponseResult deleteNews(Integer id);

    /**
     * 上架或下架自媒体文章
     * @param wmNewsDto
     * @return
     */
    public abstract ResponseResult publishOrWithdrawNews(WmNewsDto wmNewsDto);
}
