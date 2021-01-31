package com.heima.admin.kafka.listener;

import com.heima.admin.feign.WeMediaFeign;
import com.heima.admin.service.WmNewsCensorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cuichacha
 * @date 1/31/21 13:37
 */
@Component
public class WeMediaNewsCensorshipListener {

    @Autowired
    private WmNewsCensorshipService wmNewsCensorshipService;
}
