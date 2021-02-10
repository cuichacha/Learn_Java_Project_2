package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.wemedia.WeMediaConstants;
import com.heima.common.message.NewsAutoScanConstants;
import com.heima.model.common.admin.dtos.NewsAuthDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.dtos.WmNewsDto;
import com.heima.model.common.wemedia.dtos.WmNewsPageDto;
import com.heima.model.common.wemedia.pojos.WmMaterial;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.pojos.WmNewsMaterial;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.model.common.wemedia.vo.WmNewsVo;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private WmUserService wmUserService;

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
    public PageResponseResult findWmNewsList(NewsAuthDto newsAuthDto) {
        // 校验参数
        if (newsAuthDto == null) {
            return (PageResponseResult) ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查分页
        newsAuthDto.checkParam();
        // 设置分页
        newsAuthDto.setPage((newsAuthDto.getPage() - 1) * newsAuthDto.getSize());
        // 模糊查询
        if (newsAuthDto.getTitle() != null) {
            newsAuthDto.setTitle("%" + newsAuthDto.getTitle() + "%");
        }
        // 获取数据记录数
        Integer total = wmNewsMapper.findWmNewsListCount(newsAuthDto);
        Integer startPage = newsAuthDto.getPage();
        Integer pageSize = newsAuthDto.getSize();
        // 获取数据集合
        List<WmNewsVo> wmNewsVoList = wmNewsMapper.findWmNewsList(newsAuthDto);
        // 返回数据
        PageResponseResult responseResult = new PageResponseResult(startPage, pageSize, total);
        responseResult.setData(wmNewsVoList);
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
        }
        // 审核意见设为空
        wmNews.setReason(null);
        wmNews.setSubmittedTime(new Date());
        wmNews.setPublishTime(wmNewsDto.getPublishTime());
        wmNews.setEnable(wmNews.getEnable());
        // 获得封面图片集合，获得文章图片集合
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
        // 处理封面图片与文章图片
        operateImageUrlsAndSaveWmNews(coverImageUrls, contentImageUrls, wmNews, newsId, userId);
        // 返回结果
        return new ResponseResult();
    }

    private void operateImageUrlsAndSaveWmNews(List<String> coverImageUrls, List<String> contentImageUrls, WmNews wmNews, Integer newsId, Integer userId) {
        // 修改封面图片连接
        coverImageUrls = coverImageUrls.stream().map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.replace(fileServerUrl, "");
            }
        }).collect(Collectors.toList());
        // 修改内容图片连接
        contentImageUrls = contentImageUrls.stream().map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.replace(fileServerUrl, "");
            }
        }).collect(Collectors.toList());

        // 合并两个链接并修改链接字符串
        List<String> imageUrls = new ArrayList<>(coverImageUrls);
        imageUrls.addAll(contentImageUrls);
        String replace = imageUrls.toString().replace("[", "").replace("]", "").replace(" ", "");
        // 判断是修改还是保存，二者只能执行一个！！
        if (newsId != null) {
            // 先删除关联关系
            LambdaQueryWrapper<WmNewsMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(WmNewsMaterial::getNewsId, newsId);
            wmNewsMaterialMapper.delete(lambdaQueryWrapper);
            // 把图片链接存入表中
            wmNews.setImages(replace);
            // 设置对象id
            wmNews.setId(newsId);
            // 更新表记录数据
            updateById(wmNews);
            // 新增关系表记录
            // 没有图片，传入空集合
            List<String> emptyList = new ArrayList<>();
            if (coverImageUrls.isEmpty() && !contentImageUrls.isEmpty()) {
                saveImagesMaterialRelation(emptyList, contentImageUrls, wmNews);
            } else if (!coverImageUrls.isEmpty() && contentImageUrls.isEmpty()) {
                saveImagesMaterialRelation(coverImageUrls, emptyList, wmNews);
            } else {
                saveImagesMaterialRelation(coverImageUrls, contentImageUrls, wmNews);
            }
        } else {
            // 把图片链接存入表中
            wmNews.setImages(replace);
            // 保存数据
            save(wmNews);
            // 没有图片，传入空集合
            List<String> emptyList = new ArrayList<>();
            if (coverImageUrls.isEmpty() && !contentImageUrls.isEmpty()) {
                saveImagesMaterialRelation(emptyList, contentImageUrls, wmNews);
            } else if (!coverImageUrls.isEmpty() && contentImageUrls.isEmpty()) {
                saveImagesMaterialRelation(coverImageUrls, emptyList, wmNews);
            } else {
                saveImagesMaterialRelation(coverImageUrls, contentImageUrls, wmNews);
            }
        }
        if (!wmNews.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            // 发送Kafka消息
            kafkaTemplate.send(NewsAutoScanConstants.WM_NEWS_CENSORSHIP_TOPIC, JSON.toJSONString(wmNews.getId()));
        }
    }

    private void saveImagesMaterialRelation(List<String> coverImageUrls, List<String> contentImageUrls, WmNews wmNews) {
        // 查询已保存封面图片
        // 增加无图情况判断
        List<WmMaterial> coverImages = new ArrayList<>();
        if (!coverImageUrls.isEmpty()) {
            LambdaUpdateWrapper<WmMaterial> lambdaQueryWrapper1 = new LambdaUpdateWrapper<>();
            lambdaQueryWrapper1.in(WmMaterial::getUrl, coverImageUrls);
            coverImages = wmMaterialMapper.selectList(lambdaQueryWrapper1);
        }

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
        // 增加无图情况判断
        List<WmMaterial> contentImages = new ArrayList<>();
        if (!contentImageUrls.isEmpty()) {
            LambdaUpdateWrapper<WmMaterial> lambdaQueryWrapper2 = new LambdaUpdateWrapper<>();
            lambdaQueryWrapper2.in(WmMaterial::getUrl, contentImageUrls);
            contentImages = wmMaterialMapper.selectList(lambdaQueryWrapper2);
        }

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
        // 检查数据
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        // 返回数据
        ResponseResult responseResult = ResponseResult.okResult(wmNews);
        responseResult.setHost(fileServerUrl);
        return responseResult;
    }

    @Override
    public ResponseResult findNewsVoById(Integer id) {
        // 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询文章
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        // 查询作者
        WmUser wmUser = wmUserService.getById(wmNews.getUserId());
        if (wmUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "作者不存在");
        }
        // 封装VO对象
        WmNewsVo wmNewsVo = new WmNewsVo();
        BeanUtils.copyProperties(wmNews, wmNewsVo);
        ResponseResult responseResult = ResponseResult.okResult(wmNewsVo);
        responseResult.setHost(fileServerUrl);
        return responseResult;
    }

    @Override
    public ResponseResult findById(Integer id) {
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String jsonString = JSON.toJSONString(wmNews);
        return ResponseResult.okResult(jsonString);
    }

    @Override
    public ResponseResult deleteNews(Integer id) {
        // 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查数据
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        // 已发布不能删除
        if (wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode()) && wmNews.getEnable().equals(WeMediaConstants.WM_NEWS_ENABLE_UP)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章已发布，不能删除");
        }
        // 删除关联信息
        LambdaQueryWrapper<WmNewsMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmNewsMaterial::getNewsId, wmNews.getId());
        wmNewsMaterialMapper.delete(lambdaQueryWrapper);
        // 发送Kafka消息
        WmNews byId = getById(id);
        kafkaTemplate.send(NewsAutoScanConstants.WM_NEWS_DELETE_TOPIC, JSON.toJSONString(byId.getArticleId()));
        // 删除数据
        boolean result = removeById(id);
        if (result) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult publishOrWithdrawNews(WmNewsDto wmNewsDto) {
        // 检查参数
        if (wmNewsDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 检查数据
        WmNews wmNews = getById(wmNewsDto.getId());
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章不存在");
        }
        // 非发布状态不能上下架
        if (!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "当前文章不是发布状态，不能上下架");
        }
        // 修改状态
        if (wmNewsDto.getEnable() != null && wmNewsDto.getEnable() > -1 && wmNewsDto.getEnable() < 2) {
            LambdaUpdateWrapper<WmNews> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(WmNews::getId, wmNewsDto.getId()).set(WmNews::getEnable, wmNewsDto.getEnable());
            boolean result = update(updateWrapper);
            if (result) {
                return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
            }
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
