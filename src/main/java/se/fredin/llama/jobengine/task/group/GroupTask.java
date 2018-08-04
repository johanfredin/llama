package se.fredin.llama.jobengine.task.group;

import org.apache.camel.Exchange;
import se.fredin.llama.jobengine.bean.FxKBean;
import se.fredin.llama.jobengine.task.BaseTask;
import se.fredin.llama.jobengine.utils.JobUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class GroupTask<T extends FxKBean, R extends FxKBean> extends BaseTask {

    private Exchange exchange;
    private List<R> resultList;
    private Function<T, R> groupFunction;


    public GroupTask(Exchange exchange) {
        super();
        this.exchange = exchange;
    }

    @Override
    public Exchange doExecuteTask() {
        for (Map.Entry<Object, List<T>> entry : JobUtils.<T>asMap(this.exchange).entrySet()) {
            addResult(entry);
        }

        this.exchange.getIn().setBody(this.resultList);
        return this.exchange;
    }

    protected void addResult(Map.Entry<Object, List<T>> entry) {
        if (this.resultList == null) {
            this.resultList = new ArrayList<>();
        }
        this.resultList.add(getResult(entry));
    }

    protected abstract R getResult(Map.Entry<Object, List<T>> entry);

    @Override
    public String toString() {
        return "GroupTask{" +
                "exchange=" + exchange +
                ", resultList=" + resultList +
                ", groupFunction=" + groupFunction +
                '}';
    }
}