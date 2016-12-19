package com.cheekyeye.langular.routes

import akka.http.scaladsl.server.{Directives, Route}
import com.cheekyeye.langular.domain.{JsonFormat, LoginRequest, LoginResponse}
import com.cheekyeye.langular.services.SessionService
import spray.json.{JsObject, JsString}
import akka.http.scaladsl.model.StatusCodes._

trait LoginRoute extends Directives with JsonFormat {

  def loginRoute: Route = pathPrefix("login") {
    pathEndOrSingleSlash {
      post {
        entity(as[LoginRequest]) { request =>
          SessionService.generateLoginToken(request.username, request.password) match {
            case Some(token) => complete(LoginResponse(token))
            case _ => complete(BadRequest, JsObject(Map("status" -> JsString("Wrong username or password"))))
          }
        }
      }
    }
  }

}
