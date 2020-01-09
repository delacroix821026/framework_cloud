package com.fw.common.batch.impl;

import com.fw.common.batch.BatchService;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Map;

//@Component
public class BatchServiceImpl<T> implements BatchService<T>, ApplicationContextAware {

    @Override
    public List<T> read(String queryId, Map<String, Object> parameters) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        itemReader.setParameterValues(parameters);
        itemReader.setQueryId(queryId);
        QueryProcessor queryProcessor = new QueryProcessor();
        //Build Step
        Step step = stepBuilderFactory.get("Step_" + queryId).chunk(1000)  //1000次处理提交一次事务
                .reader(itemReader)
                .processor(queryProcessor)
                .faultTolerant()
                //.skipLimit(10)                           //可以异常跳过10次，多了就任务失败
                //.skip(NullPointerException.class)        //空指针异常可以跳过，继续执行
                //.listener(new DemoSkipListener())
                //.taskExecutor(executorReader)
                .build();
        //Build Job
        Job job = jobBuilderFactory.get("Job_" + queryId).preventRestart().incrementer(new RunIdIncrementer()).start(step).build();
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        jobLauncher.run(job, jobParameters);
        return queryProcessor.getList();
    }

    @Override
    public void writer(String queryId, Map<String, Object> parameters, String statementId, ItemProcessor processor) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        itemReader.setParameterValues(parameters);
        itemReader.setQueryId(queryId);
        itemWriter.setStatementId(statementId);
        //Build Step
        Step step = stepBuilderFactory.get("Step7_" + statementId).chunk(1000)  //1000次处理提交一次事务
                .reader(itemReader)
                .processor(processor)
                .writer(itemWriter)
                .faultTolerant()
                //.skipLimit(10)                           //可以异常跳过10次，多了就任务失败
                //.skip(NullPointerException.class)        //空指针异常可以跳过，继续执行
                //.listener(new DemoSkipListener())
                //.taskExecutor(executorWriter)
                .build();
        //Build Job
        Job job = jobBuilderFactory.get("Job7_" + statementId).incrementer(new RunIdIncrementer()).start(step).build();
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        jobLauncher.run(job, jobParameters);
    }

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MyBatisBatchItemWriter itemWriter;

    @Autowired
    private MyBatisPagingItemReader itemReader;

    @Autowired
    @Qualifier("executorForBatchReader")
    private ThreadPoolTaskExecutor executorReader;

    @Autowired
    @Qualifier("executorForBatchWriter")
    private ThreadPoolTaskExecutor executorWriter;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
