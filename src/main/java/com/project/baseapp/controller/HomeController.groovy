package com.project.baseapp.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController {

  @RequestMapping("/join")
  String renderJoin() {
    return "index"
  }

  @RequestMapping("/game")
  String renderGame() {
    return "index"
  }

  @RequestMapping("/about")
  String renderAbout() {
    return "index"
  }

  @RequestMapping("/contact")
  String renderContact() {
    return "index"
  }

}
