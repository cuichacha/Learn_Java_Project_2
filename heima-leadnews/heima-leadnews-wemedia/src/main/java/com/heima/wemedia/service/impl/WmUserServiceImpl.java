package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.dtos.WmUserDto;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.utils.common.AppJwtUtil;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cuichacha
 * @date 1/27/21 18:30
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveWmUser(WmUser wmUser) {
        //校验参数
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean result = save(wmUser);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult findWmUserByName(String name) {
        // 校验参数
        if (name == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaQueryWrapper<WmUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmUser::getName, name);
        WmUser wmUser = getOne(lambdaQueryWrapper);
        if (wmUser != null) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
    }

    @Override
    public ResponseResult wmUserLogin(WmUserDto wmUserDto) {
        // 检查参数
        if (wmUserDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String name = wmUserDto.getName();
        LambdaQueryWrapper<WmUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmUser::getName, name);
        WmUser wmUser = getOne(lambdaQueryWrapper);
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String inputPassword = wmUserDto.getPassword();
        String salt = wmUser.getSalt();
        String md5Hex = DigestUtils.md5Hex(inputPassword + salt);
        if (md5Hex.equals(wmUser.getPassword())) {
            Integer id = wmUser.getId();
            String token = AppJwtUtil.getToken(id.longValue());
            wmUser.setPassword("");
            wmUser.setSalt("");
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("user", wmUser);
            return ResponseResult.okResult(map);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
    }
}
