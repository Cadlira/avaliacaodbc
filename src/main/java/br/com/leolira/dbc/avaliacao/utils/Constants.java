package br.com.leolira.dbc.avaliacao.utils;

import java.io.File;

import br.com.leolira.dbc.avaliacao.config.DbcDirConfig;

public interface Constants {
	
	String BASE_DIR = DbcDirConfig.BASE_DIR;
	
	String DATA_DIR = "data";
	
	String IN_DIR = BASE_DIR + File.separator + "in";
	
	String OUT_DIR = BASE_DIR + File.separator + "out";
	
	String IN_FILE_EXTENSION = ".dat";
	
	String OUT_FILE_EXTENSION = ".done.dat";
	
	String OUT_FILE_PATH = OUT_DIR + File.separator + "avaliacao_dbc" + OUT_FILE_EXTENSION;
	
	String DATA_DELIMITER = "ç";
	
	String OUT_FILE_HEADER = String.format("client_quantity%ssalesman_quantity%smore_expensive_sale_id%sworst_salesman",
			DATA_DELIMITER, DATA_DELIMITER, DATA_DELIMITER);
	
	String IN_FILES_PATTERN = "file:" + Constants.IN_DIR + "/*" + IN_FILE_EXTENSION;
	
	
}
