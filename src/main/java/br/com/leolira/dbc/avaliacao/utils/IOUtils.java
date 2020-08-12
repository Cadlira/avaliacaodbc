package br.com.leolira.dbc.avaliacao.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class IOUtils {
	
	public static Path getPathAndCrateDirectories(String pathStr) throws IOException {
		Path path = Paths.get(pathStr);
		if (Files.notExists(path)) {
			createDirectories(path);
		}
		return path;
	}

	private static void createDirectories(Path path) throws IOException {
		if (Files.isDirectory(path)) {			
			Files.createDirectories(path);
		} else {
			Files.createDirectories(path.getParent());
		}
	}
	
	public static Resource[] loadResourcesWithPattern(String pattern) {
		Resource[] resources = null;
		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		String filePattern = Constants.IN_FILES_PATTERN;
		try {
			resources = patternResolver.getResources(filePattern);
		} catch (IOException e) {
			/**
			 * TODO tratar erro
			 */
		}
		return resources;
	}
	
	public static void writeLinesInFile(Path filePath, String ...lines) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(filePath, Charset.forName("UTF-8"));
		if (hasLines(lines)) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		}
		writer.close();
	}

	private static boolean hasLines(String... lines) {
		return Objects.nonNull(lines) && lines.length > 0;
	}
}
