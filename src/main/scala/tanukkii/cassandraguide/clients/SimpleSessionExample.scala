package tanukkii.cassandraguide.clients

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session.State
import scala.collection.JavaConverters._


object SimpleSessionExample extends App {

  val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build()

  val session = cluster.connect()

  val state: State = session.getState

  printf("New session created for keyspace: %s\n", session.getLoggedKeyspace)

  for {
    host <- state.getConnectedHosts.asScala
  } {
    printf("Data Center: %s; Rack: %s; Host: %s; Open Connections: %s\n", host.getDatacenter, host.getRack, host.getAddress, state.getOpenConnections(host))
  }

  cluster.close()
  System.exit(0)

}
