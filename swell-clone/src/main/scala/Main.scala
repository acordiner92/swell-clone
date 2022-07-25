import zhttp.http.*
import zhttp.service.{EventLoopGroup, Server}
import zhttp.service.server.ServerChannelFactory
import zio.ZIOAppDefault
import zio.Console.*
import zio.*

import scala.language.postfixOps

object Main extends ZIOAppDefault {

  val app: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> _ / "healthcheck" => Response.text("Swell app is running...")
  }

  private val server = Server.app(app) ++ Server.port(8080)

  override val run =
    server.make.flatMap(startedServer =>
      printLine(s"Server started on port ${startedServer.port}") *> ZIO.never
    ).provideLayer(
      ServerChannelFactory.auto ++ EventLoopGroup.auto(10) ++ Scope.default
    )

}