package com.heima.apis.article;

import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PutMapping;

@Api(value = "文章内容操作", tags = "ApAuthor", description = "文章内容操作API")
public interface ApArticleContentControllerApi {

    /**
     * 保存文章内容(远程调用)
     * @param apArticleContent
     * @return
     */
    @ApiOperation(value = "保存文章内容(远程调用)")
    ResponseResult saveArticleContent(ApArticleContent apArticleContent);

    /**
     * 更新文章内容(远程调用)
     *
     * @param apArticleContent
     * @return
     */
    @ApiOperation(value = "更新文章内容(远程调用)")
    public ResponseResult updateArticleContent(ApArticleContent apArticleContent);
}