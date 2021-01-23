package com.heima.apis.admin;

import com.heima.model.common.admin.dtos.ChannelDto;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/23/21 20:20
 */
public interface AdChannelControllerApi {

    /**
     * controller层API的接口
     * @param channelDto
     * @return
     */
    public abstract ResponseResult findChannelsByNameAndPage(ChannelDto channelDto);
}
