package info.galudisu.syntax

import akka.actor.{Actor, ActorSystem, PoisonPill}
import akka.cluster.singleton.{
  ClusterSingletonManager,
  ClusterSingletonManagerSettings,
  ClusterSingletonProxy,
  ClusterSingletonProxySettings
}
import info.galudisu.job.Scheduler
import info.galudisu.job.Scheduler.ScheduleTrigger

object fragment {

  final class Fragment(protected val cron: String) {

    def dispatch(receptionist: Class[_ <: Actor])(implicit system: ActorSystem): Unit = {
      // create a cluster singleton actor
      val singleton = system.actorOf(
        ClusterSingletonManager
          .props(Scheduler.props(cron, receptionist), PoisonPill, ClusterSingletonManagerSettings(system)))

      val proxy = system.actorOf(
        ClusterSingletonProxy.props(singleton.path.toStringWithoutAddress, ClusterSingletonProxySettings(system)))
      proxy ! ScheduleTrigger
    }

    def ~~>(target: Class[_ <: Actor])(implicit system: ActorSystem): Unit = dispatch(target)
  }

  object Fragment {
    def apply(cron: String): Fragment = new Fragment(cron)
    val empty: Fragment               = new Fragment("")
  }
}
