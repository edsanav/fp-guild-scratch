package aux

import cats.effect.{IO, Resource}

import java.io.{File, FileInputStream}

object io {

  def inputStream(f: File): Resource[IO, FileInputStream] =
    Resource.make {
      IO.blocking(new FileInputStream(f))                         // build
    } { inStream =>
      IO.blocking(inStream.close()).handleErrorWith(_ => IO.unit) // release
    }

}
