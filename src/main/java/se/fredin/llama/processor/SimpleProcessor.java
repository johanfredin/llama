package se.fredin.llama.processor;

import org.apache.camel.Exchange;
import se.fredin.llama.bean.LlamaBean;
import se.fredin.llama.utils.LlamaUtils;

import java.util.List;

/**
 * Superclass for "simple" processors that modifies a singe exchange.
 * This could very easily be achieved by calling .stream() on a collection
 * and SHOULD be the way to do it if we have several things we need to do (transform, sort, filter etc).
 * However if we know we simply want to do one thing to our collection and want to
 * have the code a bit less bloated then this could be a bit more elegant.
 * Body of the exchange is expected to contain a {@link List} of type {@link LlamaBean}.
 * @param <T> any class extending {@link LlamaBean}
 */
public abstract class SimpleProcessor<T extends LlamaBean> extends BaseProcessor {

    protected Exchange exchange;

    /**
     * Create a new instance calling super first.
     * @param exchange the exchange to process.
     */
    public SimpleProcessor(Exchange exchange) {
        setExchange(exchange);
    }

    /**
     * @param exchange the exchange to process
     */
    protected void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    /**
     * @return the exchange to process
     */
    public Exchange getExchange() {
        return exchange;
    }

    @Override
    public Exchange getResult() {
        return this.exchange;
    }

    @Override
    public void process() {
        var beans = LlamaUtils.<T>asLlamaBeanList(this.exchange);
        this.exchange.getIn().setBody(processData(beans));
    }

    /**
     * The result of this method will be what we give the exchange.
     * What we do with the passed in collection is decided in the subclasses that
     * will be forced to implement this method.
     * @param beans the beans to modify
     * @return the passed in beans, modified.
     */
    public abstract List<T> processData(List<T> beans);

}
