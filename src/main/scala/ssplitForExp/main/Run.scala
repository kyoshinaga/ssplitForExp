package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2016/12/28.
  */

import java.io._
import javax.management.RuntimeErrorException

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import scala.sys.process._

class Run(path:String) {

  private val fileList:Array[String] = (Process("ls " + path) !!).split("\n")
  private val bioCorpusList = fileList.map{x => Convert(path + "/" + x).BIOFullCorpus}

  private val wordSet = bioCorpusList.flatMap{x => x.map{w => w._1}}.toSet[String]
  private val lookup = LookupTable(wordSet)

  implicit val formats = DefaultFormats

  val jsonBIOCorpus:JValue = "_articles" -> Extraction.decompose(bioCorpusList)
  val jsonTable:JValue = "_lookup" ->
    ("_key2id" -> Extraction.decompose(lookup.getKey2Id))~
      ("_id2key" -> Extraction.decompose(lookup.getId2Key))

  def writeJson(outDir: String) : Unit = {

    val bioCorpusPath = outDir + "/jpnCorpusBunsetsuBIO.json"
    val lookupPath = outDir + "/jpnLookup.json"

    val bioCorpusf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bioCorpusPath)))
    val lookupf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lookupPath)))

    bioCorpusf.write(compact(render(jsonBIOCorpus)))
    lookupf.write(compact(render(jsonTable)))

    bioCorpusf.close()
    lookupf.close()
  }
}

object Run {
  def main(args:Array[String]): Unit = {
    try {
      val fileDir = args(0)
      val outDir = args(1)
      val r = new Run(fileDir)
      r.writeJson(outDir)
    }
    catch {
      case e: Exception =>
        System.err.println("USAGE: ssplitForExp.main.Run INPUTDIR OUTPUTDIR")
    }
  }
}