package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.dtos.WmMaterialDto;
import com.heima.model.common.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cuichacha
 * @date 1/29/21 21:00
 */
public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    public abstract ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 查询素材列表
     * @param wmMaterialDto
     * @return
     */
    public abstract ResponseResult materialList(WmMaterialDto wmMaterialDto);

    /**
     * 删除图片素材
     * @param id
     * @return
     */
    public abstract ResponseResult deletePicture(Integer id);

    /**
     * 收藏或取消收藏图片素材
     * @param id
     * @param type
     * @return
     */
    public abstract ResponseResult addToOrRemoveFromCollection(Integer id, Short type);
}
