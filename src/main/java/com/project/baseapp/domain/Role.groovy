package com.project.baseapp.domain

enum Role {
  BE('Backend'), FE('Frontend'), QA('QA'), OTHER('Other')

  private String value

  private Role(String value) {
    this.value = value
  }

  String getValue() {
    this.value
  }
}