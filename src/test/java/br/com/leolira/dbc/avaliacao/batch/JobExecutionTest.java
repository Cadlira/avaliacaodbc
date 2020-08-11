package br.com.leolira.dbc.avaliacao.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.leolira.dbc.avaliacao.utils.TestUtils;

@SpringBootTest(classes = { AvaliacaoDbcApplication.class })
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootApplication(scanBasePackages = { "br.com.leolira" })
@EntityScan(basePackages = { "br.com.leolira" })
public class JobExecutionTest {

	private static final String SALESMAN_1 = "001ç1234567891234çPedroç50000"; 
	private static final String CLIENT_1 = "002ç2345675434544345çJose da SilvaçRural"; 
	private static final String SALES_1 = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro";
	
	private static final String SALESMAN_2 = "001ç123456789çJoaoç50000"; 
	private static final String CLIENT_2 = "002ç1234578798çPadaria 1çUrbano"; 
	private static final String SALES_2 = "003ç11ç[1-5-100,2-30-2.50,3-1000-2.10]çJoao";
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@AfterEach
	public void tearDown() {
		Files.delete(new File(Constants.OUT_FILE_PATH));
		TestUtils.deleteFile(Constants.IN_DIR, ".dat");
	}

	@Test
	public void testExecuteJobWithOneSalesmanOneClientAndOneSale() throws InterruptedException {
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(SALESMAN_1);
		TestUtils.writeInFile(SALES_1);
		this.executeJob();
		String result = TestUtils.readOutFile();
		assertEquals("1ç1ç10çPedro", result);
	}
	
	@Test
	public void testExecuteJobWithTwoSalesmanTwoClientAndTwoSale() throws InterruptedException {
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(SALESMAN_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(CLIENT_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALES_2);
		this.executeJob();
		String result = TestUtils.readOutFile();
		assertEquals("2ç2ç11çPedro", result);
	}
	
	@Test
	public void testExecuteJobWithRepitedClients() throws InterruptedException {
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(SALESMAN_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(CLIENT_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALES_2);
		this.executeJob();
		String result = TestUtils.readOutFile();
		assertEquals("2ç2ç11çPedro", result);
	}
	
	@Test
	public void testExecuteJobWithRepitedSalesman() throws InterruptedException {
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(SALESMAN_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(CLIENT_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALES_2);
		this.executeJob();
		String result = TestUtils.readOutFile();
		assertEquals("2ç2ç11çPedro", result);
	}
	
	@Test
	public void testExecuteJobWithRepitedSales() throws InterruptedException {
		TestUtils.writeInFile(CLIENT_1);
		TestUtils.writeInFile(SALESMAN_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(SALES_1);
		TestUtils.writeInFile(CLIENT_2);
		TestUtils.writeInFile(SALESMAN_2);
		TestUtils.writeInFile(SALES_2);
		this.executeJob();
		String result = TestUtils.readOutFile();
		assertEquals("2ç2ç11çPedro", result);
	}

	private void executeJob() {
		JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		try {
			jobLauncher.run(job, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
