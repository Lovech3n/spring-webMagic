package me.shota.wm;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Hello world!
 *
 */
public class App implements PageProcessor{
	private static final String start = "http://qq.yh31.com/zjbq/0551964.html";
	private static final String path = "C:\\\\Users\\\\macbookpro\\\\Desktop\\\\wm_down";
	private String baseUrl = "http://qq.yh31.com";
	
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	
	public Site getSite() {
		return site;
	}

	public void process(Page page) {
		List<String> linkss = page.getHtml().xpath("//div[@id='fontzoom']/p/img/@src").all();
		for(int i=0; i<linkss.size(); i++) {
			linkss.set(i, baseUrl + linkss.get(i));
		}
		
		page.putField("link", linkss);
		
		List<String> links = page.getHtml().xpath("//span[@id='pe100_page_contentpage']/a/@href").all();
		for(int i=0; i<links.size(); i++) {
			System.out.println(links.get(i));
			links.set(i, baseUrl + links.get(i));
		}
		if(links != null && links.size()>0) {
			page.addTargetRequests(links);
		}
	}
	
	public static void main( String[] args )
	{
		Spider.create(new App())
				.addUrl(start)
				.addPipeline(new PipelineImpl(path))
				.thread(10)
				.run();
	}
}
