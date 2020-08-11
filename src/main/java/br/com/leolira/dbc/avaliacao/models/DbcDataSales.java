package br.com.leolira.dbc.avaliacao.models;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class DbcDataSales extends DbcData {

	private String id;

	private String salesmanName;

	private List<DbcDataSalesItem> itens;

	public DbcDataSales() {
		super();
		this.setType(DbcDataType.SALES);
	}

	public DbcDataSales(String id, List<DbcDataSalesItem> itens, String salesmanName) {
		this();
		this.id = id;
		this.salesmanName = salesmanName;
		this.itens = itens;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public List<DbcDataSalesItem> getItens() {
		return itens;
	}

	public void setItens(List<DbcDataSalesItem> itens) {
		this.itens = itens;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public double getTotal() {
		if (CollectionUtils.isEmpty(this.itens)) {
			return 0.0;
		}
		return this.itens.stream().mapToDouble(item -> item.getPrice()).sum();
	}
}
