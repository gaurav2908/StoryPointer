package com.project.baseapp.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

//@JsonIgnoreProperties("ipAddress")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
class Player {
  String playerId
  String name
  Double point
  boolean voted
  Boolean host
}
