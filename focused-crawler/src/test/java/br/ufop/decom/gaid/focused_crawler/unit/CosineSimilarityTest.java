package br.ufop.decom.gaid.focused_crawler.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.ufop.decom.gaid.focused_crawler.similarity.CosineSimilarity;

public class CosineSimilarityTest {

	@Test
	public void testTf() throws IOException {
		String text = "Coletores temáticos apresentam o propósito maior de"
				+ " coletar páginas da Web que sejam relevantes a um tópico"
				+ " ou interesse específico do usuário, sendo importantes"
				+ " para uma grande variedade de aplicações. Em geral, funcionam"
				+ " tentando localizar e coletar páginas que estejam relacionadas"
				+ " a um determinado tópico de interesse. Contudo, alguns"
				+ " usuários podem não estar simplesmente interessados"
				+ " em páginas sobre um tópico; na verdade, podem estar"
				+ " interessados em recuperar páginas de um determinado estilo"
				+ " ou gênero referente ao tópico.";

		List<String> terms = new ArrayList<>();
		
		terms.add("Coletores tematicos");
		terms.add("páginas");
		terms.add("tópico");
		terms.add("páginas da web");
		
		CosineSimilarity cs = new CosineSimilarity();
		List<Double> normalizedTF = cs.augmentedTermFrequency(terms, text);
		System.out.println(normalizedTF);
		double similarity = cs.sim(normalizedTF);
		System.out.println(similarity);
		
		Assert.assertTrue(similarity - 0.9743911956946198 < 0.00000000001);
		
	}

}
