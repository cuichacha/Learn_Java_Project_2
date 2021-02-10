package com.heima.admin.controller.v1;

import com.heima.admin.service.WmNewsAuthService;
import com.heima.apis.admin.NewsAuthControllerApi;
import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.pojos.WmNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuichacha
 * @date 2/7/21 17:47
 */
@RestController
@RequestMapping("/api/v1/news_auth")
public class WmNewsAuthController implements NewsAuthControllerApi {

    @Autowired
    private WmNewsAuthService wmNewsAuthService;

    @Override
    @PostMapping("/list")
    public ResponseResult findNews(@RequestBody NewsAuthDto newsAuthDto) {
        return wmNewsAuthService.findNews(newsAuthDto);
    }

    @Override
    @GetMapping("/one/{id}")
    public ResponseResult findOne(@PathVariable("id") Integer id) {
        return wmNewsAuthService.findOne(id);
    }

    @Override
    @PostMapping("/auth_pass")
    public ResponseResult authPass(@RequestBody NewsAuthDto newsAuthDto) {
        return wmNewsAuthService.updateNewsStatus(newsAuthDto, WmNews.Status.SUCCESS.getCode());
    }

    @Override
    @PostMapping("/auth_fail")
    public ResponseResult authFail(@RequestBody NewsAuthDto newsAuthDto) {
        return wmNewsAuthService.updateNewsStatus(newsAuthDto, WmNews.Status.FAIL.getCode());
    }
}
