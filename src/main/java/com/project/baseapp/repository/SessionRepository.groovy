package com.project.baseapp.repository

import com.project.baseapp.domain.Session
import org.springframework.data.repository.CrudRepository

interface SessionRepository extends CrudRepository<Session, String>{

}