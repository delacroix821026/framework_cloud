package com.fw.common.config;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BatchConfigration {
    @Bean
    @Scope("prototype")
    //@StepScope
    public MyBatisPagingItemReader getItemReader(SqlSessionFactory sqlSessionFactory) {//, @Value("#{jobParameters['queryId']}") String queryId
        MyBatisPagingItemReader myBatisPagingItemReader = new MyBatisPagingItemReader();
        myBatisPagingItemReader.setSqlSessionFactory(sqlSessionFactory);
        myBatisPagingItemReader.setPageSize(100);
        myBatisPagingItemReader.setQueryId("");//queryId
        return myBatisPagingItemReader;
    }

    @Bean
    @Scope("prototype")
    //@StepScope
    public MyBatisBatchItemWriter getItemWriter(SqlSessionFactory sqlSessionFactory) {//, @Value("#{jobParameters['statementId']}") String statementId
        MyBatisBatchItemWriter myBatisBatchItemWriter = new MyBatisBatchItemWriter();
        myBatisBatchItemWriter.setSqlSessionFactory(sqlSessionFactory);
        myBatisBatchItemWriter.setSqlSessionTemplate(new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH));
        myBatisBatchItemWriter.setStatementId("");//statementId
        return myBatisBatchItemWriter;
    }

    @Bean
    @Scope("prototype")
    //@StepScope
    public MyBatisCursorItemReader getCursorItemReader(SqlSessionFactory sqlSessionFactory) {//, @Value("#{jobParameters['statementId']}") String statementId
        MyBatisCursorItemReader myBatisCursorItemReader = new MyBatisCursorItemReader();
        myBatisCursorItemReader.setSqlSessionFactory(sqlSessionFactory);
        myBatisCursorItemReader.setQueryId("");//queryId
        return myBatisCursorItemReader;
    }

    /*@Bean("executorForBatchReader")
    public ThreadPoolTaskExecutor executorReader(@Value("${BK.EXECUTOR.BATCH.READER.MAXPOOLSIZE}") Integer maxPoolSize,
                             @Value("${BK.EXECUTOR.BATCH.READER.QUEUECAPACITY}") Integer queueCapacity) {
        return getExecutor(maxPoolSize, queueCapacity);
    }

    @Bean("executorForBatchWriter")
    public ThreadPoolTaskExecutor executorWriter(@Value("${BK.EXECUTOR.BATCH.WRITER.MAXPOOLSIZE}") Integer maxPoolSize,
                             @Value("${BK.EXECUTOR.BATCH.WRITER.QUEUECAPACITY}") Integer queueCapacity) {
        return getExecutor(maxPoolSize, queueCapacity);
    }

    private ThreadPoolTaskExecutor getExecutor(int maxPoolSize, int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ContextAwarePoolExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }*/
}
