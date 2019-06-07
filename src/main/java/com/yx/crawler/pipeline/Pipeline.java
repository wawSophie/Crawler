package com.yx.crawler.pipeline;

import com.yx.crawler.common.Page;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */
public interface Pipeline {
    /**
     * 管道处理数据
     * @param page
     */
    void pipeline(final Page page);

}
