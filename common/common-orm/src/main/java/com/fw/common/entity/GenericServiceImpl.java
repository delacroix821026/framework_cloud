package com.fw.common.entity;

import com.fw.common.model.LoginUserInfo;
import com.fw.common.utils.JsonUtils;
import com.fw.common.utils.StringUtils;
import com.fw.common.utils.UserInfoUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class GenericServiceImpl<T, TE, PK> implements GenericService<T, TE, PK> {
    public GenericServiceImpl() {
    }

    protected abstract GenericMapper<T, TE, PK> getGenericMapper();

    @Override
    public int deleteByPrimaryKey(PK id) {
        return getGenericMapper().deleteByPrimaryKey(id);
    }

    @Override
    public T insert(T entity) {
        setClientId(entity);
        setCreateBy(entity);
        setCreateDate(entity);
        setUpdateBy(entity);
        setUpdateDate(entity);
        int flag = getGenericMapper().insert(entity);
        if (flag == 0) {
            return null;
        }
        return entity;
    }

    @Override
    public T insertSelective(T entity) {
        setClientId(entity);
        setCreateBy(entity);
        setCreateDate(entity);
        setUpdateBy(entity);
        setUpdateDate(entity);
        int flag = getGenericMapper().insertSelective(entity);
        if (flag == 0) {
            return null;
        }
        return entity;
    }

    @Override
    public T selectByPrimaryKey(PK id) {
        return getGenericMapper().selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T entity) {
        setUpdateBy(entity);
        setUpdateDate(entity);
        PK id = null;
        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            id = (PK) getIdMethod.invoke(entity);
        } catch (Exception e1) {
            id = null;
            e1.printStackTrace();
        }

        if (id == null) {
            return 0;
        }

        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldGenericType = field.getGenericType().toString();
            if (fieldGenericType.equals("class java.lang.Object")) {
                String fieldName = field.getName();
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                try {
                    Method getMethod = entity.getClass().getMethod("get" + fieldName);
                    Method setMethod = entity.getClass().getMethod("set" + fieldName, field.getClass());
                    Object value = getMethod.invoke(entity);
                    if (value != null) {
                        T oldEntity = selectByPrimaryKey(id);

                        Method getOldEntityMethod = oldEntity.getClass().getMethod("get" + fieldName);
                        Object oldValue = getOldEntityMethod.invoke(oldEntity);
                        if (oldValue != null) {
                            value = JsonUtils.assembleTwoModel(oldValue, value);
                            setMethod.invoke(entity, value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return getGenericMapper().updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateByPrimaryKey(T entity) {

        return getGenericMapper().updateByPrimaryKey(entity);
    }

    @Override
    public List<T> selectByExample(TE entity) {
        return getGenericMapper().selectByExample(entity);
    }

    @Override
    public int countByExample(TE example) {
        return getGenericMapper().countByExample(example);
    }

    @Override
    public int deleteByExample(TE example) {
        return getGenericMapper().deleteByExample(example);
    }

    @Override
    public int updateByExampleSelective(T record, TE example) {
        setUpdateBy(record);
        setUpdateDate(record);
        Map<String, Object> params = Maps.newHashMap();
        params.put("record", record);
        params.put("example", example);
        return getGenericMapper().updateByExampleSelective(params);
    }

    @Override
    public int updateByExample(T record, TE example) {
        setUpdateBy(record);
        setUpdateDate(record);
        Map<String, Object> params = Maps.newHashMap();
        params.put("record", record);
        params.put("example", example);
        return getGenericMapper().updateByExample(params);
    }

    @Override
    public List<T> queryAll() {
        return getGenericMapper().selectByExample(null);
    }

    public void setClientId(T entity) {
        if (StringUtils.isNotEmpty(UserInfoUtils.getClientId())) {
            setEntityFiled(entity, "clientId", UserInfoUtils.getClientId());
        }
    }

    public void setCreateBy(T entity) {
        LoginUserInfo userInfo = UserInfoUtils.getUser();
        if (null != userInfo && userInfo.getId() != null) {
            setEntityFiled(entity, "createBy", userInfo.getId());
        }
    }

    public void setUpdateBy(T entity) {
        LoginUserInfo userInfo = UserInfoUtils.getUser();
        if (null != userInfo && userInfo.getId() != null) {
            setEntityFiled(entity, "updateBy", userInfo.getId());
        }
    }

    public void setCreateDate(T entity) {
        setEntityFiled(entity, "createDate", new Date());
    }

    public void setUpdateDate(T entity) {
        setEntityFiled(entity, "updateDate", new Date());
    }

    public void setEntityFiled(T entity, String newField, Object obj) {
        //首字母改为大写
        newField = newField.substring(0, 1).toUpperCase() + newField.substring(1);
        try {
            // 查看是否有getClientId方法，若有则把当前登录用户的clientid存入进去
            Class c = entity.getClass();
            for (Method method : c.getMethods()) {
                if (("get" + newField).equals(method.getName())) {
                    Object value = method.invoke(entity);
                    if (null == value) {
                        Method setMethod = entity.getClass().getMethod("set" + newField, method.getReturnType());
                        setMethod.invoke(entity, obj);
                    }
                    break;
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
    }
}
