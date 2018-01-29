package com.samehadar.financialcentre.core

import com.samehadar.financialcentre.db.model.{Account, AccountBalance}
import com.samehadar.financialcentre.http.message.{DoTransactionMessage, Transaction}
import io.getquill.{PostgresAsyncContext, SnakeCase}

import scala.util.{Failure, Success}

/**
  * Created by vital on 28.01.2018.
  */
class TransactionService(ctx: PostgresAsyncContext[SnakeCase.type]) {

  import ctx._

  private case class DoTransaction(from: (Account, AccountBalance), to: (Account, AccountBalance), trans: Transaction) {
    require(from._2.balanceFact - trans.amount > 0)
  }
  private case class EndTransaction(from: (Account, AccountBalance), to: (Account, AccountBalance)) {
    require(from._2.balanceFact > 0 && to._2.balanceFact > 0)
  }

//  def getFrom(trans: Transaction) = quote {
//    query[Account].join(query[AccountBalance]).on(_.accountId == _.accountBalanceId)
//      .filter(row => row._1.userInfoId == trans.userInfoIdFrom && row._1.accountType == trans.accountTypeFrom)
//  }
//  def getTo(trans: Transaction) = quote {
//    query[Account].join(query[AccountBalance]).on(_.accountId == _.accountBalanceId)
//      .filter(row => row._1.userInfoId == trans.userInfoIdTo && row._1.accountType == trans.accountTypeTo)
//  }


//  def transact(doTransactionMessage: DoTransactionMessage) = {
//
//    val predicateList = doTransactionMessage.transactions.flatMap(trans => List({ account: Account =>
//      account.userInfoId == trans.userInfoIdFrom && account.accountType == trans.accountTypeFrom
//    }, { account: Account =>
//      account.userInfoId == trans.userInfoIdTo && account.accountType == trans.accountTypeTo
//    }))
//
//
//    val getAllAccountsForTransaction = quote {
//      query[Account].join(query[AccountBalance]).on(_.accountId == _.accountBalanceId)
//        .filter(row => predicateList.map(func => func(row)).foldLeft(false)(_ || _))
//    }
//
//    ctx.run(getAllAccountsForTransaction).map{ users =>
//      for {
//        trans: Transaction <- doTransactionMessage.transactions
//        from: (Account, AccountBalance) <- users if from._1.userInfoId == trans.userInfoIdFrom && from._1.accountType == trans.accountTypeFrom
//        to: (Account, AccountBalance) <- users if from._1.userInfoId == trans.userInfoIdTo && from._1.accountType == trans.accountTypeTo
//      } yield DoTransaction(from, to, trans)
//    }.map{transactions =>
//      transactions.map{ trans =>
//        trans.from._2.balanceFact -= trans.trans.amount
//        trans.to._2.balanceFact += trans.trans.amount
//        EndTransaction(trans.from, trans.to)
//      }
//    }
//
//  }

}
