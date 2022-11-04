package com.newfangled.flockbackend.global.exception.email;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmailController {

    @GetMapping("/emailInvalid")
    public String emailInvalid() {
        return "emailInvalid";
    }
}
