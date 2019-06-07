package com.yx.crawler.parse;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yx.crawler.common.Page;

import java.util.List;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */
public class DocumentParse implements Parse {

    public void parse(final Page page) {
        if (page.isDetail()) {
            return;
        }
        //原子执行
//        final AtomicInteger count=new AtomicInteger(0);
        HtmlPage htmlPage = page.getHtmlPage();
        List<HtmlElement> htmlElement = htmlPage.getBody()
                .getElementsByAttribute("div", "class", "typecont");
        for (HtmlElement htmlElements : htmlElement) {
//            System.out.println(htmlElements.asText());
            HtmlDivision htmlDivision = (HtmlDivision) htmlElements;
            DomNodeList<HtmlElement> domNodeList = htmlDivision.getElementsByTagName("a");
            for (HtmlElement domNodeLists : domNodeList) {
//                System.out.println(domNodeLists.asXml());
                String path = domNodeLists.getAttribute("href");
                Page subPage = new Page(page.getBase(), path, true);
                page.getSubPage().add(subPage);
            }
        }

    }
}