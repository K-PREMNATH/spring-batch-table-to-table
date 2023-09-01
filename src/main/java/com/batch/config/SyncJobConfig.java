package com.batch.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SyncJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean(name = "loadSyncRecordJob")
    public Job  loadSyncRecordJob(){
        return jobBuilderFactory.get("loadSyncRecordJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(loadSyncRecordStep()).on(ExitStatus.FAILED.getExitCode()).fail()
                .from(loadSyncRecordStep()).on(ExitStatus.COMPLETED.getExitCode())
                .end()
                .end()
                .build();
    }

    @Bean(name = "loadSyncRecordStep")
    public Step loadSyncRecordStep (){
        return stepBuilderFactory.get("loadSyncRecordStep").tasklet(moveRecordsTasklet()).build();
    }

    @Bean
    public MoveRecordsTasklet moveRecordsTasklet(){
        return new MoveRecordsTasklet();
    }
}
