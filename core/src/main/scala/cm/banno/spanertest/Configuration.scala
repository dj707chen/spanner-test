package io.chrisdavenport.spannertest

object Configuration {

  val projectId  = "spanneremulatorlab"
  val instanceId = "test-instance"
  val databaseId = "test-db"

  def spannerJdbcUrl =
    s"jdbc:cloudspanner://localhost:9010/projects/$projectId/instances/$instanceId/databases/$databaseId;usePlainText=true"

}
