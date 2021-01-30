package com.heima.wemedia.controller;

import com.heima.apis.wemedia.WmNewsControllerApi;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuichacha
 * @date 1/30/21 13:24
 */
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController implements WmNewsControllerApi {

    @Autowired
    private WmNewsService wmNewsService;

    @Override
    @PostMapping("/list")
    public ResponseResult WmNewsList(@RequestBody WmNewsPageDto wmNewsPageDto) {
        return wmNewsService.WmNewsList(wmNewsPageDto);
    }

    @Override
    @PostMapping("/submit")
    public ResponseResult updateNews(@RequestBody WmNewsDto wmNewsDto) {
        return null;
    }

    @Override
    public ResponseResult findNewsById(Integer id) {
        return null;
    }

    @Override
    public ResponseResult deleteNews(Integer id) {
        return null;
    }

    @Override
    public ResponseResult publishOrWithdrawNews(WmNewsDto wmNewsDto) {
        return null;
    }
}
