package br.ufop.decom.gaid.focused_crawler.seed.search;

import java.util.List;

import org.json.simple.JSONObject;

public interface Search {

	public List<JSONObject> search(int numSeeds);
	
}
