package info.galudisu

import akka.actor.ActorSystem
import com.typesafe.config._

trait Configuration {

  lazy val sysConfig: Config = ConfigFactory.load()

  lazy val driverName: String = sysConfig.getString("db.driver")
  lazy val url: String        = sysConfig.getString("db.url")
  lazy val username: String   = sysConfig.getString("db.username")
  lazy val password: String   = sysConfig.getString("db.password")

  lazy val clusterName: String = {
    sysConfig.getString("clustering.cluster.name")
  }

  lazy val system: ActorSystem = ActorSystem(clusterName, sysConfig)

}
