package com.heima.apis.article;

import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PutMapping;

@Api(value = "文章设置操作", tags = "ApAuthor", description = "文章设置操作API")
public interface ApArticleConfigControllerApi {

    /**
     * 保存文章设置(远程调用)
     * @param apArticleConfig
     * @return
     */
    @ApiOperation(value = "保存文章设置(远程调用)")
    ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 更新文章设置(远程调用)
     *
     * @param apArticleConfig
     * @return
     */
    @ApiOperation(value = "更新文章设置(远程调用)")
    public ResponseResult updateArticleConfig(ApArticleConfig apArticleConfig);
}