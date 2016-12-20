package com.cheekyeye.langular.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, ExceptionHandler}
import com.typesafe.scalalogging.LazyLogging

object Exceptions extends Directives with LazyLogging {

  val exceptionHandler = ExceptionHandler {
    case ex: Exception =>
      extractUri { uri =>
        logger.error(s"Server error for request '$uri': '$ex'", ex)
        complete(HttpResponse(StatusCodes.InternalServerError, entity = ex.getMessage))
      }
  }

}
