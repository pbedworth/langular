package com.cheekyeye.langular.routes

import akka.Done
import akka.http.scaladsl.server.{Directives, Route}
import com.cheekyeye.langular.domain.{JsonFormat, User}
import com.cheekyeye.langular.services.UserService

import scala.concurrent.Future

trait UserRoute extends Directives with JsonFormat {

  def userRoute: Route =
    pathPrefix("user") {
      pathEnd {
        post {
          entity(as[User]) { user =>
            complete {
              UserService.createUser(user)
              Done
            }
          }
        }
      }
    }
}
