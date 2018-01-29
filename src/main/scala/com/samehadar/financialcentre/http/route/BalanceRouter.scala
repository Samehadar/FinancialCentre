package com.samehadar.financialcentre.http.route

import akka.http.scaladsl.server.Route
import com.samehadar.financialcentre.http.message.{GetBalanceMessage, GetBalancesMessage, ResponseGetBalance, ResponseGetBalances}
import akka.http.scaladsl.server.Directives._
import com.samehadar.financialcentre.core.AccountService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.{Json, JsonObject}
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by vital on 28.01.2018.
  */
class BalanceRouter(accountService: AccountService)(implicit executionContext: ExecutionContext) {

  import akka.http.scaladsl.model.StatusCodes.OK
  import akka.http.scaladsl.model.StatusCodes.BadRequest
  import akka.http.scaladsl.model.StatusCodes.InternalServerError
  import com.samehadar.financialcentre.http.message.MyProtocol.encodeResponseGetBalance
  import com.samehadar.financialcentre.http.message.MyProtocol.encodeResponseGetBalances
  import io.circe.Encoder.encodeString
  import io.circe.Encoder.encodeList

  val routePOST: Route =
    post {
      path("getBalance") {
        pathEndOrSingleSlash {
          entity(as[GetBalanceMessage]) { message =>
            onComplete(
              accountService.getBalance(message)
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
      } ~
      path("getBalances") {
        pathEndOrSingleSlash {
          entity(as[GetBalancesMessage]) { message =>
            onComplete(
              accountService.getBalances(message)
            ) {
              case Success(value) => value match {
                case Nil => complete(OK -> List.empty[ResponseGetBalances].asJson)
                case t3 :: tail => complete(OK -> (t3 :: tail).map(t3 => ResponseGetBalances(t3._1, t3._2, t3._3)).asJson)
                case _ => complete(OK -> List.empty[ResponseGetBalances].asJson)
              }
              case Failure(ex)    =>
                println(ex.getMessage)
                complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
            }
          }
        }
      }
    }

  val route: Route =
    get {
      path("getBalance") {
        parameters("userinfoid".as[Int], "currency", "accounttype") { (userInfoId: Int, currency, accountType) =>
          onComplete(
            accountService.getBalance(GetBalanceMessage(userInfoId, currency, accountType))
          ) {
            case Success(value) => value match {
              case balanceFact :: Nil => complete(OK -> ResponseGetBalance(balanceFact).asJson)
              case _ :: _ => complete(BadRequest -> "More than 1 request result".asJson)
              case _ => complete(OK -> Option(null)) // todo:: remove null
            }
            case Failure(ex) =>
              println(ex.getMessage)
              complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
          }
        }
      } ~
        path("getBalances") {
          parameters("userinfoid".as[Int]) { userInfoId: Int =>
            onComplete(
              accountService.getBalances(GetBalancesMessage(userInfoId))
            ) {
              case Success(value) => value match {
                case Nil => complete(OK -> List.empty[ResponseGetBalances].asJson)
                case t3 :: tail => complete(OK -> (t3 :: tail).map(t3 => ResponseGetBalances(t3._1, t3._2, t3._3)).asJson)
                case _ => complete(OK -> List.empty[ResponseGetBalances].asJson)
              }
              case Failure(ex) =>
                println(ex.getMessage)
                complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
            }
          }
        }
    }

}