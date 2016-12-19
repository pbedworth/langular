package com.cheekyeye.langular.common

import awscala._
import awscala.dynamodbv2.ProvisionedThroughput
import com.typesafe.config.ConfigFactory

object Config {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  val s3BucketName = config.getString("aws.s3.bucketName")
  val s3Region = Region.apply(config.getString("aws.s3.region"))
  val dynamoProvisionedThroughput = ProvisionedThroughput(config.getInt("aws.dynamo.readCapacityUnits"), config.getInt("aws.dynamo.writeCapacityUnits"))
}
