package com.dotop.smartwater.project.third.server.meterread.client2.rest.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin
@Controller
public class HtmlController {

    private final static Logger logger = LogManager.getLogger(HtmlController.class);

    // http://localhost:35551/meterread-client/toList
    @GetMapping("/toList")
    public String toList(Model model) {
        return "list";
    }


}
