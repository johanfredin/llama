package com.github.johanfredin.llama.processor.bean;

import org.apache.camel.Exchange;
import com.github.johanfredin.llama.bean.LlamaBean;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Filters a collection of llama beans based on the predicate passed in.
 * @param <T> any class extending {@link LlamaBean}
 */
public class FilterBeansBeanProcessor<T extends LlamaBean> extends SimpleBeanProcessor<T> {

    private Predicate<T> function;

    /**
     * Create a new instance
     * @param exchange the exchange whose body we are going to filter
     * @param function the filter function to apply.
     */
    public FilterBeansBeanProcessor(Exchange exchange, Predicate<T> function) {
        super(exchange);
        setFunction(function);
    }

    /**
     * @param function the filter function to apply.
     */
    private void setFunction(Predicate<T> function) {
        this.function = function;
    }

    /**
     * @return the filter function applied.
     */
    public Predicate<T> getFunction() {
        return function;
    }

    @Override
    public List<T> processData(List<T> beans) {
        return beans
                .stream()
                .filter(this.function)
                .collect(Collectors.toList());

    }

}