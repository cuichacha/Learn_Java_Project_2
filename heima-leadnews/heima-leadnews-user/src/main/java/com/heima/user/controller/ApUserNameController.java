package com.heima.user.controller;

import com.heima.apis.user.ApUserRealNameControllerApi;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.user.dtos.AuthDto;
import com.heima.user.service.ApUserRealNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuichacha
 * @date 1/26/21 17:20
 */
@RestController
@RequestMapping("/api/v1/auth")
public class ApUserNameController implements ApUserRealNameControllerApi {

    @Autowired
    private ApUserRealNameService apUserRealNameService;

    @Override
    @RequestMapping("/list")
    public ResponseResult userListByStatus(@RequestBody AuthDto authDto) {
        return apUserRealNameService.apUserRealNameList(authDto);
    }
}
