package se.fredin.llama.jobengine.task;

import org.apache.camel.Exchange;

public interface Task {

    Exchange doExecuteTask();

    int getProcessedRecords();

    void postExecute();

    void postCreate();

    String getTaskName();

}