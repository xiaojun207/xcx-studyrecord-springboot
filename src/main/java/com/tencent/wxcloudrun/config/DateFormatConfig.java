package com.tencent.wxcloudrun.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@JsonComponent
public class DateFormatConfig {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern = "yyyy-MM-dd HH:mm:ss";
    @Value("${spring.jackson.time-zone:GMT+8}")
    private String timeZone = "GMT+8";

    @Resource
    LocalDateTimeSerializer localDateTimeSerializer;

    /**
     * @description date 类型全局时间格式化
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        return builder -> {
            DateFormat df = new SimpleDateFormat(pattern);
            df.setTimeZone(TimeZone.getTimeZone(timeZone));
            builder.failOnEmptyBeans(false)
                    .failOnUnknownProperties(false)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .dateFormat(df);
        };
    }

    /**
     * @description LocalDate 类型全局时间格式化
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(TimeZone.getTimeZone(timeZone).toZoneId());
        return new LocalDateTimeSerializer(dateTimeFormatter);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeSerializer).timeZone(TimeZone.getTimeZone(timeZone));
    }
}
