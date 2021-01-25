package com.heima.apis.admin;

import com.heima.model.common.admin.dtos.AdUserDto;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/25/21 15:52
 */
@Api(value = "管理员登录", tags = "UserLogin", description = "管理员登录API")
public interface AdUserControllerApi {

    /**
     * Controller层中用户登录API的父接口
     * @param adUserDto
     * @return
     */
    @ApiOperation("管理员登录")
    public abstract ResponseResult userLogin(AdUserDto adUserDto);
}
