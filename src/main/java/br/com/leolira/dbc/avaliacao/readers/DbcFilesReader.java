package br.com.leolira.dbc.avaliacao.readers;

import java.util.Arrays;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.utils.Constants;
import br.com.leolira.dbc.avaliacao.utils.IOUtils;

@Component
@JobScope
public class DbcFilesReader extends MultiResourceItemReader<DbcData> {
	@PostConstruct
	private void initializer() {
		this.setResources(this.getResources());
		this.setDelegate(reader());
	}

	private Resource[] getResources() {
		Resource[] resources = IOUtils.loadResourcesWithPattern(Constants.IN_FILES_PATTERN);
		resources = removeInvalidFiles(resources);
		return resources;
	}

	private Resource[] removeInvalidFiles(Resource[] resources) {
		if (isValidResources(resources)) {
			resources = filterResourcesWithoutExtension(resources, Constants.OUT_FILE_EXTENSION);
		}
		return resources;
	}

	private Resource[] filterResourcesWithoutExtension(Resource[] resources, String extension) {
		return Arrays.stream(resources).filter(res -> !res.getFilename().endsWith(extension))
				.toArray(Resource[]::new);
	}

	private boolean isValidResources(Resource[] resources) {
		return Objects.nonNull(resources) && resources.length > 0;
	}

	private FlatFileItemReader<DbcData> reader() {

		FlatFileItemReader<DbcData> reader = new FlatFileItemReader<DbcData>();
		reader.setEncoding("utf-8");
		reader.setLineMapper(new DefaultLineMapper<DbcData>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer(Constants.DATA_DELIMITER));
				setFieldSetMapper(new DbcCustomDataMapper());
			}
		});
		return reader;
	}
}
