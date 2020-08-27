package info.galudisu.model

import doobie._
import doobie.implicits._

case class Staff(staffId: Long, username: String)

object Staff {
  def find(username: String): ConnectionIO[Option[Staff]] =
    sql"select staff_id, username from t_staff where username = $username ".query[Staff].option
}
