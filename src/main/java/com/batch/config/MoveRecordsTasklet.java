package com.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class MoveRecordsTasklet  implements Tasklet, StepExecutionListener {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        StepExecution stepExecution = stepContribution.getStepExecution();
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        String jobName = stepExecution.getJobParameters().getString("jobName");
        log.info("execute | IN MoveRecordsTasklet jobName : {}",jobName);

        String timeInMS = stepExecution.getJobParameters().getString("timeInMS");
        log.info("execute | IN MoveRecordsTasklet timeInMS : {}",timeInMS);

        String insertQuery = "INSERT INTO TargetTable (ID, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS) " +
                "SELECT ID, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS " +
                "FROM SourceTable " +
                "WHERE CON_TYPE_CDE = 'MCON' AND TRNS_CLSF_CDE = 'PMT' AND RCD_STS = 'S' " +
                "AND (SYNC_STS IS NULL OR SYNC_STS = 'N')";
        int insertedCount = jdbcTemplate.update(insertQuery);

        if (insertedCount > 0) {
            String updateQuery = "UPDATE SourceTable " +
                    "SET SYNC_STS = 'S' " +
                    "WHERE CON_TYPE_CDE = 'MCON' AND TRNS_CLSF_CDE = 'PMT' " +
                    "AND RCD_STS = 'S' AND (SYNC_STS IS NULL OR SYNC_STS = 'N')";

            jdbcTemplate.update(updateQuery);
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return new ExitStatus(ExitStatus.COMPLETED.getExitCode(),"Job seems completed");
    }
}
