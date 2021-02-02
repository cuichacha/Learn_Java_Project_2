package com.heima.wemedia.controller;

import com.alibaba.fastjson.JSON;
import com.heima.apis.wemedia.WmNewsControllerApi;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult wmNewsList(@RequestBody WmNewsPageDto wmNewsPageDto) {
        return wmNewsService.WmNewsList(wmNewsPageDto);
    }

    @Override
    @PostMapping("/submit")
    public ResponseResult updateNews(@RequestBody WmNewsDto wmNewsDto) {
        if (wmNewsDto.getStatus().equals(WmNews.Status.SUBMIT.getCode())) {
            return wmNewsService.updateNews(wmNewsDto, WmNews.Status.SUBMIT.getCode());
        } else {
            return wmNewsService.updateNews(wmNewsDto, WmNews.Status.NORMAL.getCode());
        }
    }

    @Override
    @PostMapping("/update")
    public ResponseResult updateWmNews(@RequestBody WmNews wmNews) {
        boolean result = wmNewsService.updateById(wmNews);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    @GetMapping("/one/{id}")
    public ResponseResult findNewsById(@PathVariable("id") Integer id) {
        return wmNewsService.findNewsById(id);
    }

    @Override
    @GetMapping("/findOne/{id}")
    public ResponseResult findById(@PathVariable("id") Integer id) {
        return wmNewsService.findById(id);
    }

    @Override
    @GetMapping("/del_news/{id}")
    public ResponseResult deleteNews(@PathVariable("id") Integer id) {
        return wmNewsService.deleteNews(id);
    }

    @Override
    @PostMapping("/down_or_up")
    public ResponseResult publishOrWithdrawNews(@RequestBody WmNewsDto wmNewsDto) {
        return wmNewsService.publishOrWithdrawNews(wmNewsDto);
    }
}
