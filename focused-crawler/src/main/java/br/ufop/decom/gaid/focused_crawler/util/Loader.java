package br.ufop.decom.gaid.focused_crawler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.xmlbeans.impl.xb.ltgfmt.Code.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loader {

	private static final Logger logger = LoggerFactory.getLogger(Loader.class);

	private String file = "config.properties";
	
	private static Loader instance;
	private static Object classLock = Factory.class;

	private boolean initialized;
	
	private String workingDirectory;
	private String genreTermsFile;
	private String contentTermsFile;

	private Loader() {
		this.initialized = false;
	}

	public static Loader getInstace() {
		synchronized (classLock) {
			if (instance == null) {
				instance = new Loader();
			}

			return instance;
		}
	}

	public void init() {
		synchronized (classLock) {
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(file));

				this.workingDirectory = prop.getProperty("working_directory");
				this.genreTermsFile = prop.getProperty("genre_terms_file");
				this.contentTermsFile = prop.getProperty("content_terms_file");

				this.initialized = true;
			} catch (IOException e) {
				logger.error(e.getMessage());
				logger.warn("Could not load config.properties file");
			}
		}
	}

	public List<String> loadGenreTerms() {
		synchronized (classLock) {
			List<String> genreTerms = new ArrayList<>();

			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(workingDirectory + genreTermsFile)));

				while (br.ready()) {
					genreTerms.add(br.readLine());
				}

				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return genreTerms;
		}
	}

	public List<String> loadContentTerms() {
		synchronized (classLock) {
			List<String> contentTerms = new ArrayList<>();

			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(workingDirectory + contentTermsFile)));

				while (br.ready()) {
					contentTerms.add(br.readLine());
				}

				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return contentTerms;
		}
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public String getGenreTermsFile() {
		return genreTermsFile;
	}

	public String getContentTermsFile() {
		return contentTermsFile;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setConfigFile(String file) {
		this.file = file;
	}
	
}
