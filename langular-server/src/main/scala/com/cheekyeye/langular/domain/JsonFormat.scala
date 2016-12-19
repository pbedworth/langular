package com.cheekyeye.langular.domain

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonFormat extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val textFormat = jsonFormat3(Text)
  implicit val textsFormat = jsonFormat1(Texts)

  implicit val userFormat = jsonFormat3(User)

  implicit val loginRequestFormat = jsonFormat2(LoginRequest)
  implicit val loginResponseFormat = jsonFormat1(LoginResponse)

  implicit val sessionFormat = jsonFormat3(Session)
}
