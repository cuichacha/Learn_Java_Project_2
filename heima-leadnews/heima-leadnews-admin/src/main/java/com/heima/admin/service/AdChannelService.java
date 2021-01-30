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

    /**
     * 查询频道列表
     * @return
     */
    public abstract ResponseResult findChannelList();

    /**
     * 新建频道
     * @param adChannel
     * @return
     */
    public abstract ResponseResult addChannels(AdChannel adChannel);

    /**
     * 编辑频道
     * @param adChannel
     * @return
     */
    public abstract ResponseResult updateChannel(AdChannel adChannel);

    /**
     * 删除频道
     * @param id
     * @return
     */
    public abstract ResponseResult removeChannel(Integer id);
}
