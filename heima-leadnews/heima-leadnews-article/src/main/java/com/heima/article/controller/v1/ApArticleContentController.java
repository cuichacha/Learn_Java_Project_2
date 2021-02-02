package com.heima.article.controller.v1;

import com.heima.apis.article.ApArticleContentControllerApi;
import com.heima.article.service.ApArticleContentService;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article_content")
public class ApArticleContentController implements ApArticleContentControllerApi {

    @Autowired
    private ApArticleContentService apArticleContentService;

    @Override
    @PostMapping("/save")
    public ResponseResult saveArticleContent(@RequestBody ApArticleContent apArticleContent) {
        return apArticleContentService.saveArticleContent(apArticleContent);
    }

    @Override
    @PutMapping("/update")
    public ResponseResult updateArticleContent(@RequestBody ApArticleContent apArticleContent) {
        return apArticleContentService.updateArticleContent(apArticleContent);
    }
}
