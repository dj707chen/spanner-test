package io.chrisdavenport.spannertest

import cats.effect._
import doobie._
import doobie.implicits._

import java.sql.DriverManager
import scala.concurrent.ExecutionContext

object Main extends IOApp {

  val projectId = "spanneremulatorlab"
  val instanceId = "test-instance"
  val databaseId = "test-db"

  def spannerJdbcUrl = s"jdbc:cloudspanner://localhost:9010/projects/$projectId/instances/$instanceId/databases/$databaseId;usePlainText=true"

  def run(args: List[String]): IO[ExitCode] = {
    import doobie.util.yolo._

    // JDBCStuff.createJDBC.as{ExitCode.Success}
    
      /*
        */
    DoobieStuff.transactor.use{ xa => 
      val yolo = new Yolo(xa); import yolo._
      // DoobieStuff.insert.run(DoobieStuff.Singer(10, "Marc", "Richards", 104100)).transact(xa) >>
      // DoobieStuff.create.update.run.transact(xa)
      DoobieStuff.select.to[List]
         .transact(xa)
         .flatTap(a => IO.println(a))
    }.as{ExitCode.Success}
  }

}