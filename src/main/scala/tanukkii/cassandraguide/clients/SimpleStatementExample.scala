package tanukkii.cassandraguide.clients

import java.text.SimpleDateFormat

import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.{ResultSet, SimpleStatement, Cluster}
import scala.collection.JavaConverters._

object SimpleStatementExample extends App {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()

  val session = cluster.connect("hotel")

  val uuid = UUIDs.random()
  println(uuid)

  val hotelInsert: SimpleStatement = new SimpleStatement(s"INSERT INTO hotels (id, name, phone) VALUES ('$uuid', 'Super Hotel at WestWorld', '1-888-999-9999');")

  val hotelInsertResult: ResultSet = session.execute(hotelInsert)

  println(hotelInsertResult)
  println(hotelInsertResult.wasApplied())
  println(hotelInsertResult.getExecutionInfo)
  println(hotelInsertResult.getExecutionInfo.getIncomingPayload)

  val hotelSelect = new SimpleStatement(s"SELECT * FROM hotels WHERE id='$uuid'")
  hotelSelect.enableTracing()

  val hotelSelectResult = session.execute(hotelSelect)

  println(hotelSelectResult)
  println(hotelSelectResult.wasApplied())
  println(hotelSelectResult.getExecutionInfo)
  println(hotelSelectResult.getExecutionInfo.getIncomingPayload)
  println(hotelSelectResult.getExecutionInfo.getQueryTrace)

  for {
    row <- hotelSelectResult.asScala
  } {
    System.out.format("id: %s, name: %s, phone: %s\n\n", row.getString("id"), row.getString("name"), row.getString("phone"))
  }

  val dateFormat = new SimpleDateFormat("HH:mm:ss.SSS")
  val queryTrace = hotelSelectResult.getExecutionInfo.getQueryTrace
  printf("Trace id: %s\n\n", queryTrace.getTraceId)
  printf("%-42s | %-12s | %-10s \n", "activity", "timestamp", "source")
  println("-------------------------------------------+--------------+------------")

  for {
    event <- queryTrace.getEvents.asScala
  } {
    printf("%42s | %12s | %10s\n", event.getDescription, dateFormat.format(event.getTimestamp), event.getSource)
  }


  cluster.close()
  System.exit(0)


}
