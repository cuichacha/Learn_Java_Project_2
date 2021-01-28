package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.UserConstants;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.user.dtos.AuthDto;
import com.heima.model.common.user.pojos.ApUser;
import com.heima.model.common.user.pojos.ApUserRealName;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealNameMapper;
import com.heima.user.service.ApUserRealNameService;
import com.heima.user.service.feign.ArticleFeign;
import com.heima.user.service.feign.WeMediaFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cuichacha
 * @date 1/26/21 16:58
 */
@Service
public class ApUserRealNameServiceImpl extends ServiceImpl<ApUserRealNameMapper, ApUserRealName> implements ApUserRealNameService {

    @Autowired
    private ArticleFeign articleFeign;

    @Autowired
    private WeMediaFeign weMediaFeign;

    @Autowired
    private ApUserMapper apUserMapper;

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
        IPage<ApUserRealName> iPage = new Page<>(startPage, pageSize);
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

    @Override
    public ResponseResult updateStatusById(AuthDto authDto, Short status) {
        // 校验参数
        if (authDto == null || authDto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 校验状态
        if (!checkStatus(status)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 改变用户状态
        ApUserRealName apUserRealName = new ApUserRealName();
        apUserRealName.setCreatedTime(new Date());
        apUserRealName.setId(authDto.getId());
        apUserRealName.setStatus(status);
        if (authDto.getMsg() != null) {
            apUserRealName.setReason(authDto.getMsg());
        }
        boolean result = updateById(apUserRealName);
        // 添加作者信息和自媒体信息
        if (result) {
            if (status.equals(UserConstants.PASS_AUTH)) {
                Integer apUserRealNameId = authDto.getId();
                ApUserRealName userRealName = getById(apUserRealNameId);
                Integer apUserRealNameUserId = userRealName.getUserId();
                // 先判断作者是否以及存在于数据库中
                ResponseResult apAuthorResult = articleFeign.findAuthorByUserId(apUserRealNameUserId);
                Integer apAuthorResultCode = apAuthorResult.getCode();
                String name = userRealName.getName();
                ApAuthor apAuthor = new ApAuthor();
                if (apAuthorResultCode.equals(AppHttpCodeEnum.DATA_NOT_EXIST.getCode())) {
                    // 如不存在则添加
                    apAuthor.setUserId(apUserRealNameUserId);
                    apAuthor.setName(name);
                    apAuthor.setType(UserConstants.AUTH_TYPE);
                    apAuthor.setCreatedTime(new Date());
                    articleFeign.saveAuthor(apAuthor);
                }
                // 先判断自媒体是否以及存在于数据库中
                ResponseResult wmUserResult = weMediaFeign.findByName(name);
                Integer wmUserResultCode = wmUserResult.getCode();
                if (wmUserResultCode.equals(AppHttpCodeEnum.DATA_NOT_EXIST.getCode())) {
                    // 如不存在则添加
                    // 先获取ap_user信息
                    ApUser apUser = apUserMapper.selectById(apUserRealNameId);
                    if (apUser == null) {
                        return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
                    }
                    WmUser wmUser = new WmUser();
                    wmUser.setApUserId(apUser.getId());
                    wmUser.setApAuthorId(apAuthor.getId());
                    wmUser.setName(apUser.getName());
                    wmUser.setPassword(apUser.getPassword());
                    wmUser.setSalt(apUser.getSalt());
                    wmUser.setPhone(apUser.getPhone());
                    wmUser.setStatus(status.intValue());
                    wmUser.setCreatedTime(new Date());
                    weMediaFeign.saveWmUser(wmUser);
                }
            }
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);

    }

    /**
     * 检查状态
     *
     * @param status
     * @return
     */
    private boolean checkStatus(Short status) {
        if (status == null || (!status.equals(UserConstants.FAIL_AUTH) && !status.equals(UserConstants.PASS_AUTH))) {
            return true;
        }
        return false;
    }

}
