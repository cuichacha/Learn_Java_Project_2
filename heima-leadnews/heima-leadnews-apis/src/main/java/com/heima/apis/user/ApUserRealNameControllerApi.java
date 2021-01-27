package com.heima.apis.user;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.user.dtos.AuthDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/26/21 16:48
 */
@Api(value = "用户认证", tags = "ApUserRealName", description = "用户认证列表API")
public interface ApUserRealNameControllerApi {

    /**
     * 根据状态查询用户认证列表
     * @param authDto 用户审核Dto
     * @return 返回通过结果对象
     */
    @ApiOperation(value = "查询用户认证列表")
    public abstract ResponseResult userListByStatus(AuthDto authDto);

    /**
     * 审核通过
     * @param authDto
     * @return
     */
    @ApiOperation(value = "审核通过")
    public abstract ResponseResult authPass(AuthDto authDto);

    /**
     * 审核失败
     * @param authDto
     * @return
     */
    @ApiOperation(value = "审核失败")
    public abstract ResponseResult authFail(AuthDto authDto);
}
