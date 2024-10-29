//package com.service.runnersmap.config;
//
//import com.service.runnersmap.service.RankService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class BatchJobConfig {
//
//  private final String JOB_NAME = "rankJob";
//
//  private final String STEP_NAME = "rankStep";
//
//  private final RankService rankService;
//
//  /**
//   * Job 등록
//   */
//  @Bean
//  public Job rankJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//    return new JobBuilder(JOB_NAME, jobRepository)
//        .incrementer(new RunIdIncrementer()) // sequential id
//        .start(rankStep(jobRepository, transactionManager)) // step 설정
//        .build();
//  }
//
//  /**
//   * Step 등록
//   */
//  @Bean
//  @JobScope
//  public Step rankStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//    return new StepBuilder(STEP_NAME, jobRepository)
//        .tasklet(testTasklet(), transactionManager) // tasklet 설정
//        .build();
//  }
//
//  /**
//   * Tasklet 수행
//   */
//  @Bean
//  @StepScope
//  public Tasklet testTasklet() {
//    return new Tasklet() {
//      @Override
//      public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
//        throws Exception {
//        log.info("[Runners-Batch] rankJob 시작");
//
//        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution()
//            .getJobParameters();
//        String searchMonth = jobParameters.getString("searchMonth");
//
//        int year = Integer.parseInt(searchMonth.substring(0, 4));
//        int month = Integer.parseInt(searchMonth.substring(4, 6));
//
//        log.info("[Runners-Batch] {}년 {}월 기준 데이터 집계 ", year, month);
//        rankService.saveRankingByMonth(year, month);
//
//        log.info("[Runners-Batch] rankJob 종료");
//        return RepeatStatus.FINISHED; // 작업에 대한 Status 명시
//      }
//    };
//  }
//}