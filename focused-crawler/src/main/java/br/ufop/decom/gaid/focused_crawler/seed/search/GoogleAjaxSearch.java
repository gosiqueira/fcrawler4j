package br.ufop.decom.gaid.focused_crawler.seed.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufop.decom.gaid.focused_crawler.crawler.CrawlerController;
import br.ufop.decom.gaid.focused_crawler.seed.Seed;

public class GoogleAjaxSearch implements Search {

	Logger logger = LoggerFactory.getLogger(GoogleAjaxSearch.class);

	private static final String SEARCH_URL = "http://ajax.googleapis.com/ajax/services/search/web?&v=1.0&rsz=large&q=";
	private static final String FILE_TYPE = "&fileType="; /*
															 * Give preference to
															 * html fileType
															 */
	private static final String START = "&start=";

	private QueryBuilder queryBuilder;

	public GoogleAjaxSearch() {
		this.queryBuilder = new QueryBuilder();
	}

	@Override
	public List<JSONObject> search(int numSeeds) {
		List<JSONObject> result = new ArrayList<>();

		String query = queryBuilder.getQuery(Seed.UNION_FIRST);
		int start = 0;		
		
		do {
			String url = SEARCH_URL + query.replaceAll(" ", "+") + FILE_TYPE + "html" + START + start;

			Connection conn = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000);
			try {
				Document doc = conn.get();
				result.addAll(formatter(doc));
			} catch (IOException e) {
				logger.warn("Could not search for seed pages.");
				logger.error(e.getMessage());
			} catch (ParseException e) {
				logger.warn("Could not search for seed pages.");
				logger.error(e.getMessage());
			}
			
			start += 10;
		} while (result.size() < numSeeds);

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> formatter(Document doc) throws ParseException {
		List<JSONObject> entries = new ArrayList<>();

		Elements body = doc.select("body");
		JSONObject response = (JSONObject) new JSONParser().parse(body.text());

		if (!response.isEmpty() && response.containsKey("responseData")) {
			JSONArray results = (JSONArray) ((JSONObject) response.get("responseData")).get("results");
			for (Object result : results) {
				JSONObject entry = new JSONObject();
				entry.put("url", ((JSONObject) result).get("url"));
				entry.put("tag", ((JSONObject) result).get("titleNoFormatting"));
				entry.put("anchor", ((JSONObject) result).get("content"));
				if (CrawlerController.FILTERS.matcher((String) entry.get("url")).matches()) {
					continue;
				}
				entries.add(entry);
			}
		}

		return entries;
	}

}
