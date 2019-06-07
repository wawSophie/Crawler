package com.yx.crawler.pipeline;

import com.yx.crawler.common.Page;

import java.util.Map;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */
public class ConsolePipeline implements Pipeline {
    @Override
    public void pipeline(Page page) {
        Map<String,Object> data=page.getDataSet().getData();
        //存储
        System.out.println(data);
    }
}
