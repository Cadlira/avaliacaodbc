package br.com.leolira.dbc.avaliacao.models;

public class DbcDataSalesman extends DbcDataNamed {
	
	private double salary;
	
	public DbcDataSalesman() {
		super();
		this.setType(DbcDataType.SALESMAN);
	}
	
	public DbcDataSalesman(String document, String name, double salary) {
		this();
		this.salary = salary;
		this.setDocument(document);
		this.setName(name);
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
}
