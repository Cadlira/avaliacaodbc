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

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.models.DbcDataResult;
import br.com.leolira.dbc.avaliacao.utils.Constants;

@Component
@JobScope
public class DbcWriter implements ItemWriter<DbcData> {
	private List<DbcData> items = new ArrayList<DbcData>();

	@PreDestroy
	public void teardown() throws IOException {
		DbcDataResult result = new DbcDataResult(items);

		Path path = this.getPath();
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
			writer.write(Constants.OUT_FILE_HEADER);
			writer.newLine();
			writer.write(String.format("%sç%sç%sç%s", result.getClientQuantity(), result.getSalesmanQuantity(),
					result.getMoreExpensiveSalesId(), result.getWorstSalesmanName()));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (Objects.nonNull(writer)) {
				writer.close();
			}
		}
		this.items = new ArrayList<DbcData>();
	}
	
	@Override
	public void write(List<? extends DbcData> items) throws Exception {
		this.items.addAll(items);
		
	}
	
	private Path getPath() throws IOException {
		Path path = Paths.get(Constants.OUT_DIR);
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}
		return Paths.get(Constants.OUT_FILE_PATH);
	}

}
