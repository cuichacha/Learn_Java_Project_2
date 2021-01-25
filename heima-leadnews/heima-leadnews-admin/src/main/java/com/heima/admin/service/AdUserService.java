package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.admin.dtos.AdUserDto;
import com.heima.model.common.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/25/21 15:52
 */
public interface AdUserService extends IService<AdUser> {

    /**
     * 管理员登录
     * @param adUserDto
     * @return
     */
    public abstract ResponseResult login(AdUserDto adUserDto);
}
