package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Service;

@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {
    @Override
    public ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig) {
        boolean result = save(apArticleConfig);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult updateArticleConfig(ApArticleConfig apArticleConfig) {
        LambdaUpdateWrapper<ApArticleConfig> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(ApArticleConfig::getArticleId, apArticleConfig.getArticleId());
        boolean result = update(apArticleConfig, lambdaQueryWrapper);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}