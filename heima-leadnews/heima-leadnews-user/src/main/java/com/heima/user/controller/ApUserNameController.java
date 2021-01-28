package com.heima.user.controller;

import com.heima.apis.user.ApUserRealNameControllerApi;
import com.heima.common.constants.UserConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.user.dtos.AuthDto;
import com.heima.user.service.ApUserRealNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/list")
    public ResponseResult userListByStatus(@RequestBody AuthDto authDto) {
        return apUserRealNameService.apUserRealNameList(authDto);
    }

    @Override
    @PostMapping("/authPass")
    public ResponseResult authPass(@RequestBody AuthDto authDto) {
        return apUserRealNameService.updateStatusById(authDto, UserConstants.PASS_AUTH);
    }

    @Override
    @PostMapping("/authFail")
    public ResponseResult authFail(@RequestBody AuthDto authDto) {
        return apUserRealNameService.updateStatusById(authDto, UserConstants.FAIL_AUTH);
    }
}
