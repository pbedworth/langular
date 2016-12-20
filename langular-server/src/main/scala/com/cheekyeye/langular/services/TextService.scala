package com.cheekyeye.langular.services

import java.util.UUID

import spray.json._
import awscala.dynamodbv2._
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.s3.model.ObjectMetadata
import com.cheekyeye.langular.common._
import com.cheekyeye.langular.domain.{Text, Texts}
import spray.json.JsonParser

object TextService extends Service with DynamoStore with S3Store {
  private val BucketName: String = Config.s3BucketName
  private val TableName = "LangularTexts"
  private val Id = AttributeDefinition("Id", ScalarAttributeType.S)
  private val Title = AttributeDefinition("Title", ScalarAttributeType.S)
  private val UserId = AttributeDefinition("UserId", ScalarAttributeType.S)
  private val UserIndex = GlobalSecondaryIndex(name = "UserIndex",
    keySchema = Seq(KeySchema(UserId.name, KeyType.Hash)),
    projection = Projection(ProjectionType.All),
    provisionedThroughput = Config.dynamoProvisionedThroughput)

  def initSchema(): Unit = {
    if (!tableExists(TableName)) {
      createTable(TableName, Id, None, Seq(Id, UserId), Seq(UserIndex))
    }
    if (!bucketExists(BucketName)) {
      createBucket(BucketName)
    }
  }

  def destroySchema(): Unit = {
    dropTable(TableName)
    dropBucket(BucketName)
  }

  def fetchTexts(userId: UUID): Texts =
    Texts(
      getTable(TableName).map(table =>
        table.queryWithIndex(UserIndex, Seq(UserId.name -> cond.eq(userId.toString)))
             .map(item => (getS(item, Id), getS(item, Title)))
             .map(r => Text(r._1, r._2))).getOrElse(Seq())
    )

  def fetchText(userId: UUID, textId: UUID): Option[Text] =
    if (isTextOwnedByUser(textId, userId)) {
      fetch(textId)
    } else {
      None
    }

  def fetch(textId: UUID): Option[Text] =
    getBucket(BucketName).map(bucket => {
      val s3Obj = bucket.get(s"$textId-text.json")
      JsonParser(s3Obj.toString()).convertTo[Text]
    })

  def isTextOwnedByUser(textId: UUID, userId: UUID): Boolean =
    getTable(TableName).map(table => {
      table.query(Seq(UserId.name -> cond.eq(userId.toString),
                  Id.name -> cond.eq(textId.toString)))
    }).getOrElse(Seq()).nonEmpty

  def createText(userId: UUID, text: Text): Option[UUID] =
    getBucket(BucketName).flatMap(bucket => {
      getTable(TableName).map(table => {
        val textId = uuid()
        val textToWrite = Text(textId.toString, text.title, text.text)
        bucket.putObject(s"$textId-text.json", textToWrite.toJson.prettyPrint.getBytes(), new ObjectMetadata())
        table.put(textId.toString, userId.toString, Title.name -> text.title)
        textId
      })
    })

  def updateText(userId: UUID, text: Text): Unit =
    getBucket(BucketName).foreach(bucket => {
      getTable(TableName).foreach(table => {
        bucket.putObject(s"${text.id}-text.json", text.toJson.prettyPrint.getBytes(), new ObjectMetadata())
        table.put(text.id.toString, userId.toString, Title.name -> text.title)
      })
    })

  def deleteText(userId: UUID, textId: UUID): Unit =
    getBucket(BucketName).foreach(bucket => {
      getTable(TableName).foreach({ table =>
        table.delete(textId.toString)
        bucket.delete(s"$textId-text.json")
      })
    })

}
