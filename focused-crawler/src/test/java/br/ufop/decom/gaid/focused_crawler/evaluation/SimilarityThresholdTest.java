package br.ufop.decom.gaid.focused_crawler.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufop.decom.gaid.focused_crawler.similarity.CosineSimilarity;
import br.ufop.decom.gaid.focused_crawler.util.Loader;

public class SimilarityThresholdTest {

	public double estimateLowerSimilarityThreshold() throws Exception {
		double threshold = 1.0;

		Loader loader = Loader.getInstace();
		loader.init();

		List<String> genreTerms = loader.loadGenreTerms();
		List<String> contentTerms = loader.loadContentTerms();
		List<String> urlTerms = new ArrayList<>();
		Map<String, String> template = loader.loadTemplate();

		urlTerms.addAll(genreTerms);
		urlTerms.addAll(contentTerms);

		CosineSimilarity cosineSimilarity = new CosineSimilarity(genreTerms, contentTerms, urlTerms, 0.3, 0.7, 0.8, 0.2,
				0.0);

		for (String fileName : template.keySet()) {
			BufferedReader br = new BufferedReader(new FileReader(new File("./crawledPages/" + fileName)));

			String text = "";
			String url = "";
			while (br.ready()) {
				String line = br.readLine();
				if (line.startsWith("url=")) {
					url = line.substring(4);
				} else {
					text += line;
				}
			}

			br.close();

			double similarity = cosineSimilarity.similarity(text, url);

			if (similarity < threshold) {
				System.out.println(fileName + " has a lower similarity: " + similarity);
				threshold = similarity;
			}

		}

		return threshold;
	}

	public double estimateUpperSimilarityThreshold() throws Exception {
		double threshold = 0.0;

		Loader loader = Loader.getInstace();
		loader.init();

		List<String> genreTerms = loader.loadGenreTerms();
		List<String> contentTerms = loader.loadContentTerms();
		List<String> urlTerms = new ArrayList<>();
		Map<String, String> template = loader.loadTemplate();

		urlTerms.addAll(genreTerms);
		urlTerms.addAll(contentTerms);

		CosineSimilarity cosineSimilarity = new CosineSimilarity(genreTerms, contentTerms, urlTerms, 0.3, 0.7, 0.8, 0.2,
				0.0);

		for (String fileName : template.keySet()) {
			BufferedReader br = new BufferedReader(new FileReader(new File("./crawledPages/" + fileName)));

			String text = "";
			String url = "";
			while (br.ready()) {
				String line = br.readLine();
				if (line.startsWith("url=")) {
					url = line.substring(4);
				} else {
					text += line;
				}
			}

			br.close();

			double similarity = cosineSimilarity.similarity(text, url);

			if (similarity > threshold) {
				System.out.println(fileName + " has a upper similarity: " + similarity);
				threshold = similarity;
			}

		}

		return threshold;
	}

	public void generateProcessSimilarityTable() throws Exception {
		Loader loader = Loader.getInstace();
		loader.init();

		List<String> genreTerms = loader.loadGenreTerms();
		List<String> contentTerms = loader.loadContentTerms();
		List<String> urlTerms = new ArrayList<>();

		urlTerms.addAll(genreTerms);
		urlTerms.addAll(contentTerms);

		CosineSimilarity cosineSimilarity = new CosineSimilarity(genreTerms, contentTerms, urlTerms, 0.3, 0.7, 0.8, 0.2,
				0.0);

		File folder = new File("./crawledPages");
		File[] listOfFiles = folder.listFiles();
		FileWriter outputFile = new FileWriter(new File("./similarity_table.collected"));
		for (File file : listOfFiles) {
			if (file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file));

				String text = "";
				String url = "";
				while (br.ready()) {
					String line = br.readLine();
					if (line.startsWith("url=")) {
						url = line.substring(4);
					} else {
						text += line;
					}
				}

				br.close();

				double similarity = cosineSimilarity.similarity(text, url);

				outputFile.write(file.getName() + ";" + url + ";" + similarity + "\n");
			}
		}
		outputFile.close();
	}

	public void estimateOptimalSimilarityThreshold(double lowerBound, double upperBound) {
		Loader loader = Loader.getInstace();
		loader.init();
		
		Map<String, Double> similarityTable = loader.loadSimilarityTable();
		Map<String, String> template = loader.loadTemplate();
		
		List<String> relevants = new ArrayList<String>();
		
		for(double i = lowerBound; i < upperBound; i+= 0.001) {
			
			int truePositive = 0;
			int falsePositive = 0;
			int falseNegative = 0;

			double precision = 0.0;
			double recall = 0.0;
			
			System.out.println("Evaluating result for threshold " + i);
			
			for (String str : similarityTable.keySet()) {
				if (similarityTable.get(str) >= i) {
					relevants.add(str);
				}
			}
			
			System.out.println("Relevants size = " + relevants.size());

			for (String str : relevants) {
				if (template.values().contains(str)) {
					truePositive++;
				} else {
					falsePositive++;
				}
			}

			for (String str : template.values()) {
				if (!relevants.contains(str)) {
					falseNegative++;
				}
			}
			
			relevants.clear();
			
			precision = truePositive / (1.0 * (truePositive + falsePositive));
			recall = truePositive / (1.0 * (truePositive + falseNegative));

			System.out.println("TP = " + truePositive);
			System.out.println("FP = " + falsePositive);
			System.out.println("FN = " + falseNegative);
			System.out.println("Precision = " + precision);
			System.out.println("Recall = " + recall);
			System.out.println("F-score = " + 2*((precision * recall)/(precision + recall)));
			System.out.println("----------------------------");
		}
	}

	public static void main(String[] args) {

		SimilarityThresholdTest test = new SimilarityThresholdTest();

		try {
//			System.out.println("Evaluating lower similarity threshold...");
//			System.out.println("---------------------------------");
//			double lower = test.estimateLowerSimilarityThreshold();
//			System.out.println("Lower Similarity Threshold: " + lower);
//			System.out.println("---------------------------------");
//			System.out.println("Evaluating upper similarity threshold...");
//			System.out.println("---------------------------------");
//			double upper = test.estimateUpperSimilarityThreshold();
//			System.out.println("Upper Similarity Threshold: " + upper);
//			System.out.println("---------------------------------");
			System.out.println("Generating page's similatities table...");
			test.generateProcessSimilarityTable();
			System.out.println("Finished to generate page's similarities table.");
			System.out.println("---------------------------------");
//			test.estimateOptimalSimilarityThreshold(lower, upper);
			test.estimateOptimalSimilarityThreshold(0.540, 0.973);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
