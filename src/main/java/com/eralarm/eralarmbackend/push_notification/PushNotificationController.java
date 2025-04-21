//package com.eralarm.eralarmbackend.push_notification;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/notifications")
//@RequiredArgsConstructor
//public class PushNotificationController {
//
//    private final PushNotificationService pushNotificationService;
//
//    @PostMapping("/send")
//    public String send(@RequestParam String token,
//                       @RequestParam String title,
//                       @RequestParam String body) {
//        try {
//            pushNotificationService.sendMessageTo(token, title, body);
//            return "알림 전송 완료";
//        } catch (Exception e) {
//            return "전송 실패: " + e.getMessage();
//        }
//    }
//}
