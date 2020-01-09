package com.fw.common.formatter;

import com.fw.common.annotation.DateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DateFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<DateFormat> {
    private final Set<Class<?>> fieldTypes;
    @Value("${BK.DATEFORMAT.PATTEN}")
    private String patten;

    public DateFormatAnnotationFormatterFactory() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(Date.class);
        this.fieldTypes = set;
    }


    public Set<Class<?>> getFieldTypes() {
        return fieldTypes;
    }


    public Parser<Date> getParser(DateFormat annotation, Class<?> fieldType) {
        return new DateFormatter(annotation.pattern());
    }


    public Printer<Date> getPrinter(DateFormat annotation, Class<?> fieldType) {
        return new DateFormatter(annotation.pattern());
    }

    private class DateFormatter implements Formatter<Date>, Serializable {
        private String cusPattern;

        public DateFormatter(String cusPattern) {
            this.cusPattern = cusPattern;
        }

        private static final long serialVersionUID = -818656464607971661L;


        public String print(Date value, Locale locale) {
            if (value == null)
                return "";
            SimpleDateFormat dateFormat = new SimpleDateFormat(!"".equals(cusPattern) ? cusPattern : patten);
            return dateFormat.format(value);

        }


        public Date parse(String value, Locale locale) throws ParseException {
            if(cusPattern == null || "".equals(cusPattern)) {
                try {
                    return new Date(Long.valueOf(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(!"".equals(cusPattern) ? cusPattern : patten);
            dateFormat.setLenient(false);
            try {
                return dateFormat.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
