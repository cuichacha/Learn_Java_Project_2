package com.heima.admin.job;

import com.heima.admin.feign.WeMediaFeign;
import com.heima.admin.service.WmNewsCensorshipService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cuichacha
 * @date 2/12/21 13:51
 */
@Component
@Log4j2
public class WeMediaNewsAutoScanJob {

    @Autowired
    private WmNewsCensorshipService wmNewsCensorshipService;

    @Autowired
    private WeMediaFeign weMediaFeign;

    @XxlJob("autoscan")
    public ReturnT<String> hello(String param) throws Exception {
        log.info("自媒体文章审核调度任务开始执行....");
        List<Integer> releaseIdList = weMediaFeign.findRelease();
        if(null!=releaseIdList && !releaseIdList.isEmpty()){
            for (Integer id : releaseIdList) {
                wmNewsCensorshipService.censorByWmNewsId(id);
            }
        }
        log.info("自媒体文章审核调度任务执行结束....");
        return ReturnT.SUCCESS;
    }
}
