package io.chrisdavenport.spannertest

import cats.effect._
import java.sql.DriverManager
import doobie._
import doobie.implicits._

object Main extends IOApp {

  val projectId = "your-project-id"
  val instanceId = "test-instance"
  val databaseId = "test-db"

  def run(args: List[String]): IO[ExitCode] = {
    import doobie.util.yolo._
    
    DoobieStuff.transactor.use{xa => 
      val yolo = new Yolo(xa); import yolo._
      DoobieStuff.insert.check
      // DoobieStuff.insert.check//.run(DoobieStuff.Singer(10, "Marc", "Richards", 104100)).transact(xa) >>
      // DoobieStuff.select.to[List]
        // .transact(xa)
        .flatTap(a => IO.println(a))

    }.as(ExitCode.Success)
  }

  object DoobieStuff {

    case class Singer(id: Long, first: String, last: String, revenues: Int)

    val insert = {
      Update[Singer]("""INSERT INTO Singers (SingerId, FirstName, LastName, Revenues)
      VALUES (?,?,?,?)""")
    }

    val select = {
      sql"SELECT SingerId, FirstName, LastName, Revenues FROM Singers".query[Singer]
    }
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
        ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
        xa <- HikariTransactor.newHikariTransactor[IO](
                "com.google.cloud.spanner.jdbc.JdbcDriver",                        // driver classname
                String.format(
              "jdbc:cloudspanner://localhost:9010/projects/%s/instances/%s/databases/%s;usePlainText=true",
              projectId, instanceId, databaseId),
                // "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",   // connect URL
                "",                                   // username
                "",                                     // password
                ce                                      // await connection here
              )
      } yield xa
    }

  }

  object JDBCStuff {

    def createJDBC = {
      IO(DriverManager.getConnection(
          String.format(
              "jdbc:cloudspanner://localhost:9010/projects/%s/instances/%s/databases/%s;usePlainText=true",
              projectId, instanceId, databaseId)
      )).flatMap{connection => 
        IO(connection.createStatement()).flatMap{statement => 
          IO(statement.execute("""CREATE TABLE Singers (
        SingerId   INT64 NOT NULL,
        FirstName  STRING(1024),
        LastName   STRING(1024),
        SingerInfo BYTES(MAX),
        Revenues   NUMERIC,
      ) PRIMARY KEY (SingerId)"""))
        }
      }
    }

    def jdbctest = {
      IO(DriverManager.getConnection(
          String.format(
              "jdbc:cloudspanner://localhost:9010/projects/%s/instances/%s/databases/%s;usePlainText=true",
              projectId, instanceId, databaseId)
      )).flatMap{connection => 
        IO(connection.createStatement()).flatMap{statement => 
          IO(statement.executeQuery("SELECT CURRENT_TIMESTAMP()")).flatMap{rs =>
            IO{
              if (rs.next()) {
                System.out.printf(
                  "Connected to Cloud Spanner at [%s]%n", rs.getTimestamp(1).toString())
                ()
              } else ()
            }
          } 
        }
      }
    }
  }


}