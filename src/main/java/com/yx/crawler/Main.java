package com.yx.crawler;

import com.alibaba.druid.pool.DruidDataSource;
import com.yx.crawler.common.Page;
import com.yx.crawler.parse.DataPageParse;
import com.yx.crawler.parse.DocumentParse;
import com.yx.crawler.pipeline.DatabasePipeline;


/**
 * Author:Sophie
 * Created: 2019/6/6
 */
public class Main {
    public static void main(String[] args) {
        final Page page=new Page("https://so.gushiwen.org","/gushi/tangshi.aspx",false);

        Crawler crawler=new Crawler();

        crawler.addParse(new DocumentParse());
        crawler.addParse(new DataPageParse());
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUsername("***");//加入你的数据库的用户名
        dataSource.setPassword("***");//加入你数据库的密码
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/tangshi");//按这个格式，加入你的数据库表
        crawler.addPipeline(new DatabasePipeline(dataSource));
//       crawler.addPipeline(new ConsolePipeline());
        crawler.addPage(page);
        crawler.start();

    }
}
