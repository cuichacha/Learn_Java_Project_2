package com.heima.article.controller;

import com.heima.apis.article.AuthorControllerApi;
import com.heima.article.service.AuthorService;
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
public class AuthorController implements AuthorControllerApi {

    @Autowired
    private AuthorService authorService;

    @Override
    @PostMapping("/save")
    public ResponseResult saveAuthor(@RequestBody ApAuthor apAuthor) {
        return authorService.saveAuthor(apAuthor);
    }

    @Override
    @GetMapping("/findByUserId/{id}")
    public ResponseResult findAuthorByUserId(@PathVariable("id") Integer userId) {
        return null;
    }
}
