package com.cmacgm.cdrserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String home() {

		return "index";
	}

	@RequestMapping("/{page}") //angular routing
	String partialHandler(@PathVariable("page") final String page) {
		return page;
	}

}