package io.chrisdavenport.spannertest

object ConfigurationEmulator {

  val projectId = "spannerlab"
  val instanceId = "test-instance"
  val databaseId = "test-db"

  def spannerJdbcUrl: String =
    s"jdbc:cloudspanner://localhost:9010/projects/$projectId/instances/$instanceId/databases/$databaseId;usePlainText=true"

}