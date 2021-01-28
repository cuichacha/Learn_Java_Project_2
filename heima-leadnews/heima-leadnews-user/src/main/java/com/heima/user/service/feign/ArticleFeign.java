package com.heima.user.service.feign;

import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author cuichacha
 * @date 1/27/21 20:46
 */
@FeignClient("leadnews-article")
public interface ArticleFeign {

    /**
     * 保存作者
     * @param apAuthor
     * @return
     */
    @PostMapping("/api/v1/author/save")
    public ResponseResult saveAuthor(@RequestBody ApAuthor apAuthor);

    /**
     * 查询作者
     * @param id
     * @return
     */
    @GetMapping("/findByUserId/{id}")
    public ResponseResult findAuthorByUserId(@PathVariable("id") Integer id);
}
