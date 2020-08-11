package br.com.leolira.dbc.avaliacao.models;

public class DbcDataSalesItem {
	private String id;
	private long quantity;
	private double price;
	
	public DbcDataSalesItem(String id, long quantity, double price) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.price = price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
