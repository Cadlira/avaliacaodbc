package br.com.leolira.dbc.avaliacao.models;

public class DbcDataClient extends DbcDataNamed {

	private String businessArea;

	public DbcDataClient() {
		super();
		this.setType(DbcDataType.CLIENT);
	}

	public DbcDataClient(String document, String name, String businessArea) {
		this();
		this.businessArea = businessArea;
		this.setDocument(document);
		this.setName(name);
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

}
