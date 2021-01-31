package com.heima.article.controller.v1;

import com.heima.apis.article.ApArticleContentControllerApi;
import com.heima.article.service.ApArticleContentService;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article_content")
public class ApArticleContentController implements ApArticleContentControllerApi {

    @Autowired
    private ApArticleContentService apArticleContentService;

    @PostMapping("/save")
    @Override
    public ResponseResult saveArticleContent(@RequestBody ApArticleContent apArticleContent) {
        boolean result = apArticleContentService.save(apArticleContent);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
