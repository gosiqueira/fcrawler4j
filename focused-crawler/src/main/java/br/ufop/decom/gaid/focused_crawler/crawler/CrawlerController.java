package br.ufop.decom.gaid.focused_crawler.crawler;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufop.decom.gaid.focused_crawler.seed.SeedBuilder;
import br.ufop.decom.gaid.focused_crawler.seed.search.GoogleAjaxSearch;
import br.ufop.decom.gaid.focused_crawler.similarity.CosineSimilarity;
import br.ufop.decom.gaid.focused_crawler.threshold.summarization.ArithmeticMean;
import br.ufop.decom.gaid.focused_crawler.util.Loader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerController {

	private static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);

	/**
	 * Files extensions that must be ignored by the crawler.
	 */
	public static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4"
			+ "doc|docx|xsd|ppt|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static CrawlerController instance;
	public static double threshold;

	private Loader loader = Loader.getInstace();

	private PageFetcher pageFetcher;
	private RobotstxtConfig robotstxtConfig;
	private RobotstxtServer robotstxtServer;
	private CrawlController controller;

	private int numberOfCrawlers;

	private List<String> genreTerms;
	private List<String> contentTerms;

	public static CrawlerController getInstance() {
		if (instance == null) {
			instance = new CrawlerController();
		}

		return instance;
	}

	private CrawlerController() {
	}

	public void init() throws Exception {

		logger.info("Initializing crawler's configuration...");

		/*
		 * Initializing genre and content terms for future purposes
		 */
		loader.init();
		genreTerms = loader.loadGenreTerms();
		contentTerms = loader.loadContentTerms();

		/*
		 * processStorageFolder is a folder where intermediate crawl data is
		 * stored. crawlerStorageFolder is a folder where visited pages are
		 * stored. relevantsStorageFolder is a folder where relevant, according
		 * to desired topic, crawl data is stored.
		 */
		// TODO make storage folder be read from config file.
		String processStorageFolder = "./processProperties";
		String crawlerStorageFolder = "./crawledPages";
		String relevantsStorageFolder = "./relevantPages";

		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		numberOfCrawlers = 4;

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(processStorageFolder);
		new File(crawlerStorageFolder).mkdirs();
		new File(relevantsStorageFolder).mkdirs();

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(1000);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		config.setMaxDepthOfCrawling(15);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		config.setMaxPagesToFetch(100);

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		pageFetcher = new PageFetcher(config);
		robotstxtConfig = new RobotstxtConfig();
		robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		controller = new CrawlController(config, pageFetcher, robotstxtServer);

		SeedBuilder builder = new SeedBuilder(new GoogleAjaxSearch(), 15);
		List<WebURL> seeds = builder.build();
		for (WebURL seed : seeds) {
			controller.addSeed(seed.getURL());
		}

		/*
		 * Defines similarity threshold for focused crawling process.
		 */
		// TODO check a better pattern for different threshold implementations, maybe factory method.
		threshold = new ArithmeticMean(new CosineSimilarity(genreTerms, contentTerms, 0.7, 0.3), seeds).getThreshold();
		logger.info("Similarity threshold were defined as " + threshold);
		logger.info("Starting crawl process.");

	}

	public void run() {
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(CrawlerWorker.class, numberOfCrawlers);
		
		logger.info("Crawl process finished.");
	}

}
