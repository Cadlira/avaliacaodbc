package br.com.leolira.dbc.avaliacao.models;

public abstract class DbcData {
	private DbcDataType type;

	public DbcDataType getType() {
		return type;
	}

	protected void setType(DbcDataType type) {
		this.type = type;
	}
	
	
}
