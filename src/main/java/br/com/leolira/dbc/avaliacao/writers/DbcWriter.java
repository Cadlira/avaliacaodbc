package br.com.leolira.dbc.avaliacao.writers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.models.DbcDataResult;
import br.com.leolira.dbc.avaliacao.utils.Constants;
import br.com.leolira.dbc.avaliacao.utils.IOUtils;

@Component
@JobScope
public class DbcWriter implements ItemWriter<DbcData> {
	private List<DbcData> dbcDatas;

	public DbcWriter() {
		this.clearDbcDatas();
	}

	@Override
	public void write(List<? extends DbcData> items) {
		this.dbcDatas.addAll(items);
	}

	@PreDestroy
	public void generateResultsDbcDatas() throws IOException {
		DbcDataResult result = new DbcDataResult(dbcDatas);
		Path outFile = IOUtils.getPathAndCrateDirectories(Constants.OUT_FILE_PATH);
		writeResultToFile(result, outFile);
		clearDbcDatas();
	}

	private void clearDbcDatas() {
		this.dbcDatas = new ArrayList<DbcData>();
	}

	private void writeResultToFile(DbcDataResult result, Path path) throws IOException {
		IOUtils.writeLinesInFile(path, Constants.OUT_FILE_HEADER, getResultLine(result));
	}

	private String getResultLine(DbcDataResult result) {
		return String.format("%s%s%s%s%s%s%s", result.getClientQuantity(), Constants.DATA_DELIMITER,
				result.getSalesmanQuantity(), Constants.DATA_DELIMITER, result.getMoreExpensiveSalesId(),
				Constants.DATA_DELIMITER, result.getWorstSalesmanName());
	}
}
