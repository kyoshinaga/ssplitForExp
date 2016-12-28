package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import org.scalatest.{FlatSpec, Matchers}
import scala.xml._
class ConvertSpec extends FlatSpec with Matchers{

  def findPath(localPath:String) = getClass.getClassLoader.getResource(localPath).getPath

  "convert" should "output corpus" in {

    val sampleCorpusXML = XML.load(findPath("./OC01_00001.xml"))

    println(sampleCorpusXML.text)

    1 should be(1)
  }

}
