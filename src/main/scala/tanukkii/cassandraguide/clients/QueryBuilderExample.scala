package tanukkii.cassandraguide.clients

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import scala.collection.JavaConverters._

object QueryBuilderExample extends App {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()

  val session = cluster.connect("hotel")

  val uuid = UUIDs.random().toString

  val hotelInsertBuilder = QueryBuilder.insertInto("hotels")
    .value("id", uuid)
    .value("name", "Super Hotel at WestWorld")
    .value("phone", "1-888-999-9999")

  val hotelInsertResult = session.execute(hotelInsertBuilder)

  println(hotelInsertResult)
  println(hotelInsertResult.wasApplied())
  println(hotelInsertResult.getExecutionInfo)
  println(hotelInsertResult.getExecutionInfo.getIncomingPayload)


  val hotelSelectBuild = QueryBuilder.select().all().from("hotels").where(QueryBuilder.eq("id", uuid))

  val hotelSelectResult = session.execute(hotelSelectBuild)

  println(hotelSelectResult)
  println(hotelSelectResult.wasApplied())
  println(hotelSelectResult.getExecutionInfo)
  println(hotelSelectResult.getExecutionInfo.getIncomingPayload)

  for {
    row <- hotelSelectResult.asScala
  } {
    System.out.format("id: %s, name: %s, phone: %s\n", row.getUUID("id"), row.getString("name"), row.getString("phone"));
  }

  val hotelDeleteBuilt = QueryBuilder.delete().all().from("hotels").where(QueryBuilder.eq("id", uuid))

  val hotelDeleteResult = session.execute(hotelDeleteBuilt)

  println(hotelDeleteResult)
  println(hotelDeleteResult.wasApplied())
  println(hotelDeleteResult.getExecutionInfo)
  println(hotelDeleteResult.getExecutionInfo.getIncomingPayload)

  cluster.close()
  System.exit(0)
}
