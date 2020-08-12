package br.com.leolira.dbc.avaliacao.exceptions;

public class InputDataInvalidException extends RuntimeException {

	private static final long serialVersionUID = 7720342296099798927L;

	private String[] invalidData;
	
	public InputDataInvalidException(Throwable throwable, String[] invalidData) {
		super(throwable);
		this.invalidData = invalidData;
	}

	public String[] getInvalidData() {
		return invalidData;
	}
}
