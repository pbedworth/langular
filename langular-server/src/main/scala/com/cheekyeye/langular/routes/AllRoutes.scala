package com.cheekyeye.langular.routes

import java.util.UUID

import akka.event.Logging
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging

trait AllRoutes extends UserRoute with TextRoute with LazyLogging {

  private def serviceRoutes(currentUserId: UUID): Route =
    userRoute(currentUserId) ~ textRoute(currentUserId)

  val allRoutes: Route =
    handleExceptions(Exceptions.exceptionHandler) {
      logRequestResult("Client REST", Logging.InfoLevel) {
        Auth.authenticate { user =>
          authorize(Auth.hasUserPermissions(user)) {
            serviceRoutes(UUID.fromString(user.id))
          }
        }
      }
    }

}
