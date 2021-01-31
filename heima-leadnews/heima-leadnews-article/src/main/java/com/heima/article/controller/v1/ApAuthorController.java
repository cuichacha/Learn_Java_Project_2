package com.heima.article.controller.v1;

import com.heima.apis.article.ApAuthorControllerApi;
import com.heima.article.service.ApAuthorService;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuichacha
 * @date 1/27/21 20:33
 */
@RestController
@RequestMapping("/api/v1/author")
public class ApAuthorController implements ApAuthorControllerApi {

    @Autowired
    private ApAuthorService apAuthorService;

    @Override
    @PostMapping("/save")
    public ResponseResult saveAuthor(@RequestBody ApAuthor apAuthor) {
        return apAuthorService.saveAuthor(apAuthor);
    }

    @Override
    @GetMapping("/findByUserId/{id}")
    public ResponseResult findAuthorByUserId(@PathVariable("id") Integer userId) {
        return apAuthorService.findAuthorByUserId(userId);
    }

    @Override
    @GetMapping("/findByName/{name}")
    public ResponseResult findAuthorByName(@PathVariable("name") String name) {
        return apAuthorService.findAuthorByName(name);
    }
}
