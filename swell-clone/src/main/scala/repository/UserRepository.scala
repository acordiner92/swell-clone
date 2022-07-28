package repository

import domain.User
import io.getquill.*
import io.getquill.context.ZioJdbc.DataSourceLayer
import repository.UserRepository.ctx
import zio.{Task, ZIO, ZLayer, Console}

import java.sql.SQLException
import java.util.UUID
import javax.sql.DataSource

trait UserRepository {
  def users: ZIO[DataSource, SQLException, List[User]]
}

object UserRepository {
  inline given SchemaMeta[User] = schemaMeta("users")
  val ctx = new PostgresZioJdbcContext(SnakeCase)
  import ctx.*

  val zioDS: ZLayer[Any, Throwable, DataSource] = DataSourceLayer.fromPrefix("ctx")

  def users: ZIO[UserRepository, Throwable, List[User]] =
    ZIO.serviceWithZIO[UserRepository](_.users).provideSomeLayer(zioDS)

  def layer: ZLayer[Any, Throwable, UserRepositoryLive] =
      ZLayer.succeed(UserRepositoryLive())

    class UserRepositoryLive extends UserRepository {

      override def users: ZIO[DataSource, SQLException, List[User]] = run(query[User])
    }
}


