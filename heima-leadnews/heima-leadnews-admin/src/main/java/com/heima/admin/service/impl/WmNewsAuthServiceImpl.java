package com.heima.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.admin.feign.WeMediaFeign;
import com.heima.admin.service.WmNewsAuthService;
import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.pojos.WmNews;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuichacha
 * @date 2/7/21 17:50
 */
@Service
public class WmNewsAuthServiceImpl implements WmNewsAuthService {

    @Autowired
    private WeMediaFeign weMediaFeign;

    @Override
    public ResponseResult findNews(NewsAuthDto newsAuthDto) {
        return weMediaFeign.findWmNewsList(newsAuthDto);
    }

    @Override
    public ResponseResult findOne(Integer id) {
        return weMediaFeign.findWmNewsVo(id);
    }

    @Override
    @GlobalTransactional
    public ResponseResult updateNewsStatus(NewsAuthDto newsAuthDto, Short status) {
        // 校验参数
        if (newsAuthDto == null || status == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 状态必须为4人工审核通过或者2审核失败
        if (status != WmNews.Status.ADMIN_SUCCESS.getCode() || status != WmNews.Status.FAIL.getCode()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 先查询对应文章wmNews
        Integer newsId = newsAuthDto.getId();
        ResponseResult wmNewsById = weMediaFeign.findById(newsId);
        if (!wmNewsById.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 获得文章对象
        Object newsByIdData = wmNewsById.getData();
        WmNews wmNews = JSON.parseObject(newsByIdData.toString(), WmNews.class);
        // 审核通过/拒绝
        if (status.equals(WmNews.Status.ADMIN_SUCCESS.getCode())) {
            wmNews.setStatus(WmNews.Status.ADMIN_SUCCESS.getCode());
            wmNews.setReason("人工审核通过");
        } else if (status.equals(WmNews.Status.FAIL.getCode())) {
            wmNews.setStatus(WmNews.Status.FAIL.getCode());
            wmNews.setReason(newsAuthDto.getMsg());
        }
        // 全局事务控制
        ResponseResult updateWmNews = weMediaFeign.updateWmNews(wmNews);
        if (!updateWmNews.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            throw new RuntimeException("远程调用更新文章状态发生异常");
        } else {
            return updateWmNews;
        }
    }
}
