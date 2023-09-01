package com;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchTableToTableApplication implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchTableToTableApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
				.addString("jobName","loadSyncRecordJob")
				.addString("timeInMS",String.valueOf(System.currentTimeMillis()))
				.toJobParameters();

		jobLauncher.run(job,jobParameters);
		System.exit(0);

	}
}
