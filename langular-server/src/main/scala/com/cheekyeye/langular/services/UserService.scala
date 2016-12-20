package com.cheekyeye.langular.services

import java.util.UUID

import awscala.dynamodbv2.{AttributeDefinition, _}
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.cheekyeye.langular.common.{Config, DynamoStore}
import com.cheekyeye.langular.domain.User

object UserService extends Service with DynamoStore {
  private val TableName = "LangularUsers"
  private val Id = AttributeDefinition("Id", ScalarAttributeType.S)
  private val Username = AttributeDefinition("Username", ScalarAttributeType.S)
  private val Password = AttributeDefinition("Password", ScalarAttributeType.S)
  private val UsernameIndex = GlobalSecondaryIndex(name = "UsernameIndex",
    keySchema = Seq(KeySchema(Username.name, KeyType.Hash)),
    projection = Projection(ProjectionType.All),
    provisionedThroughput = Config.dynamoProvisionedThroughput)

  def initSchema(): Unit =
    if (!tableExists(TableName)) {
      createTable(TableName, Id, None, Seq(Id, Username), Seq(UsernameIndex))
    }

  def destroySchema(): Unit = {
    dropTable(TableName)
  }

  def createUser(user: User): Option[UUID] =
    getTable(TableName).map(table => {
      val userId = uuid()
      table.put(userId.toString, user.username, Password.name -> user.password)
      userId
    })

  def userExists(username: String): Boolean = findUser(username).isDefined

  def findUser(username: String): Option[User] =
    getTable(TableName).flatMap(table => {
      table.queryWithIndex(UsernameIndex, Seq(Username.name -> cond.eq(username)))
           .headOption
           .map(item => User(getS(item, Id), getS(item, Username), getS(item, Password)))
    })

  def findUser(username: String, password: String): Option[User] =
    findUser(username).filter(user => user.password eq password)

}
