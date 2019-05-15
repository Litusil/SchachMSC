package controller

import java.net.URLDecoder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import model._
import model.fileIOComponent.FileIOInterface
import util.{JsonUtil, Observable}
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


class ChessController extends Observable {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val boardSize = 8
  var chessBoard: ChessBoard = newGame()
  val json = JsonUtil()

  def newGame(): ChessBoard = {
    new ChessBoard(Vector.fill(boardSize,boardSize)(None: Option[ChessPiece])).defaultInit()
  }

  def move(x_start: Int,y_start: Int,x_ziel: Int,y_ziel: Int): Unit = {

    val uri = Uri(URLDecoder.decode("http://localhost:8080/move", "UTF-8"))

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(method = HttpMethods.POST,uri = uri.withQuery(Query("x" -> x_start.toString,"y" -> y_start.toString,"xNew" -> x_ziel.toString ,"yNew" -> y_ziel.toString )) , entity = HttpEntity(ContentTypes.`application/json`,chessBoard.toJson().toString())))

    responseFuture
      .onComplete {
        case Success(res) => {
          println(res)
          val jsonString = Unmarshal(res.entity).to[String]
          chessBoard = json.createBoardFromJSON(Await.result(jsonString, 1 second))
          notifyObservers()
        }
        case Failure(_)   => sys.error("something wrong")
      }
  }
}
