package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmMaterialDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cuichacha
 * @date 1/29/21 20:55
 */
@Api(value = "自媒体素材", tags = "WmMaterial", description = "自媒体素材管理API")
public interface WmMaterialControllerApi {

    /**
     * 上传图片素材
     * @param multipartFile
     * @return
     */
    @ApiOperation(value = "图片素材上传")
    public abstract ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 查询素材列表
     * @param wmMaterialDto
     * @return
     */
    @ApiOperation(value = "素材列表查询")
    public abstract ResponseResult materialList(WmMaterialDto wmMaterialDto);

    /**
     * 删除图片素材
     * @param id
     * @return
     */
    @ApiOperation(value = "图片素材删除")
    public abstract ResponseResult deletePicture(Integer id);

    /**
     * 收藏图片素材
     * @param add
     * @return
     */
    @ApiOperation(value = "图片素材收藏")
    public abstract ResponseResult addToCollection(Integer add);

    /**
     * 取消收藏图片素材
     * @param cancel
     * @return
     */
    @ApiOperation(value = "图片素材取消收藏")
    public abstract ResponseResult RemoveFromCollection(Integer cancel);
}
