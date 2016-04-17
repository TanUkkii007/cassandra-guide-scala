package tanukkii.cassandraguide.clients

import java.util.UUID

import com.datastax.driver.mapping.annotations.{Column, PartitionKey, Table}

@Table(keyspace = "hotel", name = "hotels")
case class Hotel(@PartitionKey id: UUID, @Column(name = "name") name: String, @Column(name = "phone") phone: String, @Column(name = "address") address: String, @Column(name = "pois") pointsOfInterest: Set[UUID])