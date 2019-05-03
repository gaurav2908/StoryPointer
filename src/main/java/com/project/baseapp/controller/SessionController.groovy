package com.project.baseapp.controller

import com.project.baseapp.dao.SessionDao
import com.project.baseapp.domain.Player
import com.project.baseapp.domain.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping('/session')
class SessionController {

  @Autowired
  SessionDao sessionDao

  @GetMapping('/create')
  Session getSession(HttpServletResponse response) {
    Session session = sessionDao.createSession()
    Cookie sessionCookie = new Cookie('sessionId', session.sessionId.toString())
    sessionCookie.setMaxAge(36000)
    sessionCookie.setPath('/')
    response.addCookie(sessionCookie)
    return session
  }

  @PostMapping('/join')
  Session createSession(@RequestBody Player player,
                        @CookieValue('sessionId') int sessionId, HttpServletResponse response,
                        @CookieValue(value = 'playerId', required = false) String playerId) {
    String newPlayerId = UUID.randomUUID().toString()
    player.playerId = newPlayerId
    Session session = sessionDao.findSession(sessionId)
    if (playerId) {
      session.players.removeAll { it.playerId == playerId }
    }
    session.players << player
    sessionDao.updateSession(session)
    session.players.each { it.point = null }
    Cookie cookie = new Cookie('playerId', newPlayerId)
    cookie.setMaxAge(36000)
    cookie.setPath('/')
    response.addCookie(cookie)
    return session
  }

  @PostMapping('/remove-player/{playerId}')
  Session removePlayer(@CookieValue('sessionId') int sessionId,
                       @PathVariable('playerId') String removePlayerId) {
    Session session = sessionDao.findSession(sessionId)
    Player player = session.players.find { it.playerId == removePlayerId }
    if (player) {
      session.players.remove(player)
    }
    sessionDao.updateSession(session)
    session.players.each { it.point = null }
    return session
  }

  @PostMapping('/verify/{sessionId}')
  void verifySession(@PathVariable('sessionId') int sessionId, HttpServletResponse response) {
    Session session = sessionDao.findSession(sessionId)
    if (session) {
      Cookie cookie = new Cookie('sessionId', sessionId.toString())
      cookie.setMaxAge(36000)
      cookie.setPath('/')
      response.addCookie(cookie)
    } else {
      response.setStatus(400)
    }
  }

  @GetMapping('/players')
  List<Player> getPlayers(@CookieValue('sessionId') int sessionId,
                          @CookieValue(value = 'playerId', required = false) String playerId) {
    sleep(50)
    Session session = sessionDao.findSession(sessionId)
    if (!session.showVotes) {
      session.players.each {
        if (it.playerId != playerId) {
          it.point = null
        }
      }
    }
    return session.players
  }

  @PostMapping('/update/desc')
  void setDesc(@RequestBody Session sessionRequest,
               @CookieValue('sessionId') int sessionId) {
    Session session = sessionDao.findSession(sessionId)
    session.desc = sessionRequest.desc
    sessionDao.updateSession(session)
  }

  @PostMapping('/update/point/{point:.+}')
  List<Player> setPoint(@CookieValue('sessionId') int sessionId,
                        @CookieValue('playerId') String playerId,
                        @PathVariable('point') Double point) {
    Session session = sessionDao.findSession(sessionId)
    Player player = session.players.find { it.playerId == playerId }
    player.point = point
    player.voted = true
    sessionDao.updateSession(session)
    if (!session.showVotes) {
      session.players.each {
        if (it.playerId != playerId) {
          it.point = null
        }
      }
    }
    return session.players
  }

  @PostMapping('/clear-points')
  List<Player> clearPoints(@CookieValue('sessionId') int sessionId) {
    Session session = sessionDao.findSession(sessionId)
    session.players.each {
      it.point = null
      it.voted = false
    }
    session.showVotes = false
    sessionDao.updateSession(session)
    return session.players
  }

  @PostMapping('/clear-desc')
  List<Player> clearDec(@CookieValue('sessionId') int sessionId,
                        @CookieValue('playerId') String playerId) {
    Session session = sessionDao.findSession(sessionId)
    session.desc = ""
    sessionDao.updateSession(session)
    if (!session.showVotes) {
      session.players.each {
        if (it.playerId != playerId) {
          it.point = null
        }
      }
    }
    return session.players
  }

  @PostMapping('/update-game')
  Session updateGame(@CookieValue('sessionId') int sessionId,
                     @RequestBody Session sessionInDisplay,
                     @CookieValue('playerId') String playerId) {
    long startTime = System.currentTimeMillis()
    boolean sessionUpdated
    boolean showVotes
    while (true) {
      Session session = sessionDao.findSession(sessionId)
      showVotes = session.showVotes
      sessionUpdated = !session.desc?.equals(sessionInDisplay.desc) ||
          (session.players?.size() != sessionInDisplay?.players?.size()) ||
          (session.showVotes != sessionInDisplay.showVotes)
      if (sessionUpdated) {
        break
      }
      session.players.find {
        Player player = it
        Player matchedPlayer = sessionInDisplay.players.find { it.playerId == player.playerId }
        if (!matchedPlayer) {
          sessionUpdated = true
          return true
        } else if (matchedPlayer.voted != player.voted) {
          sessionUpdated = true
          return true
        } else if (matchedPlayer.point && matchedPlayer.point != player.point) {
          sessionUpdated = true
          return true
        }
      }
      if (sessionUpdated) {
        break
      }
      long endTime = System.currentTimeMillis()
      long elapsedTime = endTime - startTime
      if (elapsedTime > 55000) {
        break
      }
      sleep(500)
    }

    Session session = sessionDao.findSession(sessionId)
    if (!showVotes) {
      session.players.each {
        if (it.playerId != playerId) {
          it.point = null
        }
      }
    }

    return session
  }

  @PostMapping('/update-showvotes')
  Session updateshowVotes(@CookieValue('sessionId') int sessionId,
                          @RequestBody Session sessionInDisplay,
                          @CookieValue(value = 'playerId', required = false) String playerId) {
    Session session = sessionDao.findSession(sessionId)
    session.showVotes = sessionInDisplay.showVotes
    sessionDao.updateSession(session)
    return session
  }

}
