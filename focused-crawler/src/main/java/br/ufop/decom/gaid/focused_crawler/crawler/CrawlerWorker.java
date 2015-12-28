package br.ufop.decom.gaid.focused_crawler.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.ufop.decom.gaid.focused_crawler.similarity.CosineSimilarity;
import br.ufop.decom.gaid.focused_crawler.similarity.SimilarityMetric;
import br.ufop.decom.gaid.focused_crawler.storer.Item;
import br.ufop.decom.gaid.focused_crawler.storer.Storer;
import br.ufop.decom.gaid.focused_crawler.util.Loader;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerWorker extends WebCrawler {

	private Loader loader = Loader.getInstace();

	private SimilarityMetric similarityMetric;

	private List<String> genreTerms;
	private List<String> contentTerms;
	private List<String> urlTerms;

	@Override
	public void onStart() {
		synchronized (loader) {
			if(!loader.isInitialized()) {
				loader.init();
			}
			this.genreTerms = loader.loadGenreTerms();
			this.contentTerms = loader.loadContentTerms();
			this.urlTerms = new ArrayList<>();
			urlTerms.addAll(genreTerms);
			urlTerms.addAll(contentTerms);
		}

		this.similarityMetric = new CosineSimilarity(genreTerms, contentTerms, urlTerms, 0.7, 0.3, 0.8, 0.2,
				CrawlerController.threshold);
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (CrawlerController.FILTERS.matcher(href).matches()) {
			return false;
		}

		return true;
	}

	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		int parentDocid = page.getWebURL().getParentDocid();

		logger.info("Visiting page {}", url);
		logger.info("Page's priority {}", page.getWebURL().getPriority());

		logger.debug("Docid: {}", docid);
		logger.debug("Docid of parent page: {}", parentDocid);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String title = htmlParseData.getTitle();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			logger.info("Page's title: {}", title);
			logger.debug("Text length: {}", text.length());
			logger.debug("Html length: {}", html.length());
			logger.debug("Number of outgoing links: {}", links.size());

			Item currentPage = new Item(title, url, html);
			
			new Storer("./crawledPages/" + docid + ".collected").store(currentPage);
			
			double pageSimilarity = 0.0;

			try {
				pageSimilarity = similarityMetric.similarity(text, url);
			} catch (Exception e) {
				e.printStackTrace();
			}

			logger.info("Page's similarity:" + pageSimilarity);
			
			if (similarityMetric.isSimilar(pageSimilarity)) {
				// TODO save relevant page using producer-consumer pattern
				new Storer("./relevantPages/" + docid + ".collected").store(currentPage);
			}
		}
	}
}
