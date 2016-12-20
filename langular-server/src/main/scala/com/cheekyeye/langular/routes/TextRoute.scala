package com.cheekyeye.langular.routes


import java.util.UUID

import akka.Done
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.cheekyeye.langular.services.TextService.{createText, deleteText, fetchText, fetchTexts, updateText}
import com.cheekyeye.langular.domain.Text

trait TextRoute extends Routing {

  def textRoute(currentUserId: UUID): Route =
    pathPrefix("text") {
      pathEnd {
        get {
          complete {
            fetchTexts(currentUserId)
          }
        } ~
          post {
            entity(as[Text]) { text =>
              complete {
                createText(currentUserId, text)
                Done
              }
            }
          } ~
          put {
            entity(as[Text]) { text =>
              complete {
                updateText(currentUserId, text)
                Done
              }
            }
          }
      } ~
        path(JavaUUID) { id =>
          get {
            fetchText(currentUserId, id) match {
              case Some(item) => complete(item)
              case None => complete(StatusCodes.NotFound)
            }
          } ~
            delete {
              complete {
                deleteText(currentUserId, id)
                Done
              }
            }
        }
    }

}
