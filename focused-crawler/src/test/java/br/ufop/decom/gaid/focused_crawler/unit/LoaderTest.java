package br.ufop.decom.gaid.focused_crawler.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.ufop.decom.gaid.focused_crawler.util.Loader;

public class LoaderTest {

	@Test
	public void loaderTest() {
		Loader loader = Loader.getInstace();
		loader.setConfigFile("test.properties");
		
		loader.init();

		Assert.assertEquals(loader.getWorkingDirectory(), "./");
		Assert.assertEquals(loader.getGenreTermsFile(), "genre.collect");
		Assert.assertEquals(loader.getContentTermsFile(), "content.collect");
		
		List<String> actual = new ArrayList<>();
		actual.add("plano de ensino");
		actual.add("disciplina");
		actual.add("creditos");
		actual.add("professor");
		actual.add("pre requisitos");
		actual.add("ementa");
		actual.add("conteudo programatico");
		actual.add("objetivos");
		actual.add("descricao");
		actual.add("planejamento");
		actual.add("aula");
		actual.add("notas");
		actual.add("provas");
		actual.add("trabalhos");
		actual.add("referencias bibliograficas");
		actual.add("bibliografia");

		List<String> expected = loader.loadGenreTerms();

		System.out.println(actual);
		System.out.println(expected);

		Assert.assertEquals(actual, expected);
		
		actual.clear();
		actual.add("banco de dados");
		actual.add("bancos de dados");
		actual.add("sgbd");
		actual.add("sistemas de banco de dados");
		actual.add("gerencia de banco de dados");
		actual.add("modelagem de dados");
		actual.add("modelo de dados");
		actual.add("entidade relacionamento");
		actual.add("modelo conceitual");
		actual.add("modelo relacional");
		actual.add("modelo logico");
		actual.add("integridade referencial");
		actual.add("algebra relacional");
		actual.add("calculo relacional");
		actual.add("sql");
		actual.add("normalizacao");
		actual.add("dependencias funcionais");
		actual.add("definicao de dados");
		actual.add("controle de concorrencia");
		actual.add("otimizacao de consulta");
		actual.add("triggers");
		actual.add("armazem de informacoes");
		actual.add("silberschatz");
		actual.add("navathe");

		expected = loader.loadContentTerms();
		
		System.out.println(actual);
		System.out.println(expected);

		Assert.assertEquals(actual, expected);
	}

}
