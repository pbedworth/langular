package com.cheekyeye.langular.services

import akka.Done
import awscala.dynamodbv2.{AttributeDefinition, cond}
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.cheekyeye.langular.common.{Config, DynamoStore}
import com.cheekyeye.langular.domain.{Session}

import scala.concurrent.{Future}

object SessionService extends Service with DynamoStore {
  private val BucketName: String = Config.s3BucketName
  private val TableName = "LangularSessions"
  private val Id = AttributeDefinition("Id", ScalarAttributeType.S)
  private val Accessed = AttributeDefinition("Accessed", ScalarAttributeType.N)
  private val UserId = AttributeDefinition("UserId", ScalarAttributeType.S)

  {
    if (!tableExists(TableName)) {
      createTable(TableName, Id, Some(Accessed), Seq(Id, Accessed, UserId))
    }
  }

  def generateLoginToken(username: String, password: String): Option[String] =
    UserService.findUser(username, password).map(user => {
      val token = java.util.UUID.randomUUID().toString
      token
    })

  def findSession(id: String): Option[Session] =
    getTable(TableName).get
      .query(Seq(Id.name -> cond.eq(id)))
      .map(item => Session(id,
        item.attributes.find(_.name eq Accessed.name).get.value.getN.toLong,
        item.attributes.find(_.name eq UserId.name).get.value.getS))
    match {
      case List(session) => Some(session)
      case List() => None
    }

  def touchSession(id: String, userId: String): Unit =
    getTable(TableName)
      .map(table => {
        table.put(id.toString, Accessed.name -> System.currentTimeMillis(), UserId.name -> userId)
      })


}

