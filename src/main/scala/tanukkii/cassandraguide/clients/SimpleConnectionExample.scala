package tanukkii.cassandraguide.clients

import com.datastax.driver.core.{Metadata, Cluster}
import scala.collection.JavaConverters._

object SimpleConnectionExample extends App {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()

  cluster.init()

  val metadata: Metadata = cluster.getMetadata

  printf("Connected to cluster: %s %s\n", metadata.getClusterName, cluster.getClusterName)

  for {
    host <- metadata.getAllHosts.asScala
  } {
    printf("Data Center: %s; Rack: %s; Host: %s\n", host.getDatacenter, host.getRack, host.getAddress)
  }

  printf("Protocol Version: %s\n", cluster.getConfiguration.getProtocolOptions.getProtocolVersion)

  cluster.close()
  System.exit(0)
}
