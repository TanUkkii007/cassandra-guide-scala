package tanukkii.cassandraguide.clients

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.utils.UUIDs
import scala.collection.JavaConverters._


object PreparedStatementExample extends App {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()

  val session = cluster.connect("hotel")

  val uuid = UUIDs.random().toString

  val hotelInsertPrepared = session.prepare("INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)")

  val hotelInsertBound = hotelInsertPrepared.bind(uuid, "Super Hotel at WestWorld", "1-888-999-9999")

  val hotelInsertResult = session.execute(hotelInsertBound)

  println(hotelInsertResult)
  println(hotelInsertResult.wasApplied())
  println(hotelInsertResult.getExecutionInfo)
  println(hotelInsertResult.getExecutionInfo.getIncomingPayload)

  val hotelSelectPrepared = session.prepare("SELECT * FROM hotels WHERE id = ?")

  val hotelSelectBound = hotelSelectPrepared.bind(uuid)

  val hotelSelectResult = session.execute(hotelSelectBound)

  println(hotelSelectResult)
  println(hotelSelectResult.wasApplied())
  println(hotelSelectResult.getExecutionInfo)
  println(hotelSelectResult.getExecutionInfo.getIncomingPayload)

  for {
    row <- hotelSelectResult.asScala
  } {
    System.out.format("id: %s, name: %s, phone: %s\n", row.getString("id"), row.getString("name"), row.getString("phone"))
//    System.out.format("id: %s, name: %s, phone: %s\n", row.getUUID("id"), row.getString("name"), row.getString("phone"))
  }

  cluster.close()
  System.exit(0)
}
