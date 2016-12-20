package com.cheekyeye.langular.domain

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonFormat extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val textFormat: RootJsonFormat[Text] = jsonFormat3(Text)
  implicit val textsFormat: RootJsonFormat[Texts] = jsonFormat1(Texts)
  implicit val userFormat: RootJsonFormat[User] = jsonFormat3(User)
}
