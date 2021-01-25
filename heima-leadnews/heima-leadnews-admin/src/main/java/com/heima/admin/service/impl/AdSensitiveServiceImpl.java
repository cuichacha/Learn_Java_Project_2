package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdSensitiveService;
import com.heima.model.common.admin.dtos.SensitiveDto;
import com.heima.model.common.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * @author cuichacha
 * @date 1/25/21 11:41
 */
@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {
    @Override
    public ResponseResult sensitiveWordsList(SensitiveDto sensitiveDto) {
        return null;
    }

    @Override
    public ResponseResult addSensitiveWords(AdSensitive adSensitive) {
        return null;
    }

    @Override
    public ResponseResult updateSensitiveWords(AdSensitive adSensitive) {
        return null;
    }

    @Override
    public ResponseResult removeSensitiveWords(Integer id) {
        return null;
    }
}
