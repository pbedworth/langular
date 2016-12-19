package com.cheekyeye.langular.services

import awscala.dynamodbv2.{AttributeDefinition, _}
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.cheekyeye.langular.common.{Config, DynamoStore}
import com.cheekyeye.langular.domain.{User}

object UserService extends Service with DynamoStore {
  private val BucketName: String = Config.s3BucketName
  private val TableName = "LangularUsers"
  private val Id = AttributeDefinition("Id", ScalarAttributeType.S)
  private val Username = AttributeDefinition("Username", ScalarAttributeType.S)
  private val Password = AttributeDefinition("Password", ScalarAttributeType.S)

  {
    if (!tableExists(TableName)) {
      createTable(TableName, Id, Some(Username), Seq(Id, Username))
    }
  }

  def createUser(user: User): Unit = {
    val table = getTable(TableName).get
    val userId = uuid
    table.put(userId.toString, userId.toString, Username.name -> user.username, Password.name -> user.password)
  }

  def findUser(username: String, password: String): Option[User] = {
    getTable(TableName).get
      .query(Seq(Username.name -> cond.eq(username),
        Password.name -> cond.eq(password)))
      .map(item => User(item.attributes.find(_.name eq Id.name).get.value.getS,
        item.attributes.find(_.name eq Username.name).get.value.getS,
        item.attributes.find(_.name eq Password.name).get.value.getS))
    match {
      case List(user) => Some(user)
      case List() => None
    }
  }
}
