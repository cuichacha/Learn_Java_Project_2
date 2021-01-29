package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.fastdfs.FastDFSClient;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.dtos.WmMaterialDto;
import com.heima.model.common.wemedia.pojos.WmMaterial;
import com.heima.model.common.wemedia.pojos.WmNewsMaterial;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cuichacha
 * @date 1/29/21 21:01
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FastDFSClient fastDFSClient;

    @Value("${fdfs.url}")
    private String fileServerUrl;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 校验参数
        if (multipartFile == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 上传图片
        String fileUrl = null;
        try {
            fileUrl = fastDFSClient.uploadFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 保存到表中
        WmUser wmUser = WmThreadLocalUtils.getUser();
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(wmUser.getId());
        wmMaterial.setUrl(fileUrl);
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());
        boolean result = save(wmMaterial);
        if (result) {
            wmMaterial.setUrl(fileServerUrl + fileUrl);
            return ResponseResult.okResult(wmMaterial);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult materialList(WmMaterialDto wmMaterialDto) {
        // 校验参数
        if (wmMaterialDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查分页
        wmMaterialDto.checkParam();
        // 获取当前用户ID
        WmUser wmUser = WmThreadLocalUtils.getUser();
        Integer userId = wmUser.getId();
        // 分页查询数据
        Integer startPage = wmMaterialDto.getPage();
        Integer pageSize = wmMaterialDto.getSize();
        IPage<WmMaterial> iPage = new Page<>(startPage, pageSize);
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmMaterial::getUserId, userId);
        // 收藏数据查询
        if(wmMaterialDto.getIsCollected() != null && wmMaterialDto.getIsCollected() ==1){
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection,wmMaterialDto.getIsCollected());
        }
        // 按照日期倒序排序
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        IPage<WmMaterial> wmMaterialIPage = page(iPage, lambdaQueryWrapper);
        // 封装参数
        Long total = wmMaterialIPage.getTotal();
        List<WmMaterial> wmMaterials = wmMaterialIPage.getRecords();
        // 给每个图片素材连接补充完整方便显示
        wmMaterials = wmMaterials.stream().map(new Function<WmMaterial, WmMaterial>() {
            @Override
            public WmMaterial apply(WmMaterial wmMaterial) {
                wmMaterial.setUrl(fileServerUrl+wmMaterial.getUrl());
                return wmMaterial;
            }
        }).collect(Collectors.toList());

        ResponseResult responseResult = new PageResponseResult(startPage, pageSize, total.intValue());
        responseResult.setData(wmMaterials);
        return responseResult;
    }

    @Override
    public ResponseResult deletePicture(Integer id) {
        // 校验参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询图片是否被引用
        LambdaQueryWrapper<WmNewsMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmNewsMaterial::getMaterialId,id);
        Integer count = wmNewsMaterialMapper.selectCount(lambdaQueryWrapper);
        if(count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"当前图片被引用");
        }
        // 删除FastDFS中的图片
        WmMaterial wmMaterial = getById(id);
        if (wmMaterial == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String wmMaterialUrl = wmMaterial.getUrl();
        fastDFSClient.delFile(wmMaterialUrl);
        // 删除数据库记录
        boolean result = removeById(id);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult addToOrRemoveFromCollection(Integer id, Short type) {
        // 校验参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer userId = WmThreadLocalUtils.getUser().getId();
        LambdaUpdateWrapper<WmMaterial> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(WmMaterial::getIsCollection, type).eq(WmMaterial::getId, id).eq(WmMaterial::getUserId, userId);
        boolean result = update(lambdaUpdateWrapper);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
