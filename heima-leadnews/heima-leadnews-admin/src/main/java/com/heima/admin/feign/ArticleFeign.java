package com.heima.admin.feign;


import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("leadnews-article")
public interface ArticleFeign {

    /**
     * 保存文章(远程调用)
     * @param apArticle
     * @return
     */
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveApArticle(ApArticle apArticle);

    /**
     * 保存文章设置(远程调用)
     * @param apArticleConfig
     * @return
     */
    @PostMapping("/api/v1/article_config/save")
    public ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 保存文章内容(远程调用)
     * @param apArticleContent
     * @return
     */
    @PostMapping("/api/v1/article_content/save")
    public ResponseResult saveArticleContent(ApArticleContent apArticleContent);

    /**
     * 根据名称查询作者(远程调用)
     * @param name
     * @return
     */
    @GetMapping("/api/v1/author/findByName/{name}")
    public ResponseResult findAuthorByName(@PathVariable("name") String name);
}
