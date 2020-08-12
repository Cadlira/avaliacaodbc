package br.com.leolira.dbc.avaliacao.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import br.com.leolira.dbc.avaliacao.utils.Constants;
import br.com.leolira.dbc.avaliacao.utils.IOUtils;

@Service
@Profile("!TEST")
public class ExecutionService {
	private static final Log logger = LogFactory.getLog(ExecutionService.class);

	private static final String JOB_ID = "JobID";
	private JobLauncher jobLauncher;
	private Job job;
	private WatchService watchService;
	private boolean waitingOthersFilesForAnalize = false;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	public ExecutionService(JobLauncher jobLauncher, Job job) throws IOException, InterruptedException {
		this.jobLauncher = jobLauncher;
		this.job = job;

		this.initializerDirectoryWatch();
	}

	private void initializerDirectoryWatch() throws IOException, InterruptedException {
		this.executeAnalizeDataJob();
		this.inicializeWatchService();
		this.registerPathToWatch();
		this.startWatchingPath();
	}

	private void inicializeWatchService() throws IOException {
		this.watchService = FileSystems.getDefault().newWatchService();
	}

	private void registerPathToWatch() throws IOException {
		Path path = IOUtils.getPathAndCrateDirectories(Constants.IN_DIR);
		path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
	}

	private void startWatchingPath() throws InterruptedException {
		WatchKey key = null;
		while ((key = watchService.take()) != null) {
			processEventKeyLoop(key);
			key.reset();
		}
	}

	private void processEventKeyLoop(WatchKey key) {
		for (WatchEvent<?> event : key.pollEvents()) {
			if (canExecute(event)) {
				schedulerJobExecution();
			}
		}
	}

	private void schedulerJobExecution() {
		this.waitingOthersFilesForAnalize = true;
		scheduler.schedule(() -> {
			executeAnalizeDataJob();
		}, 2, TimeUnit.SECONDS);
	}

	private boolean canExecute(WatchEvent<?> event) {
		return !this.waitingOthersFilesForAnalize && isValidFile(event.context().toString());
	}

	private boolean isValidFile(String file) {
		return (file.endsWith(Constants.IN_FILE_EXTENSION) && !file.endsWith(Constants.OUT_FILE_EXTENSION));
	}

	private void executeAnalizeDataJob() {
		this.waitingOthersFilesForAnalize = false;
		JobParameters params = new JobParametersBuilder().addString(JOB_ID, String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		try {
			jobLauncher.run(job, params);
		} catch (Exception e) {
			logger.error("Ocorreu um erro inexperado na execução do job de analise de dados!", e);
		}
	}
}
