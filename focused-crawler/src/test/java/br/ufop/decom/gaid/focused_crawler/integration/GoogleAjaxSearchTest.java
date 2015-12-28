package br.ufop.decom.gaid.focused_crawler.integration;

import java.util.List;

import org.json.simple.JSONObject;

import br.ufop.decom.gaid.focused_crawler.seed.SeedBuilder;
import br.ufop.decom.gaid.focused_crawler.seed.search.GoogleAjaxSearch;
import br.ufop.decom.gaid.focused_crawler.seed.search.Search;
import edu.uci.ics.crawler4j.url.WebURL;

public class GoogleAjaxSearchTest {
	
	public static void main(String[] args) {
		Search search = new GoogleAjaxSearch();
		
		/**
		 * Validating JSONObject pattern to be used for SeedBuilder
		 */
		List<JSONObject> seedsJson = search.search(10);
		for(JSONObject seedJson : seedsJson) {
			System.out.println(seedJson);
		}
		
		System.out.println("===========================");
		
		/**
		 * Checking SeedBuilder final result using Google Ajax API as serach engine.
		 */
		SeedBuilder builder = new SeedBuilder(search, 10);
		List<WebURL> seeds = builder.build();
		for(WebURL seed: seeds) {
			System.out.println(seed);
		}
	}

}
