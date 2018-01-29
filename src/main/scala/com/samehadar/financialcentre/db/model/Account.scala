package com.samehadar.financialcentre.db.model

import org.joda.time.DateTime
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder

/**
  * Created by vital on 28.01.2018.
  */
case class Account(accountId: Int, account: String, name: String, accountType: String, currency: String, blocked: Boolean, closed: Boolean,
                  createDate: DateTime, closeDate: Option[DateTime], comment: String, userInfoId: Int, version: Int, restriction: Option[String])
//  require(accountType != null && currency != null && createDate != null)
