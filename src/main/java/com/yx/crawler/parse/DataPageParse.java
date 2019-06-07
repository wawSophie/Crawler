package com.yx.crawler.parse;

import com.gargoylesoftware.htmlunit.html.*;
import com.yx.crawler.common.Page;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */
public class DataPageParse implements Parse {
    public void parse(final Page page) {
        if (!page.isDetail()) {
            return;
        }

        HtmlPage htmlPage = page.getHtmlPage();
        HtmlElement body = htmlPage.getBody();
        //标题
        String titlePath = "//div[@class='cont']/h1/text()";
        DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
        String title = titleDom.asText();
//        System.out.println(title);
        //朝代
        String dynastyPath = "//div[@class='cont']/p/a[1]";
        HtmlAnchor anchor = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = anchor.asText();
//        System.out.println(dynasty);

        //作者
        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();
//        System.out.println(author);

        //内容
        String contentPath = "//div[@class='cont']//div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();
//        System.out.println(content);


        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);

        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);


        //由清洗器去决定是否要加元素
        //更多的数据
        page.getDataSet().putData("url",page.getUrl());
    }
}
