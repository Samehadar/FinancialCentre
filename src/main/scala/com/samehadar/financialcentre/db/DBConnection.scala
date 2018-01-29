package com.samehadar.financialcentre.db

import com.samehadar.financialcentre.db.model.Account
import io.getquill.{PostgresAsyncContext, SnakeCase}
import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by vital on 28.01.2018.
  */
object DBConnection {

  lazy val ctx = new PostgresAsyncContext(SnakeCase, "ctx")
  import ctx._
  
  def create(account: Account): Future[Long] =
    ctx.run(query[Account].insert(lift(account)))

  def read(accountId: Int): Future[Seq[Account]] =
    ctx.run(query[Account].filter(_.accountId == lift(accountId)))

  def update(account: Account): Future[Long] =
    ctx.run(query[Account].update(lift(account)))

  def update(accountId: Int, account: String): Future[Long] =
    ctx.run(query[Account].filter(_.accountId == lift(accountId)).update(_.account -> lift(account)))

  def delete(accountId: Int): Future[Long] =
    ctx.run(query[Account].filter(_.accountId == lift(accountId)).delete)

  def runExample(): Future[Unit] =
      create(Account(10000, "U0762513", "USD", "INTERNAL", "USD", false, false, new DateTime(2014, 4, 14, 15, 56, 5, 766), None, "Created from quill", 762, 0, null)).flatMap { _ =>
        read(10000)
      }.flatMap { _ =>
        update(10000, "NEW_TEST_ID")
      }.flatMap { _ =>
        update(10000, "David Bowie")
      }.flatMap { _ =>
        delete(10000)
      }.map(_ => ())
}
