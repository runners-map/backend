package com.service.runnersmap.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class JobScheduler {

  private final JobLauncher jobLauncher;

  private final Job rankJob;

//  @Scheduled(cron = "*/10 * * * * *") // 10초에 한번(개발용)
  @Scheduled(cron = "0 0 0 * * *") // 매일 자정
//  @Scheduled(cron = "0 0 * * * *") // 정각마다 수행
  public void runJob() {
    try {
      LocalDate now = LocalDate.now();
      String nowMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));

      JobParameters jobParameters = new JobParametersBuilder()
          .addLong("time", System.currentTimeMillis())
          .addString("searchMonth", nowMonth)
          .toJobParameters();

      jobLauncher.run(rankJob, jobParameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}