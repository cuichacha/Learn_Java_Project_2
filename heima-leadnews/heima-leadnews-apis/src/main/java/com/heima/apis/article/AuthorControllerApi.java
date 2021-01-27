package com.heima.apis.article;

import com.heima.model.common.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cuichacha
 * @date 1/27/21 07:39
 */
@Api(value = "认证作者", tags = "ApAuthor", description = "认证作者API")
public interface AuthorControllerApi {

    /**
     * 保存作者
     * @param apAuthor
     * @return
     */
    @ApiOperation(value = "保存作者")
    public abstract ResponseResult saveAuthor(ApAuthor apAuthor);

}
