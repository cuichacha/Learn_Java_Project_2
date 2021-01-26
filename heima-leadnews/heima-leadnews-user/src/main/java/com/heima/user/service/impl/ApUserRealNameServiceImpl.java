package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.user.dtos.AuthDto;
import com.heima.model.common.user.pojos.ApUserRealName;
import com.heima.user.mapper.ApUserRealNameMapper;
import com.heima.user.service.ApUserRealNameService;
import org.springframework.stereotype.Service;

/**
 * @author cuichacha
 * @date 1/26/21 16:58
 */
@Service
public class ApUserRealNameServiceImpl extends ServiceImpl<ApUserRealNameMapper, ApUserRealName> implements ApUserRealNameService {


    @Override
    public ResponseResult apUserRealNameList(AuthDto authDto) {
        // 校验参数
        if (authDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 分页检查
        authDto.checkParam();
        // 查询结果
        Integer startPage = authDto.getPage();
        Integer pageSize = authDto.getSize();
        IPage<ApUserRealName> iPage = new Page<>(startPage ,pageSize);
        LambdaQueryWrapper<ApUserRealName> queryWrapper = new LambdaQueryWrapper<>();
        if (authDto.getStatus() != null) {
            queryWrapper.eq(ApUserRealName::getStatus, authDto.getStatus());
        }
        IPage<ApUserRealName> apUserRealNamePage = page(iPage, queryWrapper);
        // 封装结果
        Long total = apUserRealNamePage.getTotal();
        ResponseResult responseResult = new PageResponseResult(startPage, pageSize, total.intValue());
        responseResult.setData(apUserRealNamePage.getRecords());
        return responseResult;
    }
}
