package com.project.baseapp.dao

import com.project.baseapp.common.ApplicationUtil
import com.project.baseapp.domain.Session
import com.project.baseapp.domain.SessionHolder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SessionDao {

  @Autowired
  SessionHolder sessionHolder

  Session createSession() {
    Integer sessionId
    Integer maxSessionId = sessionHolder.sessionMap.keySet()?.max()
    if (maxSessionId) {
      sessionId = maxSessionId + 1
    } else {
      sessionId = 10001
    }
    Session session = new Session(sessionId: sessionId)
    sessionHolder.sessionMap << [(sessionId): session]
    return ApplicationUtil.clone(session) as Session
  }

  Session findSession(Integer id) {
    return ApplicationUtil.clone(sessionHolder.sessionMap.get(id)) as Session
  }

  void updateSession(Session session) {
    Session sessionCopy = ApplicationUtil.clone(session) as Session
    sessionHolder.sessionMap.remove(sessionCopy.sessionId)
    sessionHolder.sessionMap.put(session.sessionId, sessionCopy)
  }
}
