package com.project.baseapp.domain

import org.springframework.stereotype.Component

@Component
class SessionHolder {
  Map<Integer, Session> sessionMap = [:]
}
