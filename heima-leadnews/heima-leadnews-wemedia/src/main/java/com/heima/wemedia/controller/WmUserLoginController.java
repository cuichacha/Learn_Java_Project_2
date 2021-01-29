package com.heima.wemedia.controller;

import com.heima.apis.wemedia.WmUserLoginControllerApi;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmUserDto;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuichacha
 * @date 1/28/21 21:15
 */
@RestController
@RequestMapping("/login")
public class WmUserLoginController implements WmUserLoginControllerApi {

    @Autowired
    private WmUserService wmUserService;

    @Override
    @PostMapping("/in")
    public ResponseResult wmUserLogin(@RequestBody WmUserDto wmUserDto) {
        return wmUserService.wmUserLogin(wmUserDto);
    }
}
