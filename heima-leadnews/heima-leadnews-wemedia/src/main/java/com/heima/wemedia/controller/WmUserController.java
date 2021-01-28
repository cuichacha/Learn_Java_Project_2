package com.heima.wemedia.controller;

import com.heima.apis.wemedia.WmUserControllerApi;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuichacha
 * @date 1/27/21 20:37
 */
@RestController
@RequestMapping("/api/v1/user")
public class WmUserController implements WmUserControllerApi {

    @Autowired
    private WmUserService wmUserService;

    @Override
    @PostMapping("/save")
    public ResponseResult saveWmUser(@RequestBody WmUser wmUser) {
        return wmUserService.saveWmUser(wmUser);
    }

    @Override
    @GetMapping("/findByName/{name}")
    public ResponseResult findWmUserByName(@PathVariable("name") String name) {
        return wmUserService.findWmUserByName(name);
    }
}
