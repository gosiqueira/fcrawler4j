package br.ufop.decom.gaid.focused_crawler.unit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ufop.decom.gaid.focused_crawler.seed.Seed;
import br.ufop.decom.gaid.focused_crawler.seed.search.QueryBuilder;
import br.ufop.decom.gaid.focused_crawler.util.Loader;

public class QueryBuilderTest {
	
	private static Loader loader = Loader.getInstace();
	
	QueryBuilder queryBuilder = new QueryBuilder();
	
	@BeforeClass
	public static void init() {
		loader.setConfigFile("test.properties");
	}
	
	@Test
	public void unionTest() {
		String expected = "plano de ensino disciplina creditos professor pre requisitos ementa "
				+ "conteudo programatico objetivos descricao planejamento aula notas provas "
				+ "trabalhos referencias bibliograficas bibliografia banco de dados bancos de dados "
				+ "sgbd sistemas de banco de dados gerencia de banco de dados "
				+ "modelagem de dados modelo de dados entidade relacionamento modelo conceitual "
				+ "modelo relacional modelo logico integridade referencial algebra relacional calculo relacional "
				+ "sql normalizacao dependencias funcionais definicao de dados controle de concorrencia "
				+ "otimizacao de consulta triggers armazem de informacoes silberschatz navathe";

		String actual = queryBuilder.getQuery(Seed.UNION);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void unionOrTest() {
		String expected = "plano de ensino OR disciplina OR creditos OR professor OR pre requisitos OR ementa OR "
				+ "conteudo programatico OR objetivos OR descricao OR planejamento OR aula OR notas OR provas OR "
				+ "trabalhos OR referencias bibliograficas OR bibliografia OR banco de dados OR bancos de dados OR "
				+ "sgbd OR sistemas de banco de dados OR gerencia de banco de dados OR "
				+ "modelagem de dados OR modelo de dados OR entidade relacionamento OR modelo conceitual OR "
				+ "modelo relacional OR modelo logico OR integridade referencial OR algebra relacional OR calculo relacional OR "
				+ "sql OR normalizacao OR dependencias funcionais OR definicao de dados OR controle de concorrencia OR "
				+ "otimizacao de consulta OR triggers OR armazem de informacoes OR silberschatz OR navathe";

		String actual = queryBuilder.getQuery(Seed.UNION_OR);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void unionFirstTest() {
		String expected = "plano de ensino banco de dados";

		String actual = queryBuilder.getQuery(Seed.UNION_FIRST);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void unionFirstOrTest() {
		String expected = "plano de ensino OR banco de dados";

		String actual = queryBuilder.getQuery(Seed.UNION_FIRST_OR);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void intersectionTest() {
		String expected = "plano de ensino AND disciplina AND creditos AND professor AND pre requisitos AND ementa AND "
				+ "conteudo programatico AND objetivos AND descricao AND planejamento AND aula AND notas AND provas AND "
				+ "trabalhos AND referencias bibliograficas AND bibliografia AND banco de dados AND bancos de dados AND "
				+ "sgbd AND sistemas de banco de dados AND gerencia de banco de dados AND "
				+ "modelagem de dados AND modelo de dados AND entidade relacionamento AND modelo conceitual AND "
				+ "modelo relacional AND modelo logico AND integridade referencial AND algebra relacional AND calculo relacional AND "
				+ "sql AND normalizacao AND dependencias funcionais AND definicao de dados AND controle de concorrencia AND "
				+ "otimizacao de consulta AND triggers AND armazem de informacoes AND silberschatz AND navathe";

		String actual = queryBuilder.getQuery(Seed.INTERSECTION);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void intersectionFirstTest() {
		String expected = "plano de ensino AND banco de dados";

		String actual = queryBuilder.getQuery(Seed.INTERSECTION_FIRST);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void justGenreTest() {
		String expected = "plano de ensino disciplina creditos professor pre requisitos ementa "
				+ "conteudo programatico objetivos descricao planejamento aula notas provas "
				+ "trabalhos referencias bibliograficas bibliografia";

		String actual = queryBuilder.getQuery(Seed.JUST_GENRE);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void justGenreOrTest() {
		String expected = "plano de ensino OR disciplina OR creditos OR professor OR pre requisitos OR ementa OR "
				+ "conteudo programatico OR objetivos OR descricao OR planejamento OR aula OR notas OR provas OR "
				+ "trabalhos OR referencias bibliograficas OR bibliografia";

		String actual = queryBuilder.getQuery(Seed.JUST_GENRE_OR);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void justContentTest() {
		String expected = "banco de dados bancos de dados "
				+ "sgbd sistemas de banco de dados gerencia de banco de dados "
				+ "modelagem de dados modelo de dados entidade relacionamento modelo conceitual "
				+ "modelo relacional modelo logico integridade referencial algebra relacional calculo relacional "
				+ "sql normalizacao dependencias funcionais definicao de dados controle de concorrencia "
				+ "otimizacao de consulta triggers armazem de informacoes silberschatz navathe";

		String actual = queryBuilder.getQuery(Seed.JUST_CONTENT);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void justContentOrTest() {
		String expected = "banco de dados OR bancos de dados OR "
				+ "sgbd OR sistemas de banco de dados OR gerencia de banco de dados OR "
				+ "modelagem de dados OR modelo de dados OR entidade relacionamento OR modelo conceitual OR "
				+ "modelo relacional OR modelo logico OR integridade referencial OR algebra relacional OR calculo relacional OR "
				+ "sql OR normalizacao OR dependencias funcionais OR definicao de dados OR controle de concorrencia OR "
				+ "otimizacao de consulta OR triggers OR armazem de informacoes OR silberschatz OR navathe";
				
		String actual = queryBuilder.getQuery(Seed.JUST_CONTENT_OR);

		Assert.assertEquals(expected, actual);

	}

}
