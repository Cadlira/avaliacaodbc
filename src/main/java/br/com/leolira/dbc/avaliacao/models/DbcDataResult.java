package br.com.leolira.dbc.avaliacao.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

public class DbcDataResult {
	private static final Log logger = LogFactory.getLog(DbcDataResult.class);

	private Set<DbcData> clients;
	private Set<DbcData> salesman;
	private Set<DbcData> sales;
	private Map<String, Double> salesmanTotalSales;

	public DbcDataResult(List<? extends DbcData> dbcDatas) {
		try {
			Map<DbcDataType, List<DbcData>> mapDbcData = getMapDbcDataByType(dbcDatas);
			if (!CollectionUtils.isEmpty(mapDbcData)) {
				if (!CollectionUtils.isEmpty(mapDbcData.get(DbcDataType.CLIENT))) {					
					this.clients = new HashSet<DbcData>(mapDbcData.get(DbcDataType.CLIENT));
				}
				if (!CollectionUtils.isEmpty(mapDbcData.get(DbcDataType.SALESMAN))) {
					this.salesman = new HashSet<DbcData>(mapDbcData.get(DbcDataType.SALESMAN));
				}
				if (!CollectionUtils.isEmpty(mapDbcData.get(DbcDataType.SALES))) {					
					this.sales = new HashSet<DbcData>(mapDbcData.get(DbcDataType.SALES));
				}
			}
			populateSalesmanTotalSales();
			
			logger.debug("Quantidade de clientes: " + this.getClientQuantity());
			logger.debug("Quantidade de vendedores: " + this.getSalesmanQuantity());
			logger.debug("Mmelhor id venda: " + this.getMoreExpensiveSalesId());
			logger.debug("Nome do pior vendedor: " + this.getWorstSalesmanName());	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public long getClientQuantity() {
		if (CollectionUtils.isEmpty(this.clients)) {
			return 0l;
		}

		return this.clients.size();
	}

	public long getSalesmanQuantity() {
		if (CollectionUtils.isEmpty(this.salesman)) {
			return 0l;
		}

		return this.salesman.size();
	}

	public String getMoreExpensiveSalesId() {
		DbcDataSales sale = this.getMaxPriceSale();
		if (Objects.isNull(sale)) {
			return "0";
		}

		return sale.getId();
	}

	public String getWorstSalesmanName() {
		if (CollectionUtils.isEmpty(this.salesmanTotalSales)) {
			return "";
		}
		return Collections.min(this.salesmanTotalSales.entrySet(), Comparator.comparing(Entry::getValue)).getKey();
	}

	private void populateSalesmanTotalSales() {
		if (!CollectionUtils.isEmpty(this.sales)) {
			this.salesmanTotalSales = this.sales.parallelStream().map(dbcData -> (DbcDataSales) dbcData)
					.collect(Collectors.groupingBy(dbcData -> dbcData.getSalesmanName(),
							Collectors.summingDouble(DbcDataSales::getTotal)));
		}
	}

	private Map<DbcDataType, List<DbcData>> getMapDbcDataByType(List<? extends DbcData> dbcDatas) {
		return dbcDatas.stream().collect(Collectors.groupingBy(dbcData -> dbcData.getType()));
	}

	private DbcDataSales getMaxPriceSale() {
		if (CollectionUtils.isEmpty(this.sales)) {
			return null;
		}

		return this.sales.stream().map(dbcDate -> (DbcDataSales) dbcDate)
				.max(Comparator.comparing(DbcDataSales::getTotal)).orElse(null);
	}
}
