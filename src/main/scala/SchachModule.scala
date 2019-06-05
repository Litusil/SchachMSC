import com.google.inject.AbstractModule
import com.google.inject.name.Names
import model.fileIOComponent.FileIOInterface
import net.codingwell.scalaguice.ScalaModule

class SchachModule extends AbstractModule with ScalaModule {

  def configure() = {
    val config: scala.xml.Elem = scala.xml.XML.loadFile("config.xml")
    val savesystem: String = (config \\ "slmanager" \ "@type").text
    val databaseUrl: String = "jdbc:h2:~/ChessBoard" // in memory on localhost
    val databaseUser: String = "SA"

    if (savesystem.equals("JSON")){
      bind[FileIOInterface].to[model.fileIOComponent.fileIoJsonImpl.FileIO]
    } else if(savesystem.equals("XML")){
      bind[FileIOInterface].to[model.fileIOComponent.fileIoXmlImpl.FileIO]
    } else if(savesystem.equals("H2")){
      bindConstant().annotatedWith(Names.named("H2Url")).to(databaseUrl)
      bindConstant().annotatedWith(Names.named("H2User")).to(databaseUser)
      bind[FileIOInterface].to[model.fileIOComponent.fileIOSlickImpl.FileIO]
    }
  }

}
