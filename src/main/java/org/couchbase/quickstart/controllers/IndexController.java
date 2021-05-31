package org.couchbase.quickstart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller

public class IndexController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("redirect:/swagger-ui/");
    }
}