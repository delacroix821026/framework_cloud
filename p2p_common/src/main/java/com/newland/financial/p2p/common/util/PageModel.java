package com.newland.financial.p2p.common.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 翻页模板工具类.
 */
@Getter
@Setter
public final class PageModel<T> extends AbstractPageModel<T> implements Serializable {
    /**
     * 当前页.T
     * */
    private Integer pageNum = 1;
    /**
     * 每页显示条数.
     * */
    private Integer pageSize = 5;

    private T model;

    /**
     * 无参构造.
     * */
    public PageModel() {

    }
    /**
     *@param pageNum Integer
     *@param pageSize Integer
     * */
    public PageModel(final Integer pageNum, final Integer pageSize, T model) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.model = model;
    }

}
