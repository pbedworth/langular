package com.cheekyeye.langular.routes


import akka.http.scaladsl.server.Directives
import com.cheekyeye.langular.domain.JsonFormat

trait Routing extends Directives with JsonFormat {
}
