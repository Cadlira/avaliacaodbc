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
		return this.itens.stream().mapToDouble(item -> (item.getPrice() * item.getQuantity())).sum();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((salesmanName == null) ? 0 : salesmanName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbcDataSales other = (DbcDataSales) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (salesmanName == null) {
			if (other.salesmanName != null)
				return false;
		} else if (!salesmanName.equals(other.salesmanName))
			return false;
		return true;
	}
}
