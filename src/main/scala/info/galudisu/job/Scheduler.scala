package info.galudisu.job

import java.time.ZonedDateTime

import akka.actor._
import cron4s.Cron
import info.galudisu.cron4zio._
import zio.{Runtime, Task}

import scala.language.postfixOps
import scala.util.{Failure, Success}

class Scheduler(cron: String, props: Props) extends Actor with ActorLogging {

  import Scheduler._

  override def receive: Receive = {
    case ScheduleTrigger =>
      Cron(cron).toTry match {
        case Failure(exception) =>
          log.warning("*** cron parsing: {}", exception.getMessage)
        case Success(value) =>
          val now = ZonedDateTime.now()
          log.debug("*** cron expr: {}", value.toString)
          log.debug("*** curr time: {}", now)
          val sc = Task {
            log.debug("*** triggered")
            context.actorOf(props)
          }
          val scheduled = repeatEffectForCron(sc, value)
          Runtime.default.unsafeRun(scheduled)
      }
    case x =>
      log.warning(s"received unhandled msg $x")
  }

}

object Scheduler {
  case object ScheduleTrigger

  def props(cron: String, receptionist: Class[_ <: Actor]): Props = Props(classOf[Scheduler], cron, Props(receptionist))
}
