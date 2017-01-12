package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import scala.xml._
import java.io._

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class Run(path:String) {

  private val corpus = Convert(path).fullCorpus
  private val lookup = LookupTable(corpus.map(x => x._1).toSet[String])

  implicit val formats = DefaultFormats
  val jsonCorpus:JValue = "_article" -> corpus
  val jsonTable:JValue = "_lookup" ->
    ("_key2id" -> Extraction.decompose(lookup.getKey2Id))~
      ("_id2key" -> Extraction.decompose(lookup.getId2Key))

  def writeJson(outDir: String) : Unit = {

    val corpusPath = outDir + "/jpnCoupus.json"
    val lookupPath = outDir + "/jpnLookup.json"

    val corpusf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(corpusPath)))
    val lookupf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lookupPath)))

    corpusf.write(compact(render(jsonCorpus)))
    lookupf.write(compact(render(jsonTable)))

    corpusf.close()
    lookupf.close()
  }

}

object Run {
  def main(args:Array[String]): Unit = {
    val filePath = args(0)
    val outDir = args(1)
    val r = new Run(filePath)
    r.writeJson(outDir)
  }
}