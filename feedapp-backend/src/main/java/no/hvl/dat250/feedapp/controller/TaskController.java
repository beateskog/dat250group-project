package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.service.messaging.ScheduledTasksService;

/**
 * Controller for running scheduled tasks manually
 * @return
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ScheduledTasksService scheduledTasksService;

    /**
     * Runs the daily task manually
     * @return a response entity with a message if the task was successful or not
     */
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