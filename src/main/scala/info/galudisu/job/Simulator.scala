package info.galudisu.job

import akka.actor.{Actor, ActorLogging, PoisonPill}
import info.galudisu.job.Simulator.SimulatorTrigger

class Simulator() extends Actor with ActorLogging {

  override def preStart(): Unit = {
    context.self ! SimulatorTrigger
  }

  override def receive: Receive = {

    case SimulatorTrigger =>
      log.debug("Simulator starting...")
      context watch self
      self ! PoisonPill

    case x =>
      log.warning("unknown message: {}", x)
  }

  override def postStop(): Unit = {
    log.debug("Simulator terminated...")
  }
}

object Simulator {

  case object SimulatorTrigger

  def apply(): Simulator = new Simulator()
}
