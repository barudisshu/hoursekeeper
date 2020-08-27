package info.galudisu

import akka.actor.ActorSystem
import info.galudisu.job.Simulator
import info.galudisu.syntax.string.toCronInterpolator

trait Bootstrap {
  this: Database with Configuration =>
  implicit val as: ActorSystem = system

  cron"*/5 * * ? * *" ~~> classOf[Simulator]
}
