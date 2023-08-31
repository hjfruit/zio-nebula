package zio.nebula.example

import zio._
import zio.nebula._
import zio.nebula.meta._

import com.vesoft.nebula.meta.SpaceItem

final class NebulaMetaClientExample(nebulaMetaManager: NebulaMetaClient) {

  def getSpace(spaceName: String): Task[SpaceItem] =
    nebulaMetaManager.getSpace(spaceName)
}

object NebulaMetaClientExample {
  lazy val layer = ZLayer.fromFunction(new NebulaMetaClientExample(_))
}

object NebulaMetaClientMain extends ZIOAppDefault {

  override def run =
    ZIO
      .serviceWithZIO[NebulaMetaClientExample](_.getSpace("test"))
      .flatMap(space => ZIO.logInfo(space.toString))
      .provide(
        Scope.default,
        NebulaConfig.metaConfigLayer,
        NebulaMetaClientExample.layer,
        NebulaMetaClient.layer
      )

}