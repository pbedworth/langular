package com.cheekyeye.langular.common

import awscala.s3.{Bucket, S3}
import com.typesafe.scalalogging.LazyLogging

trait S3Store extends LazyLogging {
  implicit val s3:S3 = S3.at(Config.s3Region)

  def bucketExists(bucketName: String): Boolean = getBucket(bucketName).isDefined

  def getBucket(bucketName: String) : Option[Bucket] = {
    val bucketOpt = s3.bucket(bucketName)
    if (bucketOpt.isDefined) {
      bucketOpt
    } else {
      logger.info(s"Could not find bucket: $bucketName")
      None
    }
  }

  def createBucket(bucketName: String): Bucket = {
    logger.info(s"Creating bucket: $bucketName")
    val bucket = s3.createBucket(bucketName)
    logger.info(s"Created bucket ${bucket.name}")
    bucket
  }

  def dropBucket(bucketName: String): Unit = {
    getBucket(bucketName).foreach(bucket => s3.delete(bucket))
  }

}
