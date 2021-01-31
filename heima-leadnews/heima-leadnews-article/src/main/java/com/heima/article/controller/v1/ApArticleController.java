package com.heima.article.controller.v1;

import com.heima.apis.article.ApArticleControllerApi;
import com.heima.article.service.ApArticleService;
import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ApArticleController implements ApArticleControllerApi {

    @Autowired
    private ApArticleService articleService;

    @PostMapping("save")
    @Override
    public ResponseResult saveApArticle(@RequestBody ApArticle apArticle) {
        boolean result = articleService.save(apArticle);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    @GetMapping("/findByNameAndAuthorId")
    public ResponseResult findArticleByNameAndAuthorId(@RequestBody String title, @RequestBody Integer authorId) {
        return articleService.findArticleByNameAndAuthorId(title, authorId);
    }
}
