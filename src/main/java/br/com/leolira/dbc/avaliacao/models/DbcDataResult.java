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

import org.springframework.util.CollectionUtils;

public class DbcDataResult {

	private Set<DbcData> clients;
	private Set<DbcData> salesman;
	private List<DbcData> sales;
	private Map<String, Double> salesmanTotalSales;

	public DbcDataResult(List<? extends DbcData> dbcDatas) {
		Map<DbcDataType, List<DbcData>> mapDbcData = getMapDbcDataByType(dbcDatas);
		this.clients = new HashSet<DbcData>(mapDbcData.get(DbcDataType.CLIENT));
		this.salesman = new HashSet<DbcData>(mapDbcData.get(DbcDataType.SALESMAN));
		this.sales = mapDbcData.get(DbcDataType.SALES);
		populateSalesmanTotalSales();
		
		System.out.println("Quantidade de clientes: " + this.getClientQuantity());
		System.out.println("Quantidade de vendedores: " + this.getSalesmanQuantity());
		System.out.println("Quantidade de melhor id venda: " + this.getMoreExpensiveSalesId());
		System.out.println("Quantidade de pior vendedor: " + this.getWorstSalesmanName());
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
