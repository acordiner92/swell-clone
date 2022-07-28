import repository.{UserRepository}
import zhttp.http.*
import zhttp.service.{EventLoopGroup, Server}
import zhttp.service.server.ServerChannelFactory
import zio.ZIOAppDefault
import zio.Console.*
import zio.*

import scala.language.postfixOps
import zio.json.*

object Main extends ZIOAppDefault {

  val app: HttpApp[UserRepository, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> _ / "health-check" => ZIO.succeed(Response.text("Swell app is running..."))
    case Method.GET -> _ / "users" => UserRepository.users.map(response => Response.json(response.toJson))
  }

  private val server = Server.app(app) ++ Server.port(8080)

  override val run =
    server.make.flatMap(startedServer =>
      printLine(s"Server started on port ${startedServer.port}") *> ZIO.never
    ).provideLayer(
      ServerChannelFactory.auto ++ EventLoopGroup.auto(10) ++ Scope.default ++ UserRepository.layer
    )

}