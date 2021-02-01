package com.heima.apis.article;

import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/31/21 15:53
 */
@Api(value = "文章操作", tags = "ApAuthor", description = "文章操作API")
public interface ApArticleControllerApi {

    /**
     * 保存文章(远程调用)
     *
     * @param apArticle
     * @return
     */
    @ApiOperation(value = "保存文章(远程调用)")
    public ResponseResult saveApArticle(ApArticle apArticle);
}
