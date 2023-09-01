Spring Batch : Transfer data from one table to another using Spring Batch, with a focus on ensuring data integrity even if errors occur during the transfer process.
------------------------------------------------------------------------------------------------------------

Project Description: Transfer data from one table to another using Spring Batch, with a focus on ensuring data integrity even if errors occur during the transfer process.


Data Transfer: We want to move data from a source table to a target table using Spring Batch.

Error Handling: It's crucial to handle errors during data transfer. If any problems occur in the middle of the process, we should be able to undo the data transfer to prevent data inconsistencies.

Project Structure:

Spring Batch Configuration: Set up Spring Batch components like the Job, Step, Data Source, and error handling.

Source Table: The table where the original data resides.

Target Table: The table where we want to load the data.

We have data in one table, and we want to move it to another table using Spring Batch. However, we need to make sure that if anything goes wrong while moving the data, we can cancel the process to avoid messing up the data.


Transaction Management:
First, ensure that your tasklet method is transactional. You can annotate your tasklet's execute method with @Transactional as shown in the previous example. This annotation ensures that the entire operation is wrapped in a transaction.

@Override
@Transactional
public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
// Tasklet logic here
}

Error Handling and Rollback:

To handle errors and ensure a rollback in case of exceptions, you can use try-catch blocks and throw exceptions when errors occur. Spring Batch will automatically roll back the transaction when an exception is thrown.

@Override
@Transactional
public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
try {
// Tasklet logic here

        // Simulate an error
        if (somethingWentWrong) {
            throw new Exception("An error occurred.");
        }

        return RepeatStatus.FINISHED;
    } catch (Exception e) {
        // Handle the exception, log it, and possibly notify administrators

        // The transaction will be rolled back automatically due to the exception
        throw e; // Re-throw the exception to signal the failure to Spring Batch
    }
}

In the above code, if an exception is thrown within the tasklet, the transaction will be automatically rolled back, ensuring that any changes made within the tasklet are reverted.


CREATE TABLE `sourcetable` (
`ID` int NOT NULL AUTO_INCREMENT,
`SUB_ID` int DEFAULT NULL,
`RCPT_DATE` datetime DEFAULT NULL,
`PD_FOR_MTH` int DEFAULT NULL,
`PIN_NUMBER` varchar(255) DEFAULT NULL,
`TRNS_CLSF_CDE` varchar(255) DEFAULT NULL,
`CON_TYPE_CDE` varchar(255) DEFAULT NULL,
`RCD_STS` varchar(255) DEFAULT NULL,
`CRD_STS` varchar(255) DEFAULT NULL,
`SYNC_STS` varchar(255) DEFAULT NULL,
PRIMARY KEY (`ID`)
) ;

CREATE TABLE `targettable` (
`ID` int NOT NULL AUTO_INCREMENT,
`PIN_NUMBER` varchar(255) DEFAULT NULL,
`TRNS_CLSF_CDE` varchar(255) DEFAULT NULL,
`CON_TYPE_CDE` varchar(255) DEFAULT NULL,
`RCD_STS` varchar(255) DEFAULT NULL,
PRIMARY KEY (`ID`)
) ;

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (1, 1, '2023-08-01 00:00:00', 5, '12345', 'PMT', 'MCON', 'S', 'A', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (2, 2, '2023-08-02 00:00:00', 6, '54321', 'PMT', 'MCON', 'S', 'B', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (3, 3, '2023-08-03 00:00:00', 7, '98765', 'INV', 'MCON', 'S', 'C', null);

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (4, 4, '2023-08-04 00:00:00', 8, '13579', 'PMT', 'MCON', 'S', 'D', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (5, 5, '2023-08-05 00:00:00', 9, '24680', 'INV', 'MCON', 'S', 'E', 'N');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (6, 6, '2023-08-06 00:00:00', 10, '98765', 'PMT', 'MCON', 'S', 'F', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (7, 7, '2023-08-07 00:00:00', 11, '54321', 'PMT', 'MCON', 'S', 'G', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (8, 8, '2023-08-08 00:00:00', 12, '12345', 'INV', 'MCON', 'S', 'H', null);

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (9, 9, '2023-08-09 00:00:00', 13, '24680', 'PMT', 'MCON', 'S', 'I', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (10, 10, '2023-08-10 00:00:00', 14, '98765', 'PMT', 'MCON', 'S', 'J', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (11, 11, '2023-08-11 00:00:00', 15, '54321', 'INV', 'MCON', 'S', 'K', null);

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (12, 12, '2023-08-12 00:00:00', 16, '12345', 'PMT', 'MCON', 'S', 'L', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (13, 13, '2023-08-13 00:00:00', 17, '24680', 'PMT', 'MCON', 'S', 'M', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (14, 14, '2023-08-14 00:00:00', 18, '98765', 'INV', 'MCON', 'S', 'N', null);

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (15, 15, '2023-08-15 00:00:00', 19, '54321', 'PMT', 'MCON', 'S', 'O', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (16, 16, '2023-08-16 00:00:00', 20, '12345', 'PMT', 'MCON', 'S', 'P', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (17, 17, '2023-08-17 00:00:00', 21, '98765', 'INV', 'MCON', 'S', 'Q', 'N');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (18, 18, '2023-08-18 00:00:00', 22, '54321', 'PMT', 'MCON', 'S', 'R', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (19, 19, '2023-08-19 00:00:00', 23, '12345', 'PMT', 'MCON', 'S', 'S', 'S');

INSERT INTO springbatchdb.sourcetable (ID, SUB_ID, RCPT_DATE, PD_FOR_MTH, PIN_NUMBER, TRNS_CLSF_CDE, CON_TYPE_CDE, RCD_STS, CRD_STS, SYNC_STS) VALUES (20, 20, '2023-08-20 00:00:00', 24, '24680', 'PMT', 'MCON', 'S', 'T', 'S');
