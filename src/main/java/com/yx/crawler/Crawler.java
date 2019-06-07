package com.yx.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yx.crawler.common.Page;
import com.yx.crawler.parse.Parse;
import com.yx.crawler.pipeline.Pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */

/**
 * 爬虫调度框架
 */
public class Crawler {

    /**
     * 放置文档页面（详情）
     * 放置详情页面（数据）
     *
     * 未被采集和解析的页面
     * @param args
     */
    private final Queue<Page> docQueue=new LinkedBlockingDeque<>();

    /**
     * 放置详情页面
     * @param args
     */
    private final Queue<Page> detailQueue=new LinkedBlockingDeque<>();

    /**
     * 采集器不用自己写，我们用的第三方工具htmluint
     * @param args
     */
    private final WebClient webClient;

    /**
     * 一组解析器
     * @param args
     */
    private final List<Parse> parseList=new LinkedList<>();

    /**
     * 所有的清洗器
     */
    private final List<Pipeline> pipelineList=new LinkedList<>();

    /**
     * 线程执行器
     * @param args
     */
    private  ExecutorService executorService;

    //构造方法
    public Crawler(){

        this.webClient=new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);

        this.executorService=Executors.newFixedThreadPool(8, new ThreadFactory() {
            private final AtomicInteger id=new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread=new Thread(r);
                thread.setName("Crawler-Thread-"+id.getAndIncrement());
                return thread;
            }
        });

    }

    public void start(){
        //爬取
        //解析
        //清洗
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        });
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                pipeline();
            }
        });
    }

    private void parse(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Page page=this.docQueue.poll();
            if (page==null){
                continue;
            }
            //单线程变多线程
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //有Page
                    try {
                        HtmlPage htmlPage=Crawler.this.webClient.getPage(page.getUrl());
                        page.setHtmlPage(htmlPage);
                        for (Parse parse:parseList){
                            parse.parse(page);
                        }
                        if(page.isDetail()){
                            Crawler.this.detailQueue.add(page);
                        }else {
                            Iterator<Page> iterator=page.getSubPage().iterator();
                            while (iterator.hasNext()){
                                Page subPage=iterator.next();
                                Crawler.this.docQueue.add(subPage);//再加回去
                                iterator.remove();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        }
    private void pipeline(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Page page=this.detailQueue.poll();
            if (page==null){
                continue;
            }
            //调度器，单线程变多线程
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (Pipeline pipeline:Crawler.this.pipelineList){
                        pipeline.pipeline(page);
                    }
                }
            });
        }
    }
    public void addPage(Page page){
        this.docQueue.add(page);
    }
    public void addParse(Parse parse){
        this.parseList.add(parse);
    }

    public void addPipeline(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }

    public void stop(){
        if (this.executorService!=null && !this.executorService.isShutdown()){
            this.executorService.shutdown();
        }
    }

}
