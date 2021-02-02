package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleContentService;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Service;

@Service
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {

    @Override
    public ResponseResult saveArticleContent(ApArticleContent apArticleContent) {
        boolean result = save(apArticleContent);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult updateArticleContent(ApArticleContent apArticleContent) {
        LambdaUpdateWrapper<ApArticleContent> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(ApArticleContent::getArticleId, apArticleContent.getArticleId());
        boolean result = update(apArticleContent, lambdaQueryWrapper);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}