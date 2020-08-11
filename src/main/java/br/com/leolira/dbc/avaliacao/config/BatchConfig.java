package br.com.leolira.dbc.avaliacao.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.leolira.dbc.avaliacao.models.DbcData;
import br.com.leolira.dbc.avaliacao.readers.DbcFilesReader;
import br.com.leolira.dbc.avaliacao.writers.DbcWriter;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

	private JobBuilderFactory jobBuilderFactory;

	private StepBuilderFactory stepBuilderFactory;

	public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean
	public Step step(DbcFilesReader reader, DbcWriter writer) {
		return stepBuilderFactory.get("step1")
			.<DbcData, DbcData>chunk(5)
			.reader(reader)
			.writer(writer)
			.build();
	}

	@Bean
	public Job analyzeDataJob(Step step1) {
		return jobBuilderFactory.get("analyzeDataJob")
			.incrementer(new RunIdIncrementer())
			.flow(step1)
			.end()
			.build();
	}
}