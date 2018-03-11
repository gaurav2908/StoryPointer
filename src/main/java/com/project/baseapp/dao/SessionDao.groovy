package com.project.baseapp.dao

import com.project.baseapp.domain.Session
import com.project.baseapp.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = "sessionCache")
class SessionDao {

  @Autowired
  SessionRepository sessionRepository

  @Cacheable
  Session getSession(int sessionId) {
    sessionRepository.findOne(sessionId)
  }

  @CacheEvict
  void clearCache(int sessionId) {}
}
