package com.project.baseapp.controller

import com.google.gson.Gson
import com.project.baseapp.dao.SessionDao
import com.project.baseapp.domain.Player
import com.project.baseapp.domain.Session
import com.project.baseapp.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping('/session')
class SessionController {

  @Autowired
  SessionRepository sessionRepository

  @Autowired
  SessionDao sessionDao

  @GetMapping('/get')
  Session getSession(HttpServletResponse response) {
    int sessionId
    if (sessionRepository.findAll().isEmpty()) {
      sessionId = 10001
    } else {
      sessionRepository.findAll().each {
        if (!sessionId) {
          sessionId = it.sessionId
        }
        if (sessionId < it.sessionId) {
          sessionId = it.sessionId
        }
      }
      sessionId = sessionId + 1
    }
    Session session = new Session(sessionId: sessionId)
    sessionRepository.save(session)
    Cookie sessionCookie = new Cookie('sessionId', sessionId.toString())
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
    Session session = sessionRepository.findOne(sessionId)
    if (playerId) {
      session.players.removeAll { it.playerId == playerId }
    }
    boolean hostExist = session.players.find { it.host }
    if (hostExist) {
      player.host = false
    }
    session.players << player
    sessionRepository.save(session)
    session.players.each { it.point = null }
    sessionDao.clearCache(sessionId)
    Cookie cookie = new Cookie('playerId', newPlayerId)
    cookie.setMaxAge(36000)
    cookie.setPath('/')
    response.addCookie(cookie)
    return session
  }

//  @PostMapping('/join/{name}')
//  Session createSession(@PathVariable('name') String name,
//                        @CookieValue('sessionId') int sessionId, HttpServletResponse response,
//                        @CookieValue(value = 'playerId', required = false) String playerId) {
//    String newPlayerId = UUID.randomUUID().toString()
//    Player player = new Player(
//        playerId: newPlayerId,
//        name: name
//    )
//    Session session = sessionRepository.findOne(sessionId)
//    if (playerId) {
//      session.players.removeAll { it.playerId == playerId }
//    }
//    session.players << player
//    sessionRepository.save(session)
//    session.players.each { it.point = null }
//    sessionDao.clearCache(sessionId)
//    Cookie cookie = new Cookie('playerId', newPlayerId)
//    cookie.setMaxAge(36000)
//    cookie.setPath('/')
//    response.addCookie(cookie)
//    return session
//  }

  @PostMapping('/remove-player/{playerId}')
  Session removePlayer(@CookieValue('sessionId') int sessionId,
                       @PathVariable('playerId') String removePlayerId,
                       @CookieValue(value = 'playerId', required = false) String playerId) {
    Session session = sessionRepository.findOne(sessionId)
    boolean isHost = session.players.find { it.playerId == playerId }.host
    if (isHost) {
      Player player = session.players.find { it.playerId == removePlayerId }
      if (player) {
        session.players.remove(player)
      }
      sessionRepository.save(session)
      sessionDao.clearCache(sessionId)
      session.players.each { it.point = null }
    }
    return session
  }

  @PostMapping('/verify/{sessionId}')
  void verifySession(@PathVariable('sessionId') int sessionId, HttpServletResponse response) {
    Session session = sessionRepository.findOne(sessionId)
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
    Session session = sessionRepository.findOne(sessionId)
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
               @CookieValue('sessionId') int sessionId,
               @CookieValue(value = 'playerId', required = false) String playerId) {
    Session session = sessionRepository.findOne(sessionId)
    boolean isHost = session.players.find { it.playerId == playerId }.host
    if (isHost) {
      sessionDao.clearCache(sessionId)
      session.desc = sessionRequest.desc
      sessionRepository.save(session)
    }
  }

  @PostMapping('/update/point/{point:.+}')
  List<Player> setPoint(@CookieValue('sessionId') int sessionId,
                        @CookieValue('playerId') String playerId,
                        @PathVariable('point') Double point) {
    Session session = sessionRepository.findOne(sessionId)
    Player player = session.players.find { it.playerId == playerId }
    player.point = point
    player.voted = true
    sessionRepository.save(session)
    sessionDao.clearCache(sessionId)
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
  List<Player> clearPoints(@CookieValue('sessionId') int sessionId,
                           @CookieValue(value = 'playerId', required = false) String playerId) {
    Session session = sessionRepository.findOne(sessionId)
    boolean isHost = session.players.find { it.playerId == playerId }.host
    if (isHost) {
      session.players.each {
        it.point = null
        it.voted = false
      }
      session.showVotes = false
      sessionRepository.save(session)
      sessionDao.clearCache(sessionId)
    }
    return session.players
  }

  @PostMapping('/clear-desc')
  List<Player> clearDec(@CookieValue('sessionId') int sessionId,
                        @CookieValue('playerId') String playerId) {
    Session session = sessionRepository.findOne(sessionId)
    boolean isHost = session.players.find { it.playerId == playerId }.host
    if (isHost) {
      session.desc = ""
      sessionRepository.save(session)
      sessionDao.clearCache(sessionId)
      if (!session.showVotes) {
        session.players.each {
          if (it.playerId != playerId) {
            it.point = null
          }
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
      Session session = getSessionFromCache(sessionId)
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
        } else if (matchedPlayer.voted != player.voted || matchedPlayer.host != player.host) {
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

    Session session = getSessionFromCache(sessionId)
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
    Session session = sessionRepository.findOne(sessionId)
    boolean isHost = session.players.find { it.playerId == playerId }.host
    if (isHost) {
      session.showVotes = sessionInDisplay.showVotes
      sessionRepository.save(session)
      sessionDao.clearCache(sessionId)
    }
    return session
  }

  private Session getSessionFromCache(int sessionId) {
    Session sessionFromCache = sessionDao.getSession(sessionId)
    Gson gson = new Gson()
    gson.fromJson(gson.toJson(sessionFromCache), Session.class)
  }

  @PostMapping('/make-host/{playerId}')
  Session makeHost(@PathVariable('playerId') String tobeHostPlayerId,
                   @CookieValue('sessionId') int sessionId,
                   @CookieValue(value = 'playerId', required = false) String playerId) {
    Session session = sessionRepository.findOne(sessionId)
    boolean isHost = session.players.find { it.playerId == playerId }.host
    if (isHost) {
      Player tobeHostPlayer = session.players.find { it.playerId == tobeHostPlayerId }
      tobeHostPlayer.host = true
      sessionRepository.save(session)
      sessionDao.clearCache(sessionId)
    }
    return session
  }

}
