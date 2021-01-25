package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.admin.dtos.SensitiveDto;
import com.heima.model.common.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/25/21 11:38
 */
public interface AdSensitiveService extends IService<AdSensitive> {

    /**
     * 查询敏感词列表
     * @param sensitiveDto
     * @return
     */
    public abstract ResponseResult sensitiveWordsList(SensitiveDto sensitiveDto);

    /**
     * 增加敏感词
     * @param adSensitive
     * @return
     */
    public abstract ResponseResult addSensitiveWords(AdSensitive adSensitive);

    /**
     * 更新敏感词
     * @param adSensitive
     * @return
     */
    public abstract ResponseResult updateSensitiveWords(AdSensitive adSensitive);

    /**
     * 删除敏感词
     * @param id
     * @return
     */
    public abstract ResponseResult removeSensitiveWords(Integer id);
}
