package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleConfigService extends IService<ApArticleConfig> {
    /**
     * 保存文章设置(远程调用)
     *
     * @param apArticleConfig
     * @return
     */
    ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 更新文章设置(远程调用)
     *
     * @param apArticleConfig
     * @return
     */
    public ResponseResult updateArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 删除文章设置(远程调用)
     *
     * @param id
     * @return
     */
    public ResponseResult deleteArticleConfig(Long id);
}