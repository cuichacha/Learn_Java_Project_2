package com.heima.user.service.feign;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.wemedia.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author cuichacha
 * @date 1/27/21 20:51
 */
@FeignClient("leadnews-wemedia")
public interface WeMediaFeign {

    /**
     * 添加自媒体用户
     * @param wmUser
     * @return
     */
    @PostMapping("/api/v1/user/save")
    public ResponseResult saveWmUser(@RequestBody WmUser wmUser);

    /**
     * 查询自媒体用户
     * @param name
     * @return
     */
    @GetMapping("/api/v1/user/findByName/{name}")
    public ResponseResult findByName(@PathVariable("name") String name);
}
