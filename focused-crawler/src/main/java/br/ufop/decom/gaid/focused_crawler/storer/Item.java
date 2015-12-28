package br.ufop.decom.gaid.focused_crawler.storer;

public class Item {
	
	private String title;
	private String url;
	private String html;
	
	public Item(String title, String url, String html) {
		this.title = title;
		this.url = url;
		this.html = html;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getHtml() {
		return html;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	@Override
	public String toString() {
		return "title=" + title + "\nurl=" + url + "\nhtml=" + html;
	}
	
}
