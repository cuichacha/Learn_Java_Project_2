package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.model.common.admin.dtos.ChannelDto;
import com.heima.model.common.admin.pojos.AdChannel;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuichacha
 * @date 1/23/21 20:28
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {

    @Autowired
    private AdChannelMapper adChannelMapper;


    @Override
    public ResponseResult findChannelsByNameAndPage(ChannelDto dto) {

        // 1. 参数检测
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2. 分页参数检查
        dto.checkParam();

        // 3. 查询结果
        IPage<AdChannel> page = new Page<>(dto.getPage(), dto.getSize());
        QueryWrapper<AdChannel> queryWrapper= new QueryWrapper<>();
        if (dto.getName() != null) {
            queryWrapper.like("name", dto.getName());
        }
        IPage<AdChannel> adChannelIPage = adChannelMapper.selectPage(page, queryWrapper);

        // 4. 封装结果
        Long total = adChannelIPage.getTotal();
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), total.intValue());
        responseResult.setData(adChannelIPage.getRecords());

        return responseResult;
    }
}
