package info.galudisu

import doobie.util.transactor.Transactor.Aux

trait Database {
  this: Configuration =>

  import cats.effect._
  import doobie._
  import doobie.util.ExecutionContexts

  import scala.concurrent.ExecutionContext

  implicit val ec: ExecutionContext = system.dispatcher
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)

  lazy val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    driverName,
    url,
    username,
    password,
    Blocker.liftExecutionContext(ExecutionContexts.synchronous)
  )
}
