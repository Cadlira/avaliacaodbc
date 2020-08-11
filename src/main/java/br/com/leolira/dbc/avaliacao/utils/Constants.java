package br.com.leolira.dbc.avaliacao.utils;

import java.io.File;

public interface Constants {
	
	String BASE_DIR = System.getProperty("user.home") + File.separator + "data";
	
	String IN_DIR = BASE_DIR + File.separator + "in";
	
	String OUT_DIR = BASE_DIR + File.separator + "out";
	
	String OUT_FILE_PATH = OUT_DIR + File.separator + "avaliacao_dbc.done.dat";
	
	String OUT_FILE_HEADER = "client_quantityçsalesman_quantityçmore_expensive_sale_idçworst_salesman";
}
