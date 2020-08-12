package br.com.leolira.dbc.avaliacao.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class DbcDataResult {
	private Set<DbcData> clients;
	private Set<DbcData> salesman;
	private Set<DbcData> sales;
	private Map<String, Double> salesmanTotalSales;

	protected DbcDataResult() {
		this.clients = new HashSet<DbcData>();
		this.salesman = new HashSet<DbcData>();
		this.sales = new HashSet<DbcData>();
		this.salesmanTotalSales = new HashMap<String, Double>();
	}

	public DbcDataResult(List<? extends DbcData> dbcDatas) {
		this();
		convertDbcDatasInResults(dbcDatas);
		populateSalesmanTotalSales();
	}

	private void convertDbcDatasInResults(List<? extends DbcData> dbcDatas) {
		Optional<Map<DbcDataType, List<DbcData>>> mapDbcDataOpt = getMapDbcDataByType(dbcDatas);
		mapDbcDataOpt.ifPresent(mapDbcData -> {
			populateResultClient(mapDbcData);
			populateResultSalesman(mapDbcData);
			populateResultSales(mapDbcData);
		});
	}

	private void populateResultSales(Map<DbcDataType, List<DbcData>> mapDbcData) {
		if (this.hasDbcDataSales(mapDbcData)) {
			this.sales = new HashSet<DbcData>(mapDbcData.get(DbcDataType.SALES));
		}
	}

	private void populateResultSalesman(Map<DbcDataType, List<DbcData>> mapDbcData) {
		if (this.hasDbcDataSalesman(mapDbcData)) {
			this.salesman = new HashSet<DbcData>(mapDbcData.get(DbcDataType.SALESMAN));
		}

	}

	private void populateResultClient(Map<DbcDataType, List<DbcData>> mapDbcData) {
		if (this.hasDbcDataClient(mapDbcData)) {
			this.clients = new HashSet<DbcData>(mapDbcData.get(DbcDataType.CLIENT));
		}
	}

	private boolean hasDbcDataSales(Map<DbcDataType, List<DbcData>> mapDbcData) {
		return !CollectionUtils.isEmpty(mapDbcData.get(DbcDataType.SALES));
	}

	private boolean hasDbcDataSalesman(Map<DbcDataType, List<DbcData>> mapDbcData) {
		return !CollectionUtils.isEmpty(mapDbcData.get(DbcDataType.SALESMAN));
	}

	private boolean hasDbcDataClient(Map<DbcDataType, List<DbcData>> mapDbcData) {
		return !CollectionUtils.isEmpty(mapDbcData.get(DbcDataType.CLIENT));
	}

	public long getClientQuantity() {
		return this.clients.size();
	}

	public long getSalesmanQuantity() {
		return this.salesman.size();
	}

	public String getMoreExpensiveSalesId() {
		Optional<DbcDataSales> saleopt = this.getDbcDataSaleMaxPrice();
		if (saleopt.isPresent()) {
			return saleopt.get().getId();
		}
		return "0";

	}

	public String getWorstSalesmanName() {
		return getSalesmanNameMinSalesValue().orElse("");
	}

	private Optional<String> getSalesmanNameMinSalesValue() {
		if (CollectionUtils.isEmpty(this.salesmanTotalSales)) {
			return Optional.empty();
		}
		return Optional.ofNullable(Collections.min(this.salesmanTotalSales.entrySet(), Comparator.comparing(Entry::getValue)).getKey());
	}

	private void populateSalesmanTotalSales() {
		if (hasSalesResult()) {
			this.salesmanTotalSales = generateMapSalesmanTotalSales();
		}
	}

	private Map<String, Double> generateMapSalesmanTotalSales() {
		return this.sales.parallelStream().map(dbcData -> (DbcDataSales) dbcData).collect(Collectors
				.groupingBy(dbcData -> dbcData.getSalesmanName(), Collectors.summingDouble(DbcDataSales::getTotal)));
	}

	private boolean hasSalesResult() {
		return !CollectionUtils.isEmpty(this.sales);
	}

	private Optional<Map<DbcDataType, List<DbcData>>> getMapDbcDataByType(List<? extends DbcData> dbcDatas) {
		return Optional.ofNullable(dbcDatas.stream().collect(Collectors.groupingBy(dbcData -> dbcData.getType())));
	}

	private Optional<DbcDataSales> getDbcDataSaleMaxPrice() {
		return this.sales.stream().map(dbcDate -> (DbcDataSales) dbcDate)
				.max(Comparator.comparing(DbcDataSales::getTotal));
	}
}
