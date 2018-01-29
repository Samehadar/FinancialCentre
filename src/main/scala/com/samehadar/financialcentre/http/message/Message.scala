package com.samehadar.financialcentre.http.message

import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder

/**
  * Created by vital on 28.01.2018.
  */
sealed trait Message
// Input messages
case class GetBalancesMessage(userInfoId: Int) extends Message
case class GetBalanceMessage(userInfoId: Int, currency: String, accountType: String) extends Message
case class DoTransactionMessage(transactions: List[Transaction])
case class Transaction(userInfoIdFrom: Int, accountTypeFrom: String, userInfoIdTo: Int, accountTypeTo: String, currency: String, amount: BigDecimal)
// Response message
case class ResponseGetBalance(balanceFact: BigDecimal) extends Message
case class ResponseGetBalances(currency: String, accountType: String, balance:BigDecimal) extends Message

object MyProtocol {
  implicit val decodeDoTransactionMessage: Decoder[DoTransactionMessage] = deriveDecoder[DoTransactionMessage]
  implicit val encodeDoTransactionMessage: Encoder[DoTransactionMessage] = deriveEncoder[DoTransactionMessage]
  implicit val decodeResponseGetBalance: Decoder[ResponseGetBalance] = deriveDecoder[ResponseGetBalance]
  implicit val encodeResponseGetBalance: Encoder[ResponseGetBalance] = deriveEncoder[ResponseGetBalance]
  implicit val decodeResponseGetBalances: Decoder[ResponseGetBalances] = deriveDecoder[ResponseGetBalances]
  implicit val encodeResponseGetBalances: Encoder[ResponseGetBalances] = deriveEncoder[ResponseGetBalances]
}