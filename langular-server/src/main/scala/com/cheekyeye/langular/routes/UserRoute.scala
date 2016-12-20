package com.cheekyeye.langular.routes

import java.util.UUID

import akka.Done
import akka.http.scaladsl.server.Route
import com.cheekyeye.langular.domain.User
import com.cheekyeye.langular.services.UserService

trait UserRoute extends Routing {

  def userRoute(currentUserId: UUID): Route =
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
