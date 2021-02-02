package com.heima.admin.feign;


import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("leadnews-article")
public interface ArticleFeign {

    /**
     * 保存文章(远程调用)
     *
     * @param apArticle
     * @return
     */
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveApArticle(ApArticle apArticle);

    /**
     * 更新文章(远程调用)
     *
     * @param apArticle
     * @return
     */
    @PutMapping("/api/v1/article/update")
    public ResponseResult updateApArticle(ApArticle apArticle);

    /**
     * 删除文章(远程调用)
     *
     * @param id
     * @return
     */
    @DeleteMapping("/api/v1/article/delete")
    public ResponseResult deleteApArticle(Long id);

    /**
     * 保存文章内容(远程调用)
     *
     * @param apArticleContent
     * @return
     */
    @PostMapping("/api/v1/article_content/save")
    public ResponseResult saveArticleContent(ApArticleContent apArticleContent);

    /**
     * 更新文章内容(远程调用)
     *
     * @param apArticleContent
     * @return
     */
    @PutMapping("/api/v1/article_content/update")
    public ResponseResult updateArticleContent(ApArticleContent apArticleContent);

    /**
     * 删除文章内容(远程调用)
     *
     * @param id
     * @return
     */
    @DeleteMapping("/api/v1/article_content/delete")
    public ResponseResult deleteArticleContent(Long id);

    /**
     * 保存文章设置(远程调用)
     *
     * @param apArticleConfig
     * @return
     */
    @PostMapping("/api/v1/article_config/save")
    public ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 更新文章设置(远程调用)
     *
     * @param apArticleConfig
     * @return
     */
    @PutMapping("/api/v1/article_config/update")
    public ResponseResult updateArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 删除文章设置(远程调用)
     *
     * @param id
     * @return
     */
    @DeleteMapping("/api/v1/article_config/delete")
    public ResponseResult deleteArticleConfig(Long id);

    /**
     * 根据名称查询作者(远程调用)
     *
     * @param name
     * @return
     */
    @GetMapping("/api/v1/author/findByName/{name}")
    public ResponseResult findAuthorByName(@PathVariable("name") String name);

}