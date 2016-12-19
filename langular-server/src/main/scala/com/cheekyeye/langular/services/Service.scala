package com.cheekyeye.langular.services

import com.cheekyeye.langular.domain.JsonFormat
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

trait Service extends JsonFormat with LazyLogging {
  implicit val executionContext: ExecutionContext = ExecutionContext.global
}
