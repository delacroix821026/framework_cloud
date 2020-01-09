package com.fw.common.batch.impl;

import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QueryProcessor<I, O> implements ItemProcessor<I, O> {
    private List<I> list = new ArrayList();

    @Override
    public O process(I item) throws Exception {
        list.add(item);
        return null;
    }
}
