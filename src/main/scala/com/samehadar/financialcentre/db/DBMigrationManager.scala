package com.samehadar.financialcentre.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationInfoService

/**
  * Created by vital on 28.01.2018.
  */
trait DBMigrationManager {

  private val flyway = new Flyway()

  def flywayInt(jdbcUrl: String, username: String, password: String): Unit = {
    flyway.setDataSource(jdbcUrl, username, password)
    flyway.setLocations("db.migration")

    drop(); migrate()
  }
  def migrate() : Int = flyway.migrate()
  def info() : MigrationInfoService = flyway.info()
  def drop() : Unit = flyway.clean()

}