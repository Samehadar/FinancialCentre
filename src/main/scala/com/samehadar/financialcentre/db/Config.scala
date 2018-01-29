package com.samehadar.financialcentre.db

import com.typesafe.config.ConfigFactory

/**
  * Created by vital on 28.01.2018.
  */
case class Config(httpConfig: HttpConfig, dBConfig: DBConfig)

object Config {
  def init(): Config = {
    val config = ConfigFactory.load()
    val httpConfig = HttpConfig(config.getString("http.host"), config.getInt("http.port"))
    val dbConfig = DBConfig(config.getString("database.jdbc_url"), config.getString("database.username"), config.getString("database.password"))
    Config(httpConfig, dbConfig)
  }
}


case class HttpConfig(host: String, port: Int)
case class DBConfig(JDBCurl: String, username: String, password: String)