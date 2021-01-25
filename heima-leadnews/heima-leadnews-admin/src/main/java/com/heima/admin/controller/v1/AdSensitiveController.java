package com.heima.admin.controller.v1;

import com.heima.admin.service.AdSensitiveService;
import com.heima.apis.admin.AdSensitiveControllerApi;
import com.heima.model.common.admin.dtos.SensitiveDto;
import com.heima.model.common.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuichacha
 * @date 1/25/21 11:37
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController implements AdSensitiveControllerApi {

    @Autowired
    private AdSensitiveService adSensitiveService;

    @Override
    @PostMapping("/list")
    public ResponseResult sensitiveWordsList(@RequestBody SensitiveDto sensitiveDto) {
        return adSensitiveService.sensitiveWordsList(sensitiveDto);
    }

    @Override
    @PostMapping("/save")
    public ResponseResult addSensitiveWords(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.addSensitiveWords(adSensitive);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult updateSensitiveWords(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.updateSensitiveWords(adSensitive);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult removeSensitiveWords(@PathVariable("id") Integer id) {
        return adSensitiveService.removeSensitiveWords(id);
    }
}
