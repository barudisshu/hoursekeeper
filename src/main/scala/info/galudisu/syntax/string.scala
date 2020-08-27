package info.galudisu.syntax

import info.galudisu.syntax.fragment.Fragment
import shapeless.ProductArgs

final class CronInterpolator(private val sc: StringContext) {

  private def mkFragment[A](a: A): Fragment  = {
    val cron = sc.parts.mkString
    Fragment(cron)
  }

  object fr extends ProductArgs {
    def applyProduct[A](a: A): Fragment = mkFragment(a)
  }

  val cron: fr.type  = fr
}

trait ToCronInterpolator {
  implicit def toCronInterpolator(sc: StringContext): CronInterpolator = new CronInterpolator(sc)
}

object string extends ToCronInterpolator
