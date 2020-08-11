package br.com.leolira.dbc.avaliacao.config;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("dbc")
@Component
public class DbcDirConfig {
	private String baseDir;
	
	public static String BASE_DIR = System.getProperty("user.home") + File.separator + "data";

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
		if (isValidPath(baseDir)) {
			DbcDirConfig.BASE_DIR = baseDir + File.separator + "data";
		}
	}
	
	private boolean isValidPath(String path) {
	    try {
	        Paths.get(path);
	    } catch (InvalidPathException | NullPointerException ex) {
	        return false;
	    }
	    return true;
	}
}
