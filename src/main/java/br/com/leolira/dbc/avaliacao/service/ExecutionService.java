package br.com.leolira.dbc.avaliacao.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.leolira.dbc.avaliacao.utils.Constants;

@Service
public class ExecutionService {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	private WatchService watchService;
	private boolean waitingExecution = false;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	@PostConstruct
	private void initializerDirectoryWatch() throws Exception {
		this.executeJob();
		this.watchService = FileSystems.getDefault().newWatchService();
		this.registerPath();
		this.startWatchingDirectory();
	}

	private void registerPath() throws IOException {
		Path path = this.getPath();
		path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
	}

	private Path getPath() throws IOException {
		Path path = Paths.get(Constants.IN_DIR);
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}
		return path;
	}

	private void startWatchingDirectory() throws Exception {
		WatchKey key;
		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				if (!this.waitingExecution && isValidFile(event.context().toString())) {
					this.waitingExecution = true;
					scheduler.schedule(() -> {
						this.waitingExecution = false;
						executeJob();
					}, 2, TimeUnit.SECONDS);
				}
			}

			key.reset();
		}
	}

	private boolean isValidFile(String file) {
		return (file.endsWith(".dat") && !file.endsWith(".done.dat"));
	}

	private void executeJob() {
		JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		System.out.println("Processando arquivos...");
		try {
			jobLauncher.run(job, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
