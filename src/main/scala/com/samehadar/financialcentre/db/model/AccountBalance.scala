package com.samehadar.financialcentre.db.model

import akka.http.scaladsl.model.DateTime

/**
  * Created by vital on 28.01.2018.
  */
case class AccountBalance(accountBalanceId: Int, var balanceFact: BigDecimal, var balancePlan: BigDecimal, balanceDate: DateTime, version: Short) {
  // todo:: need it?
//  if (balanceFact == null) balanceFact = BigDecimal.valueOf(0)
//  if (balancePlan == null) balancePlan = BigDecimal.valueOf(0)
}
