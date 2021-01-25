package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.model.common.admin.dtos.AdUserDto;
import com.heima.model.common.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.BCrypt;
import io.jsonwebtoken.JwtBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cuichacha
 * @date 1/25/21 16:29
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {

    @Override
    public ResponseResult login(AdUserDto adUserDto) {
        // 校验参数
        if (StringUtils.isEmpty(adUserDto.getName()) || StringUtils.isEmpty(adUserDto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码不能为空");
        }
        // 判断登录管理员是否存在
        String username = adUserDto.getName();
        QueryWrapper<AdUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        AdUser adUser = getOne(queryWrapper);
        if (adUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 再判断密码是否正确
        String salt = adUser.getSalt();
        String inputPassword = adUserDto.getPassword();
        String password = adUser.getPassword();
        String md5Hex = DigestUtils.md5Hex(inputPassword + salt);
        if (md5Hex.equals(password)) {
            // 密码正确则返回token
            Integer id = adUser.getId();
            String token = AppJwtUtil.getToken(id.longValue());
            adUser.setPassword("");
            adUser.setSalt("");
            Map<String, Object> map = Maps.newHashMap();
            map.put("token", token);
            map.put("adUser", adUser);
            return ResponseResult.okResult(map);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
    }
}
