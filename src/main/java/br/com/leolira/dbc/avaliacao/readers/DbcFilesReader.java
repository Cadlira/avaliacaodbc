package br.com.leolira.dbc.avaliacao.readers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.utils.Constants;

@Component
@JobScope
public class DbcFilesReader extends MultiResourceItemReader<DbcData> {

	@PostConstruct
	private void initializer() {
		
		this.setResources(this.getResources());
		
		this.setDelegate(reader());

	}
	
	private Resource[] getResources() {
		Resource[] resources = null;
		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		String filePattern = "file:" + Constants.IN_DIR + "/*.dat"; 
		try {
			resources = patternResolver.getResources(filePattern);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		if (Objects.nonNull(resources) && resources.length > 0) {
			resources = Arrays.stream(resources).filter(res -> !res.getFilename().endsWith(".done.dat"))
					.toArray(Resource[]::new);
		}
		return resources;
	}

	
	private FlatFileItemReader<DbcData> reader() {
		FlatFileItemReader<DbcData> reader = new FlatFileItemReader<DbcData>();
		
		reader.setLineMapper(new DefaultLineMapper<DbcData>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer("ç"));
 
				setFieldSetMapper(new DbcCustomDataMapper());
			}
		});
		return reader;
	}
}
