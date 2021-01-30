package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.model.common.wemedia.pojos.WmMaterial;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author cuichacha
 * @date 1/30/21 09:47
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Value("${fdfs.url}")
    private String fileServerUrl;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Override
    public ResponseResult WmNewsList(WmNewsPageDto wmNewsPageDto) {
        // 检查参数
        if (wmNewsPageDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查分页
        wmNewsPageDto.checkParam();
        //查询数据
        Integer startPage = wmNewsPageDto.getPage();
        Integer pageSize = wmNewsPageDto.getSize();
        IPage<WmNews> iPage = new Page<>(startPage, pageSize);
        LambdaUpdateWrapper<WmNews> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        // 当前用户查询
        WmUser wmUser = WmThreadLocalUtils.getUser();
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        Integer userId = wmUser.getId();
        lambdaQueryWrapper.eq(WmNews::getUserId, userId);
        // 状态查询
        if (wmNewsPageDto.getStatus() != null) {
            lambdaQueryWrapper.eq(WmNews::getStatus, wmNewsPageDto.getStatus());
        }
        // 频道查询
        if (wmNewsPageDto.getChannelId() != null) {
            lambdaQueryWrapper.eq(WmNews::getChannelId, wmNewsPageDto.getChannelId());
        }
        //时间范围查询
        if (wmNewsPageDto.getBeginPublishDate() != null && wmNewsPageDto.getEndPublishDate() != null) {
            lambdaQueryWrapper.between(WmNews::getPublishTime, wmNewsPageDto.getBeginPublishDate(), wmNewsPageDto.getEndPublishDate());
        }
        // 关键字查询
        if (wmNewsPageDto.getKeyword() != null) {
            lambdaQueryWrapper.like(WmNews::getTitle, wmNewsPageDto.getKeyword());
        }
        // 按照创建日期倒序
        lambdaQueryWrapper.orderByDesc(WmNews::getCreatedTime);
        IPage<WmNews> wmNewsIPage = page(iPage, lambdaQueryWrapper);
        // 封装结果
        List<WmNews> wmNewsList = wmNewsIPage.getRecords();
        Long total = wmNewsIPage.getTotal();
        ResponseResult responseResult = new PageResponseResult(startPage, pageSize, total.intValue());
        responseResult.setHost(fileServerUrl);
        responseResult.setData(wmNewsList);
        return responseResult;
    }

    @Override
    public ResponseResult updateNews(WmNewsDto wmNewsDto, Short saveType) {
        // 检查参数
        if (wmNewsDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 获取当前用户
        WmUser wmUser = WmThreadLocalUtils.getUser();
        Integer userId = wmUser.getId();
        // 获取文章类型
        Short newsType = wmNewsDto.getType();
        // 获取文章Id
        Integer newsId = wmNewsDto.getId();
        // 保存文章
        // 判断保存或修改
        WmNews wmNews = null;
        if (newsId == null) {
            wmNews = new WmNews();
        } else {
            wmNews = getById(newsId);
        }
        wmNews.setUserId(userId);
        wmNews.setTitle(wmNewsDto.getTitle());
        wmNews.setContent(wmNewsDto.getContent());
        wmNews.setTitle(newsType.toString());
        wmNews.setChannelId(wmNewsDto.getChannelId());
        wmNews.setLabels(wmNewsDto.getLabels());
        wmNews.setCreatedTime(new Date());
        if (saveType.equals(WmNews.Status.NORMAL.getCode())) {
            wmNews.setStatus(WmNews.Status.NORMAL.getCode());
        } else if (saveType.equals(WmNews.Status.SUBMIT.getCode())) {
            wmNews.setStatus(WmNews.Status.SUBMIT.getCode());
            wmNews.setSubmittedTime(new Date());
        }
        wmNews.setEnable(wmNews.getEnable());
        // 保存素材图片
        List<String> imagesUrls = wmNewsDto.getImages();
        if (imagesUrls.isEmpty()) {
            // 无图
            // 判断保存或者修改

        } else {
            // 有图
            // 先保存文章数据
            wmNews.setImages(imagesUrls.toString().replace("{", "").replace("}", ""));
            save(wmNews);

            // 再保存素材数据
            for (String imagesUrl : imagesUrls) {
                WmMaterial wmMaterial = new WmMaterial();
                wmMaterial.setUrl(imagesUrl);
                wmMaterial.setType((short)0);
                wmMaterial.setIsCollection((short)0);
                wmMaterial.setUserId(userId);
                wmMaterial.setCreatedTime(new Date());
                wmMaterialMapper.insert(wmMaterial);

            }
        }




        return null;
    }

    @Override
    public ResponseResult findNewsById(Integer id) {
        // 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return null;
    }

    @Override
    public ResponseResult deleteNews(Integer id) {
        // 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return null;
    }

    @Override
    public ResponseResult publishOrWithdrawNews(WmNewsDto wmNewsDto) {
        // 检查参数
        if (wmNewsDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return null;
    }
}
