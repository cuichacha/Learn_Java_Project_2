package com.heima.wemedia.controller;

import com.heima.apis.wemedia.WmMaterialControllerApi;
import com.heima.common.constants.wemedia.WeMediaContans;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cuichacha
 * @date 1/29/21 20:52
 */

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController implements WmMaterialControllerApi {

    @Autowired
    private WmMaterialService wmMaterialService;

    @Override
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        return wmMaterialService.uploadPicture(multipartFile);
    }

    @Override
    @PostMapping("/list")
    public ResponseResult materialList(@RequestBody WmMaterialDto wmMaterialDto) {
        return wmMaterialService.materialList(wmMaterialDto);
    }

    @Override
    @GetMapping("/del_picture/{id}")
    public ResponseResult deletePicture(@PathVariable("id") Integer id) {
        return null;
    }

    @Override
    @GetMapping("/collect/{id}")
    public ResponseResult addToCollection(@PathVariable("id") Integer add) {
        return wmMaterialService.addToOrRemoveFromCollection(add, WeMediaContans.COLLECT_MATERIAL);
    }

    @Override
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult RemoveFromCollection(@PathVariable("id") Integer cancel) {
        return wmMaterialService.addToOrRemoveFromCollection(cancel, WeMediaContans.CANCEL_COLLECT_MATERIAL);
    }


}
