package com.fw.common.fegin;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateFormatRegister implements FeignFormatterRegistrar {

    public DateFormatRegister() {

    }

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverter(Date.class, String.class, new Date2StringConverter());
    }

    private class Date2StringConverter implements Converter<Date,String> {

        @Override
        public String convert(Date source) {
            if( source != null)
                return String.valueOf(source.getTime());
            return null;
        }

    }
}
