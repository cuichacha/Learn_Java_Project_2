package com.heima.apis.article;

import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cuichacha
 * @date 1/27/21 07:39
 */
@Api(value = "作者认证", tags = "ApAuthor", description = "作者认证API")
public interface ApAuthorControllerApi {

    /**
     * 保存作者
     * @param apAuthor
     * @return
     */
    @ApiOperation(value = "保存作者")
    public abstract ResponseResult saveAuthor(ApAuthor apAuthor);

    /**
     * 根据uerId查询对应作者
     * @param userId
     * @return
     */
    @ApiOperation(value = "查询作者")
    public abstract ResponseResult findAuthorByUserId(Integer userId);

    /**
     * 根据名称查询作者(远程调用)
     * @param name
     * @return
     */
    @ApiOperation(value = "查询作者(远程调用)")
    public ResponseResult findAuthorByName(String name);

}
