package com.heima.admin.service;

/**
 * @author cuichacha
 * @date 2/2/21 14:50
 */
public interface ArticleDeleteService {

    /**
     * 远程调用删除article数据库中的数据
     * @param id
     */
    public abstract void deleteArticles(Long id);
}
