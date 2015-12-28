package br.ufop.decom.gaid.focused_crawler.seed.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.ufop.decom.gaid.focused_crawler.seed.Seed;
import br.ufop.decom.gaid.focused_crawler.util.Loader;

public class QueryBuilder {

	private Loader loader = Loader.getInstace();
	
	private List<String> genreTerms;
	private List<String> contentTerms;
	
	public QueryBuilder() {
		if(!loader.isInitialized()) {
			loader.init();
		}
		
		this.genreTerms = loader.loadGenreTerms();
		this.contentTerms = loader.loadContentTerms();
	}
	
	public QueryBuilder(List<String> genreTerms, List<String> contentTerms) {
		this.genreTerms = loader.loadGenreTerms();
		this.contentTerms = loader.loadContentTerms();
	}
	
	public String getQuery(Seed heuristic) {
		String query;
		switch(heuristic) {
		case UNION:
			query = union();
			break;
		case UNION_OR:
			query = unionOr();
			break;
		case UNION_FIRST:
			query = unionFirst();
			break;
		case UNION_FIRST_OR:
			query = unionFirstOr();
			break;
		case INTERSECTION:
			query = intersection();
			break;
		case INTERSECTION_FIRST:
			query = intersectionFirst();
			break;
		case JUST_GENRE:
			query = justGenre();
			break;
		case JUST_GENRE_OR:
			query = justGenreOr();
			break;
		case JUST_CONTENT:
			query = justContent();
			break;
		case JUST_CONTENT_OR:
			query = justContentOr();
			break;
		default:
			query = "";
			break;
		}
		
		return query;
	}
	
	public String union(){
        String query;
        List<String> union = new ArrayList<>();
        
        union.addAll(this.genreTerms);
        union.addAll(this.contentTerms);
        
        query = StringUtils.join(union, " ");        
        
        return query;
    }
	
	public String unionOr() {
		String query;
        List<String> union = new ArrayList<>();
        
        union.addAll(this.genreTerms);
        union.addAll(this.contentTerms);
        
        query = StringUtils.join(union, " OR ");        
        
        return query;
	}
	
	public String unionFirst() {
		return this.genreTerms.get(0) + " " + this.contentTerms.get(0);
	}
	
	public String unionFirstOr() {
		return this.genreTerms.get(0) + " OR " + this.contentTerms.get(0);
	}

	public String intersection() {
		String query;
        List<String> intersection = new ArrayList<>();
        
        intersection.addAll(this.genreTerms);
        intersection.addAll(this.contentTerms);
        
        query = StringUtils.join(intersection, " AND ");        
        
        return query;
	}
	
	public String intersectionFirst() {
		return this.genreTerms.get(0) + " AND " + this.contentTerms.get(0); 
	}
	
	public String justGenre() {
		return StringUtils.join(this.genreTerms, " ");
	}
	
	public String justGenreOr() {
		return StringUtils.join(this.genreTerms, " OR ");
	}
	
	public String justContent() {
		return StringUtils.join(this.contentTerms, " ");
	}
	
	public String justContentOr() {
		return StringUtils.join(this.contentTerms, " OR ");
	}
	
}
