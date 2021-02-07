package com.heima.admin.feign;

import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.pojos.WmUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author cuichacha
 * @date 1/31/21 13:53
 */
@FeignClient("leadnews-wemedia")
public interface WeMediaFeign {

    /**
     * 根据ID查询文章
     *
     * @param id
     * @return
     */
    @GetMapping("/api/v1/news/findOne/{id}")
    ResponseResult findById(@PathVariable("id") Integer id);

    /**
     * 更新文章状态(远程调用)
     *
     * @param wmNews
     * @return
     */
    @PostMapping("/api/v1/news/update")
    ResponseResult updateWmNews(WmNews wmNews);

    /**
     * 根据ID查询作者
     *
     * @param id
     * @return
     */
    @GetMapping("/api/v1/user/findOne/{id}")
    ResponseResult findWmUserById(@PathVariable("id") Integer id);

    /**
     * 人工审核文章列表(远程调用)
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/news/findList/")
    public ResponseResult findWmNewsList(NewsAuthDto dto);

    /**
     * 人工审核文章详情(远程调用)
     *
     * @param id
     * @return
     */
    @GetMapping("/api/v1/news/find_news_vo/{id}")
    public ResponseResult findWmNewsVo(@PathVariable("id") Integer id);
}
