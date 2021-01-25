package com.heima.apis.admin;

import com.heima.model.common.admin.dtos.SensitiveDto;
import com.heima.model.common.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;

/**
 * @author cuichacha
 * @date 1/25/21 11:22
 */
@Api(value = "敏感词管理", tags = "sensitive", description = "敏感词管理API")
public interface AdSensitiveControllerApi {

    /**
     * controller层，查询敏感词列表API的，父接口
     * @param sensitiveDto
     * @return
     */
    public abstract ResponseResult sensitiveWordsList(SensitiveDto sensitiveDto);

    /**
     * controller层，增加敏感词API的，父接口
     * @param AdSensitive
     * @return
     */
    public abstract ResponseResult addSensitiveWords(AdSensitive AdSensitive);

    /**
     * controller层，更新敏感词API的，父接口
     * @param AdSensitive
     * @return
     */
    public abstract ResponseResult updateSensitiveWords(AdSensitive AdSensitive);

    /**
     * controller层，删除敏感词API的，父接口
     * @param id
     * @return
     */
    public abstract ResponseResult removeSensitiveWords(Integer id);
}
