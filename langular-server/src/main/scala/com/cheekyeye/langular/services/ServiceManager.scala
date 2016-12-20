package com.cheekyeye.langular.services

import com.cheekyeye.langular.common.Config

object ServiceManager {
  private def Services = Seq(UserService, TextService)

  def initServices(): Unit = {
    if (Config.destroySchemaOnStartup) destroySchema()
    initSchema()
  }

  private def destroySchema(): Unit = Services.foreach(_.destroySchema())

  private def initSchema(): Unit = Services.foreach(_.initSchema())
}
