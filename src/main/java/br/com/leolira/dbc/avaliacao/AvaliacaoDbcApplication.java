package br.com.leolira.dbc.avaliacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class AvaliacaoDbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvaliacaoDbcApplication.class, args);
	}
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void perform() throws Exception
	{
		System.out.println("rodando");
	}

}
