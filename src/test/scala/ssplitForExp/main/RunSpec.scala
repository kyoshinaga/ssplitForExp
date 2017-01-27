package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2017/01/12.
  */

import org.scalatest.{FlatSpec, Matchers}

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class RunSpec extends FlatSpec with Matchers{

  def findPath(localPath:String) = getClass.getClassLoader.getResource(localPath).getPath

  "jsonCorpus" should "have dictionary objects" in {

    1 should be (1)
  }

}
