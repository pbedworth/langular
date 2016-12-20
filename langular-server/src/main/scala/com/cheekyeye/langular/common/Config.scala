package com.cheekyeye.langular.common

import awscala._
import awscala.dynamodbv2.ProvisionedThroughput
import com.typesafe.config.{Config, ConfigFactory}

object Config {
  val config: Config = ConfigFactory.load()
  val host: String = config.getString("http.host")
  val port: Int = config.getInt("http.port")
  val destroySchemaOnStartup: Boolean = config.getBoolean("destroySchemaOnStartup")
  val s3BucketName: String = config.getString("aws.s3.bucketName")
  val s3Region: Region = Region.apply(config.getString("aws.s3.region"))
  val dynamoProvisionedThroughput: ProvisionedThroughput = ProvisionedThroughput(config.getInt("aws.dynamo.readCapacityUnits"), config.getInt("aws.dynamo.writeCapacityUnits"))
}
