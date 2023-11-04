package no.hvl.dat250.feedapp.service.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.service.PollService;

@Service
public class ScheduledTasksService {

    @Autowired
    private PollService pollService;
    
    @Scheduled(cron = "0 0 0 * * ?")  // Every midnight
    public void dailyTask() {
        pollService.dweetPollsEndToday();
        pollService.dweetPollsOpenToday();
        pollService.savePollsEndToday(); //Saving the pollresults  
    }
}