package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/27/21 08:57
 */
public interface ArticleService extends IService<ApAuthor> {

    public abstract ResponseResult saveAuthor(ApAuthor apAuthor);
}
