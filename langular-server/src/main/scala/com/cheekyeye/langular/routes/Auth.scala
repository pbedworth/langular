package com.cheekyeye.langular.routes

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.directives.{AuthenticationDirective, Credentials}
import akka.http.scaladsl.server.directives.Credentials.Missing
import com.cheekyeye.langular.domain.{Session, User}
import com.cheekyeye.langular.services.UserService

import scala.util.DynamicVariable

object Auth extends Directives {
  private def Realm = "Langular"

  private def authenticator(credentials: Credentials): Option[User] =
    credentials match {
      case p@Credentials.Provided(username) =>
        getSecret(username).filter(s => p.verify(s)).map(s => User("", username, s))
      case Missing => None
    }

  val authenticate: AuthenticationDirective[User] = authenticateBasic(realm = Realm, authenticator)

  private def getSecret(username: String): Option[String] = UserService.findUser(username).map(_.username)

  def hasUserPermissions(user: User): Boolean = UserService.userExists(user.username)
}
