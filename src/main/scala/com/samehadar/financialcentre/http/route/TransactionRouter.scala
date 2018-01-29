package com.samehadar.financialcentre.http.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.samehadar.financialcentre.core.TransactionService
import com.samehadar.financialcentre.http.message.DoTransactionMessage
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext

/**
  * Created by vital on 28.01.2018.
  */
class TransactionRouter(transactionService: TransactionService)(implicit executionContext: ExecutionContext) {

  val route: Route =
    post {
      path("transact") {
        pathEndOrSingleSlash {
          import com.samehadar.financialcentre.http.message.MyProtocol._
          entity(as[DoTransactionMessage]) { transactions =>
            onComplete(
              transactionService.getBalance(message)
            ) {
              case Success(value) => value match {
                case balanceFact :: Nil => complete(OK -> ResponseGetBalance(balanceFact).asJson)
                case _ :: _ => complete(BadRequest -> "More than 1 request result".asJson)
                case _ => complete(OK -> Option(null)) // todo:: remove null
              }
              case Failure(ex)    =>
                println(ex.getMessage)
                complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
            }
          }
        }
      }
    }

}
