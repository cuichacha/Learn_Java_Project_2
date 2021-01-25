package com.heima.admin.controller.v1;

import com.heima.admin.service.AdUserService;
import com.heima.apis.admin.AdUserControllerApi;
import com.heima.model.common.admin.dtos.AdUserDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuichacha
 * @date 1/25/21 16:50
 */
@RestController
@RequestMapping("/login")
public class AdUserController implements AdUserControllerApi {

    @Autowired
    private AdUserService adUserService;

    @Override
    @PostMapping("/in")
    public ResponseResult userLogin(@RequestBody AdUserDto adUserDto) {
        return adUserService.login(adUserDto);
    }
}
