package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmUserDto;
import com.heima.model.common.wemedia.pojos.WmUser;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/27/21 18:29
 */
public interface WmUserService extends IService<WmUser> {

    /**
     * 添加自媒体用户
     * @param wmUser
     * @return
     */
    public abstract ResponseResult saveWmUser(WmUser wmUser);

    /**
     * 查询自媒体用户
     * @param name
     * @return
     */
    public abstract ResponseResult findWmUserByName(String name);

    /**
     * 根据ID查询用户(远程调用)
     * @param id
     * @return
     */
    public abstract ResponseResult findWmUserById(Integer id);

    /**
     * 自媒体用户登录
     * @param wmUserDto
     * @return
     */
    public abstract ResponseResult wmUserLogin(WmUserDto wmUserDto);
}
