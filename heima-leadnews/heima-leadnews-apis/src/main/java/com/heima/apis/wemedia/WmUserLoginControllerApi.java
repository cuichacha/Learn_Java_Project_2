package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmUserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/28/21 21:14
 */
@Api(value = "自媒体用户", tags = "WeMediaUser", description = "自媒体用户API")
public interface WmUserLoginControllerApi {
    /**
     * 自媒体用户登录
     * @param wmUserDto
     * @return
     */
    @ApiOperation(value = "自媒体用户登录")
    public abstract ResponseResult wmUserLogin(WmUserDto wmUserDto);
}
