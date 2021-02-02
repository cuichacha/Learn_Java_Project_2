package com.heima.article.controller.v1;

import com.heima.apis.article.ApArticleControllerApi;
import com.heima.article.service.ApArticleService;
import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ApArticleController implements ApArticleControllerApi {

    @Autowired
    private ApArticleService articleService;

    @Override
    @PostMapping("save")
    public ResponseResult saveApArticle(@RequestBody ApArticle apArticle) {
        return articleService.saveApArticle(apArticle);
    }

    @Override
    @PutMapping("/update")
    public ResponseResult updateApArticle(@RequestBody ApArticle apArticle) {
        return articleService.updateApArticle(apArticle);
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseResult deleteApArticle(@RequestBody Long id) {
        return articleService.deleteApArticle(id);
    }
}
