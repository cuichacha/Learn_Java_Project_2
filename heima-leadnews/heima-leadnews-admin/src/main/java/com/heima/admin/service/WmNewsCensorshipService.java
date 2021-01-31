package com.heima.admin.service;

/**
 * @author cuichacha
 * @date 1/31/21 13:39
 */
public interface WmNewsCensorshipService {

    /**
     * 根据文章id进行审核
     * @param id
     */
    public abstract void censorByWmNewsId(Integer id);
}
