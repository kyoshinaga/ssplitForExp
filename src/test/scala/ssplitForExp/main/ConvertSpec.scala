package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import org.scalatest.{FlatSpec, Matchers}

class ConvertSpec extends FlatSpec with Matchers{

  def findPath(localPath:String): String = getClass.getClassLoader.getResource(localPath).getPath

  "apply" should "open XML file and return instance" in {
    val corpus = Convert(findPath("./OC01_00001.xml"))

    corpus.fullCorpus.length should be > 0
  }

}
