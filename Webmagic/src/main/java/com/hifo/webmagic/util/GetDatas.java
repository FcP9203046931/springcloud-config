package com.hifo.webmagic.util;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GetDatas implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

    @Override
    public void process(Page page) {
        System.out.println(">>>>>>>>>>>>>>>>>");
        List<Html> html = new ArrayList<>();
        html.add(page.getHtml());
        List<String> list = page.getHtml().xpath("//div[@class='title']/a/@href").all();
        List<String> lists = page.getHtml().xpath("//div[@class='page-box house-lst-page-box']/a/@href").all();
        page.addTargetRequests(page.getHtml().xpath("//div[@class='page-box house-lst-page-box']/a/@href").all());
        page.addTargetRequests(page.getHtml().xpath("//div[@class='title']/a/@href").all());
        System.out.println(lists);
        System.out.println("<<<<<<<<<<<<<<<<<");
    }

    @Override
    public Site getSite() {
        return site;
    }
    @RequestMapping("/start")
    public void start(){
        System.out.println("++++++++++++++++");
        System.setProperty("selenuim_config", "D://config.ini");
        Downloader downloader = new SeleniumDownloader("D:\\chromedriver.exe");
        downloader.setThread(10);
        Spider.create(new GetDatas()).
                setDownloader(downloader).addUrl("https://cq.lianjia.com/xiaoqu/?from=rec").thread(5).run();
    }
}
