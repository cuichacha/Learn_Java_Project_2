package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelService;
import com.heima.apis.admin.AdChannelControllerApi;
import com.heima.model.common.admin.dtos.ChannelDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cuichacha
 * @date 1/23/21 21:57
 */
@RestController
@RequestMapping("//api/v1/channel")
public class AdChannelController implements AdChannelControllerApi {

    @Autowired
    private AdChannelService adChannelService;

    @Override
    @PostMapping("/list")
    public ResponseResult findChannelsByNameAndPage(@RequestBody ChannelDto channelDto) {
        return adChannelService.findChannelsByNameAndPage(channelDto);
    }
}
