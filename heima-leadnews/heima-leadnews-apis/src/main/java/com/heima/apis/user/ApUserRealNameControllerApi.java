package com.heima.apis.user;

import com.heima.model.common.admin.dtos.AdUserDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.user.dtos.AuthDto;
import io.swagger.annotations.Api;

/**
 * @author cuichacha
 * @date 1/26/21 16:48
 */
@Api(value = "用户认证列表", tags = "ApUserRealName", description = "用户认证列表查询API")
public interface ApUserRealNameControllerApi {

    /**
     * 根据状态查询用户认证列表
     * @param authDto
     * @return
     */
    public abstract ResponseResult userListByStatus(AuthDto authDto);
}
