package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/31/21 15:50
 */
public interface ApArticleService extends IService<ApArticle> {

    /**
     * 根据文章名称和作者id查询文章(远程调用)
     *
     * @param title
     * @param authorId
     * @return
     */
    public ResponseResult findArticleByNameAndAuthorId(String title, Integer authorId);
}
