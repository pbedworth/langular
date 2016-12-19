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

  {
    if (!tableExists(TableName)) {
      createTable(TableName, Id, Some(UserId), Seq(Id, UserId))
    }
    if (!bucketExists(BucketName)) {
      createBucket(BucketName);
    }
  }

  def fetchTexts(userId: UUID): Texts = {
    val texts: Seq[Text] = getTable(TableName).get
      .query(Seq(UserId.name -> cond.eq(userId.toString)))
      .map(item => (item.attributes.find(_.name eq Id.name).get.value.getS,
        item.attributes.find(_.name eq Title.name).get.value.getS))
      .map(r => Text(r._1, r._2))
    Texts(texts)
  }

  def fetchText(userId: UUID, itemId: UUID): Option[Text] = {
    val items: Seq[Item] = getTable(TableName).get
      .query(Seq(UserId.name -> cond.eq(userId.toString),
        Id.name -> cond.eq(itemId.toString)))
    if (items.isEmpty) {
      None
    } else {
      getBucket(BucketName)
        .map(bucket => {
          val s3Obj = bucket.get(s"${itemId}-text.json")
          JsonParser(s3Obj.toString()).convertTo[Text]
        })
    }
  }

  def createText(userId: UUID, text: Text): Unit =
    getBucket(BucketName)
      .map(bucket => {
        val table = getTable(TableName).get
        val textId = uuid
        val textToWrite = Text(textId.toString, text.title, text.text)
        bucket.putObject(s"${textId}-text.json", textToWrite.toJson.prettyPrint.getBytes(), new ObjectMetadata())
        table.put(textId.toString, userId.toString, Title.name -> text.title)
      })

  def updateText(userId: UUID, text: Text): Unit =
    getBucket(BucketName)
      .map(bucket => {
        val table = getTable(TableName).get
        bucket.putObject(s"${text.id}-text.json", text.toJson.prettyPrint.getBytes(), new ObjectMetadata())
        table.put(text.id.toString, userId.toString, Title.name -> text.title)
      })

  def deleteText(userId: UUID, textId: UUID): Unit =
    getBucket(BucketName)
      .map(bucket => {
        val table = getTable(TableName).get
        table.delete(textId.toString)
        bucket.delete(s"${textId}-text.json")
      })

  def currentUserId() = new UUID(1, 0)
}
