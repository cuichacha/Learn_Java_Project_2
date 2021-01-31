package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author cuichacha
 * @date 1/27/21 08:57
 */
public interface ApAuthorService extends IService<ApAuthor> {

    /**
     * 保存作者
     *
     * @param apAuthor
     * @return
     */
    public abstract ResponseResult saveAuthor(ApAuthor apAuthor);

    /**
     * 根据ID查询作者
     *
     * @param id
     * @return
     */
    public abstract ResponseResult findAuthorByUserId(Integer id);

    /**
     * 根据名称查询作者(远程调用)
     *
     * @param name
     * @return
     */
    public abstract ResponseResult findAuthorByName(String name);
}
