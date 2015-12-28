package br.ufop.decom.gaid.focused_crawler.integration;

import br.ufop.decom.gaid.focused_crawler.crawler.CrawlerController;

public class FocusedCrawlerProcessTest {

	public static void main(String[] args) throws Exception {
		CrawlerController controller = CrawlerController.getInstance();
		controller.init();
		controller.run();
	}

}
