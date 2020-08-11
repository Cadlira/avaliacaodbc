package br.com.leolira.dbc.avaliacao.writers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.models.DbcDataResult;
import br.com.leolira.dbc.avaliacao.utils.Constants;

@Component
@JobScope
public class DbcWriter implements ItemWriter<DbcData> {
	private static final Log logger = LogFactory.getLog(DbcWriter.class);
	private List<DbcData> items = new ArrayList<DbcData>();

	@Override
	public void write(List<? extends DbcData> items) {
		logger.debug("Adicionando itens ao job. qtd -> " + items.size());
		this.items.addAll(items);
		
	}
	
	@PreDestroy
	public void teardown() {
		DbcDataResult result = new DbcDataResult(items);
		Path path = this.getPath();
		writeResultToFile(result, path);
		this.items = new ArrayList<DbcData>();
	}

	private void writeResultToFile(DbcDataResult result, Path path) {
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
			writer.write(Constants.OUT_FILE_HEADER);
			writer.newLine();
			String value = getResultLine(result);
			writer.write(value);
			
			this.printNewJobExecution(value);
			
		} catch (IOException ex) {
			logger.error("Ocorreu um erro ao escrever o arquivo de resultado", ex);		
		} finally {
			if (Objects.nonNull(writer)) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("Ocorreu um erro ao fechar o arquivo de saida", e);
				}
			}
		}
	}

	private void printNewJobExecution(String result) {
		System.out.println("********************************************************");
		System.out.println("********************************************************");
		System.out.println("                                                        ");
		System.out.println("                     RESULTADO                          ");
		System.out.println("                                                        ");
		System.out.println("      "+ Constants.OUT_FILE_HEADER +"   ");
		System.out.println("               "+ result +"              ");
		System.out.println("                                                        ");
		System.out.println("                                                        ");
		System.out.println("**************************************************");
		System.out.println("**************************************************");
	}
	
	private String getResultLine(DbcDataResult result) {
		return String.format("%sç%sç%sç%s", result.getClientQuantity(), result.getSalesmanQuantity(),
				result.getMoreExpensiveSalesId(), result.getWorstSalesmanName());
	}
	
	private Path getPath() {
		Path path = Paths.get(Constants.OUT_DIR);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				logger.error("Ocorreu um erro ao criar o diretorio de saida", e);
			}
		}
		return Paths.get(Constants.OUT_FILE_PATH);
	}

}
