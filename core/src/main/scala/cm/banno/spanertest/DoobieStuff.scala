package io.chrisdavenport.spannertest

import cats.effect._
import java.sql.DriverManager
import doobie._
import doobie.implicits._
import scala.concurrent.ExecutionContext

object DoobieStuff {

  case class Singer(id: Long, first: String, last: String, revenues: Int)

  val insert =
    Update[Singer]("""INSERT INTO Singers (SingerId, FirstName, LastName, Revenues)
    VALUES (?,?,?,?)""")

  val select =
    sql"SELECT SingerId, FirstName, LastName, Revenues FROM Singers".query[Singer]
  val create = {
    import doobie.implicits._
    sql"""CREATE TABLE Singers (
      SingerId   INT64 NOT NULL,
      FirstName  STRING(1024),
      LastName   STRING(1024),
      SingerInfo BYTES(MAX),
      Revenues   NUMERIC,
    ) PRIMARY KEY (SingerId)"""
  }

  def transactor = {
    import doobie._
    import doobie.hikari._
    // import doobie.implicits._
    for {
      ce: ExecutionContext     <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      xa: HikariTransactor[IO] <- HikariTransactor.newHikariTransactor[IO](
                                    "com.google.cloud.spanner.jdbc.JdbcDriver", // driver classname
                                    Configuration.spannerJdbcUrl,
                                    // "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",    // connect URL
                                    "",                                         // username
                                    "",                                         // password
                                    ce                                          // await connection here
                                  )
    } yield xa
  }

}
