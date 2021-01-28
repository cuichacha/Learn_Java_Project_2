package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ArticleMapper;
import com.heima.article.service.AuthorService;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Service;

/**
 * @author cuichacha
 * @date 1/27/21 08:59
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<ArticleMapper, ApAuthor> implements AuthorService {
    @Override
    public ResponseResult saveAuthor(ApAuthor apAuthor) {
        // 校验参数
        if (apAuthor == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 保存作者
        boolean result = save(apAuthor);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult findAuthorByUserId(Integer id) {
        // 校验参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询作者
        ApAuthor apAuthor = getById(id);
        if (apAuthor != null) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
    }
}
