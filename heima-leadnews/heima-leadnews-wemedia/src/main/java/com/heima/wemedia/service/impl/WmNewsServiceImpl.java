package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.wemedia.WeMediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.model.common.wemedia.pojos.WmMaterial;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.pojos.WmNewsMaterial;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateNews(WmNewsDto wmNewsDto, Short saveType) {
        // 检查参数
        if (wmNewsDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 获取当前用户
        WmUser wmUser = WmThreadLocalUtils.getUser();
        Integer userId = wmUser.getId();
        // 获取文章Id
        Integer newsId = wmNewsDto.getId();
        // 保存文章
        WmNews wmNews = new WmNews();
        wmNews.setUserId(userId);
        wmNews.setTitle(wmNewsDto.getTitle());
        wmNews.setContent(wmNewsDto.getContent());
        wmNews.setType(wmNewsDto.getType());
        wmNews.setChannelId(wmNewsDto.getChannelId());
        wmNews.setLabels(wmNewsDto.getLabels());
        wmNews.setCreatedTime(new Date());
        // 判断是保存草稿还是发布
        if (saveType.equals(WmNews.Status.NORMAL.getCode())) {
            wmNews.setStatus(WmNews.Status.NORMAL.getCode());
        } else if (saveType.equals(WmNews.Status.SUBMIT.getCode())) {
            wmNews.setStatus(WmNews.Status.SUBMIT.getCode());
            wmNews.setSubmittedTime(new Date());
        }
        wmNews.setEnable(wmNews.getEnable());
        // 处理封面图片，处理文章图片
        List<String> coverImageUrls = wmNewsDto.getImages();
        String content = wmNewsDto.getContent();
        List<Map> materials = JSON.parseArray(content, Map.class);
        // 获取内容图片集合
        List<String> contentImageUrls = new ArrayList<>();
        for (Map map : materials) {
            if (map.get("type").equals(WeMediaConstants.WM_NEWS_TYPE_IMAGE)) {
                String imgUrl = (String) map.get("value");
                contentImageUrls.add(imgUrl);
            }
        }

        operateImages(coverImageUrls, contentImageUrls, wmNews, newsId, userId);


        return new ResponseResult();
    }

    private void operateImages(List<String> coverImageUrls, List<String> contentImageUrls, WmNews wmNews, Integer newsId, Integer userId) {
        // 修改图片连接
        coverImageUrls = coverImageUrls.stream().map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.replace(fileServerUrl, "");
            }
        }).collect(Collectors.toList());

        contentImageUrls = contentImageUrls.stream().map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.replace(fileServerUrl, "");
            }
        }).collect(Collectors.toList());

        // 判断保存或修改
        List<String> imageUrls = new ArrayList<>(coverImageUrls);
        imageUrls.addAll(contentImageUrls);
        String replace = imageUrls.toString().replace("[", "").replace("]", "");
        if (newsId != null) {
            // 先删除关联关系
            LambdaUpdateWrapper<WmNewsMaterial> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
            lambdaQueryWrapper.eq(WmNewsMaterial::getNewsId, newsId);
            wmNewsMaterialMapper.delete(lambdaQueryWrapper);
            wmNews.setImages(replace);
            updateById(wmNews);
            saveImages(coverImageUrls, contentImageUrls, wmNews, userId);
            coverImageUrls.addAll(contentImageUrls);

        }
        wmNews.setImages(replace);
        save(wmNews);
        saveImages(coverImageUrls, contentImageUrls, wmNews, userId);
    }

    private void saveImages(List<String> coverImageUrls, List<String> contentImageUrls, WmNews wmNews, Integer userId) {
        // 查询已保存封面图片
        LambdaUpdateWrapper<WmMaterial> lambdaQueryWrapper1 = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper1.in(WmMaterial::getUrl, coverImageUrls);
        List<WmMaterial> coverImages = wmMaterialMapper.selectList(lambdaQueryWrapper1);

        for (WmMaterial wmMaterial : coverImages) {
            // 保存文章素材关系表
            Integer wmMaterialId = wmMaterial.getId();
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setMaterialId(wmMaterialId);
            wmNewsMaterial.setNewsId(wmNews.getId());
            wmNewsMaterial.setType(WeMediaConstants.WM_COVER_REFERENCE);
            wmNewsMaterial.setOrd(null);
            wmNewsMaterialMapper.insert(wmNewsMaterial);
        }

        // 查询已保存的文章图片
        LambdaUpdateWrapper<WmMaterial> lambdaQueryWrapper2 = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper2.in(WmMaterial::getUrl, contentImageUrls);
        List<WmMaterial> contentImages = wmMaterialMapper.selectList(lambdaQueryWrapper2);

        for (WmMaterial wmMaterial : contentImages) {
            // 保存文章素材关系表
            Integer wmMaterialId = wmMaterial.getId();
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setMaterialId(wmMaterialId);
            wmNewsMaterial.setNewsId(wmNews.getId());
            wmNewsMaterial.setType(WeMediaConstants.WM_CONTENT_REFERENCE);
            wmNewsMaterial.setOrd(null);
            wmNewsMaterialMapper.insert(wmNewsMaterial);
        }
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
