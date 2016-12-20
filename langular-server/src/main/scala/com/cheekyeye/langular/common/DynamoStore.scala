package com.cheekyeye.langular.common

import java.util.UUID

import awscala.dynamodbv2.{AttributeDefinition, _}
import com.amazonaws.services.{dynamodbv2 => aws}
import com.typesafe.scalalogging.LazyLogging

trait DynamoStore extends LazyLogging {
  implicit val dynamoDB: DynamoDB = DynamoDB.at(Config.s3Region)

  def uuid(): UUID = UUID.randomUUID

  def getTable(tableName: String): Option[Table] = dynamoDB.table(tableName)

  def tableExists(tableName: String): Boolean = getTable(tableName).isDefined

  def createTable(tableName: String,
                  hashPK: AttributeDefinition,
                  rangePK: Option[AttributeDefinition],
                  attributes: Seq[AttributeDefinition],
                  globalSecondaryIndexes: Seq[GlobalSecondaryIndex] = Seq()): TableMeta = {
    val createdTableMeta: TableMeta = dynamoDB.create(Table(
        name = tableName,
        hashPK = hashPK.name,
        rangePK = rangePK.map(_.name),
        attributes = attributes,
        localSecondaryIndexes = Seq(),
        globalSecondaryIndexes = globalSecondaryIndexes,
        provisionedThroughput = Some(Config.dynamoProvisionedThroughput)
    ))
    logger.info(s"Created Table: $createdTableMeta")
    waitForTableActivation(createdTableMeta)
    createdTableMeta
  }

  def dropTable(tableName: String): Unit = {
    getTable(tableName).foreach(table => dynamoDB.delete(table))
  }

  private def waitForTableActivation(createdTableMeta: TableMeta) = {
    logger.info(s"Waiting for DynamoDB table ${createdTableMeta.name} activation...")
    var isTableActivated = false
    while (!isTableActivated) {
      dynamoDB.describe(createdTableMeta.table).foreach { meta =>
        isTableActivated = meta.status == aws.model.TableStatus.ACTIVE
      }
      Thread.sleep(1000L)
      logger.info(".")
    }
    logger.info("")
    logger.info(s"Created DynamoDB table ${createdTableMeta.name} has been activated.")
  }

  def getS(item: Item, ad: AttributeDefinition): String = item.attributes.find(_.name eq ad.name).get.value.getS

  def getN(item: Item, ad: AttributeDefinition): Long = item.attributes.find(_.name eq ad.name).get.value.getN.toLong

}
