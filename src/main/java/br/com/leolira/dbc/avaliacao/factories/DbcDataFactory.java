package br.com.leolira.dbc.avaliacao.factories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.models.DbcDataClient;
import br.com.leolira.dbc.avaliacao.models.DbcDataSales;
import br.com.leolira.dbc.avaliacao.models.DbcDataSalesItem;
import br.com.leolira.dbc.avaliacao.models.DbcDataSalesman;
import br.com.leolira.dbc.avaliacao.models.DbcDataUndefined;

public class DbcDataFactory {
	private static final Log logger = LogFactory.getLog(DbcDataFactory.class);
	
	public static DbcData build(String[] values) {
		try {
			if (Objects.nonNull(values) && values.length >= 4) {
				switch (values[0]) {
				case "001":
					return new DbcDataSalesman(values[1], values[2], Double.parseDouble(values[3]));
				case "002":
					return new DbcDataClient(values[1], values[2], values[3]);
				case "003":
					return new DbcDataSales(values[1], getDbcDataSalesItems(values[2]), values[3]);
				}
			}	
		} catch (Exception e) {
			logger.error("Ocorreu um erro ao criar um objeto de análise: " + Arrays.toString(values), e);			
		}
		
		return new DbcDataUndefined();
	}

	private static List<DbcDataSalesItem> getDbcDataSalesItems(String value) {

		return Collections.list(new StringTokenizer(value.replaceAll("\\[|\\]", ""), ",")).stream().map(itemStr -> {
			String[] fields = ((String) itemStr).split("-");
			return new DbcDataSalesItem(fields[0], Long.parseLong(fields[1]), Double.parseDouble(fields[2]));
		}).collect(Collectors.toList());
	}
}
