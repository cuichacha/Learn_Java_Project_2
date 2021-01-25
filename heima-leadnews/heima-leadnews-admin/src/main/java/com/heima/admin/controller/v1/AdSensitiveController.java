package com.heima.admin.controller.v1;

import com.heima.apis.admin.AdSensitiveControllerApi;
import com.heima.model.common.admin.dtos.SensitiveDto;
import com.heima.model.common.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuichacha
 * @date 1/25/21 11:37
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController implements AdSensitiveControllerApi {
    @Override
    public ResponseResult sensitiveWordsList(SensitiveDto sensitiveDto) {
        return null;
    }

    @Override
    public ResponseResult addSensitiveWords(AdSensitive AdSensitive) {
        return null;
    }

    @Override
    public ResponseResult updateSensitiveWords(AdSensitive AdSensitive) {
        return null;
    }

    @Override
    public ResponseResult removeSensitiveWords(Integer id) {
        return null;
    }
}
