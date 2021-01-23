package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.admin.dtos.ChannelDto;
import com.heima.model.common.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/23/21 20:26
 */
public interface AdChannelService extends IService<AdChannel> {

    /**
     * 根据名称分页查询频道列表
     * @param dto
     * @return
     */
    public abstract ResponseResult findChannelsByNameAndPage(ChannelDto dto);
}
