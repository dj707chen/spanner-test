package io.chrisdavenport.spannertest

object ConfigurationCloud {

  val projectId = "spannerlab"
  val instanceId = "test-instance"
  val databaseId = "test-db"

  def spannerJdbcUrl =
    s"jdbc:cloudspanner://localhost:9010/projects/$projectId/instances/$instanceId/databases/$databaseId;usePlainText=true"

}