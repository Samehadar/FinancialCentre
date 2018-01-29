package com.samehadar.financialcentre.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.samehadar.financialcentre.core.AccountService
import com.samehadar.financialcentre.http.route.{BalanceRouter, TransactionRouter}

import scala.concurrent.ExecutionContext

/**
  * Created by vital on 28.01.2018.
  */
case class Router(accountService: AccountService)(implicit executionContext: ExecutionContext) {

  private val balanceRoute = new BalanceRouter(accountService).route
  private val transactionRoute = new TransactionRouter().route

  val route: Route =
    balanceRoute ~
    transactionRoute ~
    pathPrefix("status") {
      get {
        complete("OK")
      }
    } ~ complete("Not supported request")

}
