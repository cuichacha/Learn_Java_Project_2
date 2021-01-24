package com.heima.apis.admin;

import com.heima.model.common.admin.dtos.ChannelDto;
import com.heima.model.common.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author cuichacha
 * @date 1/23/21 20:20
 */
@Api(value = "频道管理", tags = "channel", description = "频道管理API")
public interface AdChannelControllerApi {

    /**
     * controller层，查询频道列表API的，父接口
     * @param channelDto
     * @return
     */
    @ApiOperation("查询频道列表")
    public abstract ResponseResult findChannelsByNameAndPage(ChannelDto channelDto);

    /**
     * controller层，新建频道API的，父接口
     * @param adChannel
     * @return
     */
    @ApiOperation("新建频道")
    public abstract ResponseResult addChannel(@RequestBody AdChannel adChannel);

    /**
     * controller层，编辑频道API的，父接口
     * @param adChannel
     * @return
     */
    @ApiOperation("更新频道")
    public abstract ResponseResult updateChannel(@RequestBody AdChannel adChannel);

    /**
     * controller层，删除频道API的，父接口
     * @param id
     * @return
     */
    @ApiOperation("删除频道")
    public abstract ResponseResult removeChannel(Integer id);
}
