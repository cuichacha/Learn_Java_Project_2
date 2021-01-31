package com.heima.apis.article;

import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "文章设置操作", tags = "ApAuthor", description = "文章设置操作API")
public interface ApArticleConfigControllerApi {

    /**
     * 保存文章设置(远程调用)
     * @param apArticleConfig
     * @return
     */
    @ApiOperation(value = "保存文章设置(远程调用)")
    ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);
}