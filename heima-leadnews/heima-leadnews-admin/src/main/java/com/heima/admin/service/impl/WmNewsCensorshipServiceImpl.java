package com.heima.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.admin.feign.ArticleFeign;
import com.heima.admin.feign.WeMediaFeign;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.admin.service.WmNewsCensorshipService;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.constants.aliyun.AliyunConstants;
import com.heima.model.common.admin.pojos.AdChannel;
import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cuichacha
 * @date 1/31/21 13:49
 */
@Service
@Log4j2
public class WmNewsCensorshipServiceImpl implements WmNewsCensorshipService {

    @Autowired
    private WeMediaFeign weMediaFeign;

    @Autowired
    private AdChannelService adChannelService;

    @Autowired
    private ArticleFeign articleFeign;

    @Autowired
    private AdSensitiveMapper adSensitiveMapper;

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    private Long articleId;

    private Boolean isNewArticle;

    @Override
    @GlobalTransactional
    public void censorByWmNewsId(Integer id) {
        // 校验参数
        if (id == null) {
            log.error("当前的审核id空");
            throw new RuntimeException("当前的审核id空");
//            return;
        }
        // 远程调用获取文章对象
        ResponseResult wmNewsByFeign = weMediaFeign.findById(id);
//        WmNews wmNews = weMediaFeign.findById(id);
        if (!wmNewsByFeign.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("审核的自媒体文章不存在，自媒体的id:{}", id);
            throw new RuntimeException("审核的自媒体文章不存在");
//            return;
        }
        Object wmNewsByFeignData = wmNewsByFeign.getData();
        WmNews wmNews = JSON.parseObject(wmNewsByFeignData.toString(), WmNews.class);

        // 先获取articleId
        articleId = wmNews.getArticleId();
        // 判断是增加新文章还是修改旧文章
        isNewArticle = articleId == null;
        // 文章状态
        Short status = wmNews.getStatus();

        // 文章状态为4，人工审核通过
        if (status.equals(WmNews.Status.ADMIN_SUCCESS.getCode())) {
            // 保存数据
            saveData(wmNews, isNewArticle);
            // 修改文章状态
            updateWmNews(wmNews, WmNews.Status.PUBLISHED.getCode(), "审核通过");
            // 结束方法，不再向后执行
            return;
        }

        // 文章状态为8，审核通过（待发布）
        if (status.equals(WmNews.Status.SUCCESS.getCode())) {
            // 判断文章发布时间
            if (wmNews.getPublishTime().getTime() > System.currentTimeMillis()) {
                // 大于当前时间，直接保存
                saveData(wmNews, isNewArticle);
            } else {
                // 保存数据
                saveData(wmNews, isNewArticle);
                // 小于当前时间，修改文章状态，改为已发布
                updateWmNews(wmNews, WmNews.Status.PUBLISHED.getCode(), "审核通过");
            }
            return;
        }

        // 文章状态为9，什么也不做
        if (status.equals(WmNews.Status.PUBLISHED.getCode())) {
            return;
        }

        // 文章状态不是4，8，9，检查内容
        // 获取文本内容
        String text = parseText(wmNews);
        if (StringUtils.isNotEmpty(text)) {
            // 审核文本内容
            Map textMap = null;
            try {
                textMap = greenTextScan.greenTextScan(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (textMap == null) {
                log.error("自动审核文章文本内容失败");
                throw new RuntimeException("自动审核文章文本内容失败");
            }
            if (!textMap.get("suggestion").equals(AliyunConstants.PASS)) {
                // 自动审核不通过
                if (textMap.get("suggestion").equals(AliyunConstants.BLOCK)) {
                    updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), "文章内容中有敏感词汇");
                    return;
                }
                if (textMap.get("suggestion").equals(AliyunConstants.REVIEW)) {
                    // 转人工审核
                    updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), "文章内容中有不确定词汇");
                    return;
                }
            }
            // 审核自管理敏感词
            List<String> allSensitive = adSensitiveMapper.findAllSensitive();
            SensitiveWordUtil.initMap(allSensitive);
            Map<String, Integer> matchWords = SensitiveWordUtil.matchWords(text);
            if (matchWords.size() > 0) {
                updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), "文章内容中有敏感词汇");
                return;
            }
        }

        // 获取图片内容
        String images = wmNews.getImages();
        if (StringUtils.isNotEmpty(images)) {
            String[] split = images.split(",");
            List<String> imageUrls = Arrays.stream(split).collect(Collectors.toList());
            imageUrls = imageUrls.stream().map(new Function<String, String>() {
                @Override
                public String apply(String s) {
                    s = "http://372j58p076.qicp.vip/" + s;
                    return s;
                }
            }).collect(Collectors.toList());

            // 审核图片
            Map imagesMap = null;
            try {
                imagesMap = greenImageScan.imageScan(imageUrls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (imagesMap == null) {
                log.error("自动审核文章图片内容失败");
                throw new RuntimeException("自动审核文章图片内容失败");
            }
            if (!imagesMap.get("suggestion").equals(AliyunConstants.PASS)) {
                // 自动审核不通过
                if (imagesMap.get("suggestion").equals(AliyunConstants.BLOCK)) {
                    updateWmNews(wmNews, WmNews.Status.FAIL.getCode(), "文章内容中有敏感图片");
                    return;
                }
                if (imagesMap.get("suggestion").equals(AliyunConstants.REVIEW)) {
                    // 转人工审核
                    updateWmNews(wmNews, WmNews.Status.ADMIN_AUTH.getCode(), "文章内容中有不确定图片");
                    return;
                }
            }
        }

        // 保存或修改数据
        saveData(wmNews, isNewArticle);

        Date publishTime = wmNews.getPublishTime();
        if (publishTime == null || publishTime.getTime() <= System.currentTimeMillis()) {
            updateWmNews(wmNews, WmNews.Status.PUBLISHED.getCode(), "审核通过");
        } else {
            updateWmNews(wmNews, WmNews.Status.SUCCESS.getCode(), "审核通过");
        }
    }

    private void saveData(WmNews wmNews, Boolean isNewArticle) {
        // 向article表中插入数据
        saveApArticle(wmNews, isNewArticle);
        // 向article_content表中插入数据
        saveApArticleContent(wmNews, articleId, isNewArticle);
        // 向article_config表中插入数据
        saveApArticleConfig(articleId, isNewArticle);
    }

    private void saveApArticle(WmNews wmNews, Boolean isNewArticle) {
        ApArticle apArticle = new ApArticle();
        // apArticle.setId(wmNews.getArticleId());
        apArticle.setTitle(wmNews.getTitle());
        // 查询wmUser
        Integer userId = wmNews.getUserId();
        ResponseResult wmUserByFeign = weMediaFeign.findWmUserById(userId);
//        WmUser wmUser = (WmUser) wmUserById.getData();
        if (!wmUserByFeign.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程调用查询WmUser对象发生异常");
            throw new RuntimeException("远程调用查询WmUser对象发生异常");
        }
        Object wmUserByFeignData = wmUserByFeign.getData();
        WmUser wmUser = JSON.parseObject(wmUserByFeignData.toString(), WmUser.class);
        // 保存AuthorID
        Integer apAuthorId = wmUser.getApAuthorId();
        apArticle.setAuthorId(apAuthorId);
        apArticle.setAuthorName(wmUser.getName());
        // 查询频道
        Integer channelId = wmNews.getChannelId();
        apArticle.setChannelId(channelId);
        AdChannel adChannel = adChannelService.getById(channelId);
        if (adChannel == null) {
            log.error("未查询到AdChannel对象");
            throw new RuntimeException("未查询到AdChannel对象");
        }
        apArticle.setChannelName(adChannel.getName());
        apArticle.setLayout(wmNews.getType());
        apArticle.setImages(wmNews.getImages());
        apArticle.setCreatedTime(wmNews.getCreatedTime());
        apArticle.setPublishTime(wmNews.getPublishTime());
        // 保存文章到数据库
        if (isNewArticle) {
            // 新文章就添加
            ResponseResult saveApArticleResult = articleFeign.saveApArticle(apArticle);
            if (!saveApArticleResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
                log.error("远程调用保存ApArticle对象发生异常");
                throw new RuntimeException("远程调用保存ApArticle对象发生异常");
            }
            // 取ArticleID
            articleId = Long.parseLong(saveApArticleResult.getErrorMessage());
        } else {
            // 旧文章需要更新
            apArticle.setId(articleId);
            ResponseResult updateApArticleResult = articleFeign.updateApArticle(apArticle);
            if (!updateApArticleResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
                log.error("远程调用更新ApArticle对象发生异常");
                throw new RuntimeException("远程调用更新ApArticle对象发生异常");
            }
        }
    }

    private void saveApArticleContent(WmNews wmNews, Long articleId, Boolean isNewArticle) {
        ApArticleContent apArticleContent = new ApArticleContent();
//        apArticleContent.setArticleId(wmNews.getArticleId());
        apArticleContent.setArticleId(articleId);
        apArticleContent.setContent(wmNews.getContent());
        if (isNewArticle) {
            // 新文章就添加
            ResponseResult saveArticleContentResult = articleFeign.saveArticleContent(apArticleContent);
            if (!saveArticleContentResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
                log.error("远程调用保存ApArticleContent对象发生异常");
                throw new RuntimeException("远程调用保存ApArticleContent对象发生异常");
            }
        } else {
            // 旧文章需要更新
            ResponseResult updateArticleContentResult = articleFeign.updateArticleContent(apArticleContent);
            if (!updateArticleContentResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
                log.error("远程调用更新apArticleContent对象发生异常");
                throw new RuntimeException("远程调用更新apArticleContent对象发生异常");
            }
        }

    }

    private void saveApArticleConfig(Long articleId, Boolean isNewArticle) {
        ApArticleConfig apArticleConfig = new ApArticleConfig();
//        apArticleConfig.setArticleId(wmNews.getArticleId());
        apArticleConfig.setArticleId(articleId);
        apArticleConfig.setIsForward(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(true);
        apArticleConfig.setIsComment(true);
        if (isNewArticle) {
            ResponseResult saveArticleConfigResult = articleFeign.saveArticleConfig(apArticleConfig);
            if (!saveArticleConfigResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
                log.error("远程调用保存ApArticleConfig对象发生异常");
                throw new RuntimeException("远程调用保存ApArticleConfig对象发生异常");
            }
        } else {
            // 旧文章需要更新
            ResponseResult updateArticleConfigResult = articleFeign.updateArticleConfig(apArticleConfig);
            if (!updateArticleConfigResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
                log.error("远程调用更新ApArticleConfig对象发生异常");
                throw new RuntimeException("远程调用更新ApArticleConfig对象发生异常");
            }
        }
    }

    private void updateWmNews(WmNews wmNews, Short code, String msg) {
        wmNews.setStatus(code);
        wmNews.setReason(msg);
        wmNews.setArticleId(articleId);
        if (wmNews.getPublishTime() == null) {
            wmNews.setPublishTime(new Date());
        }
        ResponseResult updateWmNewsResult = weMediaFeign.updateWmNews(wmNews);
        if (!updateWmNewsResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程更新文章状态发生异常");
            throw new RuntimeException("远程更新文章状态发生异常");
        }
    }

    private String parseText(WmNews wmNews) {
        // 获取文本集合
        StringBuilder stringBuilder = new StringBuilder();
        String content = wmNews.getContent();
        List<Map> list = JSON.parseArray(content, Map.class);
        for (Map map : list) {
            if ("text".equals(map.get("type"))) {
                String text = (String) map.get("value");
                stringBuilder.append(text);
            }
        }
        return stringBuilder.toString();
    }
}
