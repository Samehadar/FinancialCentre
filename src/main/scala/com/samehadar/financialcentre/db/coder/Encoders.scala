package com.samehadar.financialcentre.db.coder

import java.util.Date

import io.getquill.MappedEncoding
import org.joda.time.{Duration, LocalDateTime}
/**
  * Created by vital on 29.01.2018.
  */

trait Encoders {
  implicit val jodaLocalDateTimeEncoder = MappedEncoding[LocalDateTime, Date](_.toDate)

  implicit val jodaDurationEncoder = MappedEncoding[Duration, Long](_.getMillis)
}