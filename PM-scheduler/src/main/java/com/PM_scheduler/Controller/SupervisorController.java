package com.PM_scheduler.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupervisorController {

    @GetMapping("/Supervisor/login")
    public String login() {
        return "Supervisor-Login";  // your HTML file
    }

    @GetMapping("/Supervisor/home")
    public String supervisorHome() {
        return "ok";
    }
}
