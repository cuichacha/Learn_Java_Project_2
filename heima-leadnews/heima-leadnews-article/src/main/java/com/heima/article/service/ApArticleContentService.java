package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiOperation;

public interface ApArticleContentService extends IService<ApArticleContent> {
    /**
     * 保存文章内容(远程调用)
     * @param apArticleContent
     * @return
     */
    ResponseResult saveArticleContent(ApArticleContent apArticleContent);

    /**
     * 更新文章内容(远程调用)
     *
     * @param apArticleContent
     * @return
     */
    public ResponseResult updateArticleContent(ApArticleContent apArticleContent);
}