package se.fredin.llama.jobengine.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public abstract class BaseTask implements Task {

    protected Logger log = LogManager.getLogger(this.getClass());

    protected int processedRecords;

    public BaseTask() {
        postCreate();
    }

    @Override
    public int getProcessedRecords() {
        return processedRecords;
    }

    public void setProcessedRecords(int processedRecords) {
        this.processedRecords = processedRecords;
    }

    public int incProcessedRecords() {
        return this.processedRecords++;
    }

    @Override
    public void postCreate() {
        log.info(getTaskName() + " initiated");
    }

    @Override
    public void postExecute() {
        log.info(getTaskName() + " completed");
        log.info(getProcessedRecords() + " records processed");
    }

    @Override
    public String getTaskName() {
        return this.getClass().getSimpleName();
    }
}