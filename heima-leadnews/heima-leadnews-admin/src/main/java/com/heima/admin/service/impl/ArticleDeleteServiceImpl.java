package com.heima.admin.service.impl;

import com.heima.admin.feign.ArticleFeign;
import com.heima.admin.service.ArticleDeleteService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuichacha
 * @date 2/2/21 14:50
 */
@Service
@Slf4j
public class ArticleDeleteServiceImpl implements ArticleDeleteService {

    @Autowired
    private ArticleFeign articleFeign;

    @Override
    @GlobalTransactional
    public void deleteArticles(Long id) {
        ResponseResult deleteApArticleResult = articleFeign.deleteApArticle(id);
        if (!deleteApArticleResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程调用删除ApArticle对象发生异常");
            throw new RuntimeException("远程调用删除ApArticle对象发生异常");
        }
        ResponseResult deleteArticleContentResult = articleFeign.deleteArticleContent(id);
        if (!deleteArticleContentResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程调用删除ApArticleContent对象发生异常");
            throw new RuntimeException("远程调用删除ApArticleContent对象发生异常");
        }
        ResponseResult deleteArticleConfigResult = articleFeign.deleteArticleConfig(id);
        if (!deleteArticleConfigResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程调用删除ApArticleConfig对象发生异常");
            throw new RuntimeException("远程调用删除ApArticleConfig对象发生异常");
        }
    }
}
