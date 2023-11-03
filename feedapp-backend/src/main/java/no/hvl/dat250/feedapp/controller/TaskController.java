package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.service.messaging.ScheduledTasksService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ScheduledTasksService scheduledTasksService;

    @GetMapping("/runDailyTask")
    public ResponseEntity<String> runDailyTask() {
        try {
            scheduledTasksService.dailyTask();
            return ResponseEntity.ok("Daily task executed successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Daily task failed to execute.");
        }
    }
}