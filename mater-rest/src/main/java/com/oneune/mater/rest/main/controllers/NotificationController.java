package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.services.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notifications")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {

    NotificationService notificationService;

    @PostMapping("emails/self")
    public void sendSimpleMail(@RequestParam String username,
                               @RequestParam String text) {
        notificationService.sendSimpleMailToSelf(username, text);
    }

    @PostMapping("emails/users")
    public void sendSimpleMail(@RequestParam String destionation,
                               @RequestParam String title,
                               @RequestParam String text) {
        notificationService.sendSimpleMailToUser(destionation, title, text);
    }
}
