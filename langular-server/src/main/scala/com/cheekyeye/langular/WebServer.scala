package com.cheekyeye.langular

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import com.cheekyeye.langular.common.Config
import com.cheekyeye.langular.routes.AllRoutes
import com.cheekyeye.langular.services.{ServiceManager, TextService, UserService}
import com.typesafe.scalalogging.LazyLogging

import scala.io.StdIn

object WebServer extends AllRoutes with LazyLogging {

  def main(args: Array[String]) {
    // Needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // Needed for the future map/flatmap in the end
    implicit val executionContext = system.dispatcher

    ServiceManager.initServices()

    val bindingFuture = Http().bindAndHandle(allRoutes, Config.host, Config.port)

    println(s"Server online at http://${Config.host}:${Config.port}/\nPress RETURN to stop...")
    StdIn.readLine() // Let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // Trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }

}
