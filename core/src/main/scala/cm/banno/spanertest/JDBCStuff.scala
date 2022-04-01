package io.chrisdavenport.spannertest

import cats.effect._
import java.sql.DriverManager
import doobie._
import doobie.implicits._
import scala.concurrent.ExecutionContext

object JDBCStuff {

  def createJDBC: IO[Boolean] = {
    IO(DriverManager.getConnection(Configuration.spannerJdbcUrl))
    .flatMap{ connection => 
      IO(connection.createStatement()).flatMap{statement => 
        IO(statement.execute("""
          CREATE TABLE Singers (
            SingerId   INT64 NOT NULL,
            FirstName  STRING(1024),
            LastName   STRING(1024),
            SingerInfo BYTES(MAX),
            Revenues   NUMERIC,
          ) PRIMARY KEY (SingerId)"""))
      }
    }
  }

  def jdbctest = for {
        connection <- IO(DriverManager.getConnection(Configuration.spannerJdbcUrl))
        statement  <- IO(connection.createStatement())
        rs         <- IO(statement.executeQuery("SELECT CURRENT_TIMESTAMP()"))
        ret        <- if (rs.next()) {
                        System.out.printf(
                          "Connected to Cloud Spanner at [%s]%n", rs.getTimestamp(1).toString())
                        IO()
                      } else IO()
  } yield ret
}
