package com.fw.common.entity;

import java.util.List;
import java.util.Map;

public abstract interface GenericMapper<T, TE, PK> {
    int countByExample(TE paramTE);

    int deleteByExample(TE paramTE);

    int deleteByPrimaryKey(PK paramPK);

    int insert(T paramT);

    int insertSelective(T paramT);

    List<T> selectByExample(TE paramTE);

    T selectByPrimaryKey(PK paramPK);

    int updateByExampleSelective(Map<String, Object> paramMap);

    int updateByExample(T paramT, TE paramTE);

    int updateByExample(Map<String, Object> paramMap);

    int updateByPrimaryKeySelective(T paramT);

    int updateByPrimaryKey(T paramT);

    T selectOneByExample(TE paramTE);

    int logicalDeleteByPrimaryKey(Integer id);
}
