package br.com.leolira.dbc.avaliacao.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

public class TestUtils {

	public static void writeInFile(String content) {
		BufferedWriter writer = null;
		try {
			Path path = Paths.get(Constants.IN_DIR + File.separator + System.currentTimeMillis() + ".dat");
			Thread.sleep(100);
			writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
			writer.write(content);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (Objects.nonNull(writer)) {
				try {
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String readOutFile() {
		Path path = Paths.get(Constants.OUT_FILE_PATH);
		if (path.toFile().exists()) {
			List<String> lines;
			try {
				lines = Files.readAllLines(path);
				if (!CollectionUtils.isEmpty(lines) && lines.size() > 1) {
					return lines.get(1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public static void deleteFile(String folder, String ext) {

		GenericExtFilter filter = new GenericExtFilter(ext);
		File dir = new File(folder);
		String[] list = dir.list(filter);

		if (list.length == 0)
			return;

		File fileDelete;

		for (String file : list) {
			String temp = new StringBuffer(folder).append(File.separator).append(file).toString();
			fileDelete = new File(temp);
			fileDelete.delete();
		}
	}

	private static class GenericExtFilter implements FilenameFilter {
		private String ext;
		public GenericExtFilter(String ext) {
			this.ext = ext;
		}
		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
}
