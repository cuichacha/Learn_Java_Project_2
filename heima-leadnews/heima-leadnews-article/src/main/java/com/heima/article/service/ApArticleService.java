package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/31/21 15:50
 */
public interface ApArticleService extends IService<ApArticle> {

    /**
     * 保存文章(远程调用)
     *
     * @param apArticle
     * @return
     */
    public ResponseResult saveApArticle(ApArticle apArticle);

    /**
     * 更新文章(远程调用)
     *
     * @param apArticle
     * @return
     */
    public ResponseResult updateApArticle(ApArticle apArticle);
}
