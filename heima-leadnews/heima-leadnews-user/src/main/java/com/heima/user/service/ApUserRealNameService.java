package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.user.dtos.AuthDto;
import com.heima.model.common.user.pojos.ApUserRealName;

/**
 * @author cuichacha
 * @date 1/26/21 16:57
 */
public interface ApUserRealNameService extends IService<ApUserRealName> {

    /**
     * 查询用户认证列表
     * @param authDto
     * @return
     */
    public abstract ResponseResult apUserRealNameList(AuthDto authDto);

    /**
     * 改变审核状态
     * @param authDto
     * @param status
     * @return
     */
    public abstract ResponseResult updateStatusById(AuthDto authDto, Short status);

}
