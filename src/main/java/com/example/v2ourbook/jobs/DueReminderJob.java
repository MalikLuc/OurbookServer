package com.example.v2ourbook.jobs;

import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.models.GetBookProcess;
import com.example.v2ourbook.models.TestMessage;
import com.example.v2ourbook.repositories.GetBookProcessRepository;
import com.example.v2ourbook.repositories.TestMessageRepository;
import com.example.v2ourbook.services.MessageService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class DueReminderJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DueReminderJob.class);

    @Autowired
    private GetBookProcessRepository getBookProcessRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        /* Get message id recorded by scheduler during scheduling */
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        Long processId = Long.parseLong(dataMap.getString("processId"));
        String uuid = dataMap.getString("uuid");

        log.info("Executing job with processId {}", processId);

        System.out.println("Executing job for processId {}" + processId);

        /* Get message from database by id */
        Optional<GetBookProcess> process = getBookProcessRepository.findById(processId);

//        messageService.
        /* update message visible in database */
//        process.ifPresent(getBookProcess -> getBookProcessRepository.save(getBookProcess));
        /* unschedule or delete after job gets executed */

        try {
            context.getScheduler().deleteJob(new JobKey(uuid));

            TriggerKey triggerKey = new TriggerKey(uuid);

            context.getScheduler().unscheduleJob(triggerKey);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
