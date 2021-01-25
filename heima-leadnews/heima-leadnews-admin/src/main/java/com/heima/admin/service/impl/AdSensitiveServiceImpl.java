package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdSensitiveService;
import com.heima.model.common.admin.dtos.SensitiveDto;
import com.heima.model.common.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cuichacha
 * @date 1/25/21 11:41
 */
@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {
    @Override
    public ResponseResult sensitiveWordsList(SensitiveDto sensitiveDto) {
        // 校验参数
        if (sensitiveDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 分页检查
        sensitiveDto.checkParam();
        // 查询结果
        Integer startPage = sensitiveDto.getPage();
        Integer pageSize = sensitiveDto.getSize();
        IPage<AdSensitive> iPage = new Page<>(startPage, pageSize);
        LambdaQueryWrapper<AdSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(sensitiveDto.getName()) && StringUtils.isNotEmpty(sensitiveDto.getName())) {
            lambdaQueryWrapper.like(AdSensitive::getSensitives, sensitiveDto.getName());
        }
        IPage<AdSensitive> adSensitiveIPage = page(iPage, lambdaQueryWrapper);
        // 封装结果
        Long total = adSensitiveIPage.getTotal();
        ResponseResult responseResult = new PageResponseResult(sensitiveDto.getPage(), sensitiveDto.getSize(), total.intValue());
        responseResult.setData(adSensitiveIPage.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult addSensitiveWords(AdSensitive adSensitive) {
        // 检查参数
        if (adSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        adSensitive.setCreatedTime(new Date());
        boolean result = save(adSensitive);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult updateSensitiveWords(AdSensitive adSensitive) {
        // 检查参数
        if (adSensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        adSensitive.setCreatedTime(new Date());
        boolean result = updateById(adSensitive);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult removeSensitiveWords(Integer id) {
        // 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean result = removeById(id);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
