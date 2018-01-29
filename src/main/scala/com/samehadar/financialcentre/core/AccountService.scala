package com.samehadar.financialcentre.core

import com.samehadar.financialcentre.db.model.{Account, AccountBalance}
import com.samehadar.financialcentre.http.message.{GetBalanceMessage, GetBalancesMessage}
import io.getquill._

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

/**
  * Created by vital on 28.01.2018.
  */
class AccountService(ctx: PostgresAsyncContext[SnakeCase.type]) {

  def getBalance(message: GetBalanceMessage) = {
    import ctx._

    val getBalanceQuery = quote(
      query[Account].join(query[AccountBalance]).on(_.accountId == _.accountBalanceId)
        .filter {
          case (row, _) => row.userInfoId == lift(message.userInfoId) &&
            row.currency == lift(message.currency) &&
            row.accountType == lift(message.accountType)
        }.map {
        case (_, e2) => e2.balanceFact
      }
    )

    ctx.run(getBalanceQuery)
  }

  def getBalances(message: GetBalancesMessage) = {
    import ctx._

    val getBalancesQuery = quote(
      query[Account].join(query[AccountBalance]).on(_.accountId == _.accountBalanceId)
        .filter {
          case (row, _) => row.userInfoId == lift(message.userInfoId)
        }.map {
        case (e1, e2) => (e1.currency, e1.accountType, e2.balanceFact)
      }
    )

    ctx.run(getBalancesQuery)
  }

}
