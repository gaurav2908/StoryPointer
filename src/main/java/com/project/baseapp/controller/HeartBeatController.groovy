package com.project.baseapp.controller

import org.springframework.web.bind.annotation.*

@RestController
class HeartBeatController {

  @RequestMapping('/heartbeat')
  Map getHeartbeat() {
    return [applicationName   : 'Story Pointer',
            applicationVersion: '1.0.0']
  }
}
