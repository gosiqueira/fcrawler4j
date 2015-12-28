package br.ufop.decom.gaid.focused_crawler.storer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Storer {

	Logger logger = LoggerFactory.getLogger(Storer.class);
	
	private String file;
	
	public Storer(String file) {
		this.file = file.trim();
	}
	
	public void store(Item item) {
		try {
			FileWriter writer = new FileWriter(new File(this.file));
			
			writer.write(item.toString());
			
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.warn("Could not store file " + file + "...");
		}
	}
	
}
