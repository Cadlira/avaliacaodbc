package br.com.leolira.dbc.avaliacao.service;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import br.com.leolira.dbc.avaliacao.AvaliacaoDbcApplication;
import br.com.leolira.dbc.avaliacao.utils.Constants;

@SpringBootTest(classes = { AvaliacaoDbcApplication.class })
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootApplication(scanBasePackages = { "br.com.leolira" })
@EntityScan(basePackages = { "br.com.leolira"})
public class ExecutionServiceTest {
	

	@Test
	@Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
	public void teste() {
	System.out.println(Constants.BASE_DIR);	
	}
}
