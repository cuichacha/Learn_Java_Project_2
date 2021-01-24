package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelService;
import com.heima.apis.admin.AdChannelControllerApi;
import com.heima.model.common.admin.dtos.ChannelDto;
import com.heima.model.common.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuichacha
 * @date 1/23/21 21:57
 */
@RestController
@RequestMapping("/api/v1/channel")
public class AdChannelController implements AdChannelControllerApi {

    @Autowired
    private AdChannelService adChannelService;

    @Override
    @PostMapping("/list")
    public ResponseResult findChannelsByNameAndPage(@RequestBody ChannelDto channelDto) {
        return adChannelService.findChannelsByNameAndPage(channelDto);
    }

    @Override
    @PostMapping("/save")
    public ResponseResult addChannel(AdChannel adChannel) {
        return adChannelService.addChannels(adChannel);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult updateChannel(AdChannel adChannel) {
        return adChannelService.updateChannel(adChannel);
    }

    @Override
    @GetMapping("/del/{id}")
    public ResponseResult removeChannel(@PathVariable("id") Integer id) {
        return adChannelService.removeChannel(id);
    }
}
