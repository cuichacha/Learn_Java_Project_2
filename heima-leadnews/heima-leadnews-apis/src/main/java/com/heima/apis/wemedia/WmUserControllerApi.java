package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmUserDto;
import com.heima.model.common.wemedia.pojos.WmUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/27/21 17:04
 */
@Api(value = "自媒体用户", tags = "WeMediaUser", description = "自媒体用户API")
public interface WmUserControllerApi {

    /**
     * 添加自媒体用户
     * @param wmUser
     * @return
     */
    @ApiOperation(value = "添加自媒体用户")
    public abstract ResponseResult saveWmUser(WmUser wmUser);

    /**
     * 按照名称查询用户
     * @param name
     * @return
     */
    @ApiOperation(value = "查询自媒体用户")
    public abstract ResponseResult findWmUserByName(String name);

    /**
     * 根据ID查询用户(远程调用)
     * @param id
     * @return
     */
    @ApiOperation(value = "查询自媒体用户(远程调用)")
    public abstract ResponseResult findWmUserById(Integer id);


}
