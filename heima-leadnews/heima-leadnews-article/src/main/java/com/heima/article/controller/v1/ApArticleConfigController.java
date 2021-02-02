package com.heima.article.controller.v1;

import com.heima.apis.article.ApArticleConfigControllerApi;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article_config")
public class ApArticleConfigController implements ApArticleConfigControllerApi {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @Override
    @PostMapping("/save")
    public ResponseResult saveArticleConfig(@RequestBody ApArticleConfig apArticleConfig) {
        return apArticleConfigService.saveArticleConfig(apArticleConfig);
    }

    @Override
    @PutMapping("update")
    public ResponseResult updateArticleConfig(@RequestBody ApArticleConfig apArticleConfig) {
        return apArticleConfigService.updateArticleConfig(apArticleConfig);
    }

    @Override
    @DeleteMapping("delete")
    public ResponseResult deleteArticleConfig(@RequestBody Long id) {
        return apArticleConfigService.deleteArticleConfig(id);
    }
}
