package com.fw.common.entity;

import java.util.List;

public abstract interface GenericService<T, TE, PK> {

    public abstract int countByExample(TE paramTE);

    public abstract int deleteByExample(TE paramTE);

    public abstract int deleteByPrimaryKey(PK paramPK);

    public abstract T insert(T paramT);

    public abstract T insertSelective(T paramT);

    public abstract List<T> selectByExample(TE paramTE);

    public abstract T selectByPrimaryKey(PK paramPK);

    public abstract int updateByExampleSelective(T paramT, TE paramTE);

    public abstract int updateByExample(T paramT, TE paramTE);

    public abstract int updateByPrimaryKeySelective(T paramT);

    public abstract int updateByPrimaryKey(T paramT);

    public abstract List<T> queryAll();
}
