package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ArticleMapper;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cuichacha
 * @date 1/27/21 08:59
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ApAuthor> implements ArticleService {
    @Override
    public ResponseResult saveAuthor(ApAuthor apAuthor) {
        // 校验参数
        if (apAuthor == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean result = save(apAuthor);
        return null;
    }
}
