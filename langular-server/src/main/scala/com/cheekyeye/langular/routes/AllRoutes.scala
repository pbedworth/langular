package com.cheekyeye.langular.routes

import akka.http.scaladsl.server.Route

trait AllRoutes extends LoginRoute with UserRoute with TextRoute {
  val allRoutes: Route = loginRoute ~ userRoute ~ textRoute
}
