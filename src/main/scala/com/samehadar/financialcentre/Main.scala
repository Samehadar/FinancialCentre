package com.samehadar.financialcentre

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import ch.qos.logback.classic.Logger
import com.samehadar.financialcentre.core.AccountService
import com.samehadar.financialcentre.db.model.Account
import com.samehadar.financialcentre.db.{Config, DBConnection, DBMigrationManager}
import com.samehadar.financialcentre.http.Router
import com.typesafe.config.ConfigFactory
import io.getquill.{ImplicitQuery, PostgresAsyncContext, SnakeCase}
import org.joda.time.DateTime

import scala.io.StdIn
import scala.util.{Failure, Success}

/**
  * Created by vital on 27.01.2018.
  */
object Main extends DBMigrationManager {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem()
    import system.dispatcher
    implicit val materializer = ActorMaterializer()

    val config = Config.init()

    flywayInt(
      config.dBConfig.JDBCurl,
      config.dBConfig.username,
      config.dBConfig.password
    )


    val ctx = new PostgresAsyncContext(SnakeCase, "ctx") with ImplicitQuery

    val accountService = new AccountService(ctx)
    val router = Router(accountService).route

//    DBConnection.create(Account(10000, "U0762543", "RUB", "INTERNAL", "RUB", false, false, new DateTime(2004, 4, 14, 15, 56, 5, 766), null, "Created from quill", 762, 0, null))
//    DBConnection.runExample().foreach(println(_))




//    val areas = quote {
//      query[Account].map(_.toString)
//    }
//
//    val result = ctx.run(areas)
//    result.foreach(println(_))
//
//    val q = quote {
//      query[Circle].filter(c => c.radius == 1)
//    }
//    val result = ctx.run(q)
//    result.foreach(c => println(c.radius))
//
//
//    val customRoute: Route = path("user" / IntNumber) { id =>
//      complete(s"Get info about user$id")
//    }
//
//    val ex2: Route =
//      pathPrefix("user") {
//        path("info") {
//          parameter("id") { id =>
//            complete(s"Return info for user with id = $id")
//          }
//        } ~
//          complete("It's default behaviuor")
//      }
//
//    val rootRoute: Route =
//      pathSingleSlash {
//        complete(s"This is simple server, that can take info about account balance and do transaction between accounts")
//      }
//
//    val routeWithStreams =
//      path("stream") {
//        complete(
//          HttpResponse(entity =
//            HttpEntity(ContentTypes.`text/plain(UTF-8)`, 4,
//              Source.single(ByteString("ab")) ++ Source.single(ByteString("cd")))
//          )
//        )
//      }
//
//    val route: Route =
//      get {
//        rootRoute ~
//          ex2 ~
//          routeWithStreams ~
//          path("abc") {
//            complete("Hello World")
//          } ~
//          path("another") {
//            parameters("name", "age".as[Int].?) { (name, age: Option[Int]) =>
//              val ageIn10Year = age.map(_ + 10).getOrElse(999)
//              complete(s"Hello $name. You are $ageIn10Year in 10 year")
//            }
//          } ~
//          path("getLastModified") {
//            respondWithHeader(akka.http.scaladsl.model.headers.`Last-Modified`(DateTime.now)) {
//              complete(s"Response with LastModified header")
//            }
//          }
//      }

    Http().bindAndHandleAsync(Route.asyncHandler(router), config.httpConfig.host, config.httpConfig.port)
      .onComplete {
        case Success(binding) =>
          println("Server started on port 8080. Type ENTER to terminate.")
          StdIn.readLine()
          binding.unbind()
          system.terminate()
        case Failure(e) =>
          println("Binding failed.")
          e.printStackTrace()
          system.terminate()
      }
  }

  def disableDebugMode(): Unit = {
    import org.slf4j.LoggerFactory
    import ch.qos.logback.classic.Level
    import ch.qos.logback.classic.Logger

    val loggers = Seq(
      "org.apache.http",
      "groovyx.net.http",
      "com.typesafe.scala-logging",
      "ch.qos.logback"
    )

    loggers.foreach { name =>
      val logger = LoggerFactory.getLogger(name).asInstanceOf[Logger]
      logger.setLevel(Level.INFO)
      logger.setAdditive(false)
    }
  }
}
