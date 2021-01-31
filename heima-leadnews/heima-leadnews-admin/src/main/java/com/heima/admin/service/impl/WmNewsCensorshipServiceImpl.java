package com.heima.admin.service.impl;

import com.heima.admin.feign.ArticleFeign;
import com.heima.admin.feign.WeMediaFeign;
import com.heima.admin.service.AdChannelService;
import com.heima.admin.service.WmNewsCensorshipService;
import com.heima.common.constants.wemedia.WeMediaConstants;
import com.heima.model.common.admin.pojos.AdChannel;
import com.heima.model.common.article.pojos.ApArticle;
import com.heima.model.common.article.pojos.ApArticleConfig;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.common.wemedia.pojos.WmNews;
import com.heima.model.common.wemedia.pojos.WmUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public void censorByWmNewsId(Integer id) {
        // 校验参数
        if (id == null) {
            log.error("当前的审核id空");
            throw new RuntimeException("当前的审核id空");
//            return;
        }
        // 远程调用获取文章对象
        ResponseResult wmNewsByFeign = weMediaFeign.findById(id);
        WmNews wmNews = (WmNews) wmNewsByFeign.getData();
        if (wmNews == null) {
            log.error("审核的自媒体文章不存在，自媒体的id:{}", id);
            throw new RuntimeException("审核的自媒体文章不存在");
//            return;
        }
        // 文章状态
        Short status = wmNews.getStatus();
        // 文章状态为4，人工审核通过
        if (status.equals(WmNews.Status.ADMIN_SUCCESS.getCode())) {
            // 保存数据
            saveData(wmNews);
            // 修改文章状态
            wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
            updateWmNews(wmNews, WmNews.Status.PUBLISHED.getCode(), "审核通过");
            // 结束方法，不再向后执行
            return;
        }



    }

    private void saveData(WmNews wmNews) {
        // 向article表中插入数据
        saveApArticle(wmNews);
        // 向article_config表中插入数据
        saveApArticleConfig(wmNews);
        // 向article_content表中插入数据
        saveApArticleContent(wmNews);
    }

    private void saveApArticle(WmNews wmNews) {
        ApArticle apArticle = new ApArticle();
        apArticle.setId(wmNews.getArticleId());
        apArticle.setTitle(wmNews.getTitle());
        // 查询wmUser
        Integer userId = wmNews.getUserId();
        ResponseResult wmUserById = weMediaFeign.findWmUserById(userId);
        WmUser wmUser = (WmUser) wmUserById.getData();
        if (wmUser == null) {
            log.error("远程调用查询WmUser对象发生异常");
            throw new RuntimeException("远程调用查询WmUser对象发生异常");
        }
        apArticle.setAuthorId(wmUser.getApAuthorId());
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
        ResponseResult saveApArticleResult = articleFeign.saveApArticle(apArticle);
        if (!saveApArticleResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程调用保存ApArticle对象发生异常");
            throw new RuntimeException("远程调用保存ApArticle对象发生异常");
        }
    }

    private void saveApArticleConfig(WmNews wmNews) {
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(wmNews.getArticleId());
        apArticleConfig.setIsForward(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(true);
        apArticleConfig.setIsComment(true);
        ResponseResult saveArticleConfigResult = articleFeign.saveArticleConfig(apArticleConfig);
    }

    private void saveApArticleContent(WmNews wmNews) {

    }

    private void updateWmNews(WmNews wmNews, Short code, String msg) {
        wmNews.setStatus(code);
        wmNews.setReason(msg);
        ResponseResult updateWmNewsResult = weMediaFeign.updateWmNews(wmNews);
        if (!updateWmNewsResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            log.error("远程更新文章状态发生异常");
            throw new RuntimeException("远程更新文章状态发生异常");
        }
    }
}
