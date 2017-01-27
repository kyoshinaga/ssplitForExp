package ssplitForExp.main

/**
  * Created by kenta-yoshinaga on 2017/01/10.
  */

import scala.collection.mutable.ListBuffer
import scala.xml._

class Convert(path: String) {

  private val xml = XML.load(path)
  private val articles = XMLUtil.findAll(xml, "article")

  private val sentences = articles.map(x => XMLUtil.findAll(x, "sentence"))

  if (sentences.isEmpty)
    throw new IllegalAccessError("cannot construct " + getClass.getSimpleName + " without sentence node.")

  private val luws = sentences.map{x => x.flatMap{n => XMLUtil.findAll(n, "LUW").toList}}

  private val words = luws.map{x => x.map{n => n.text}}
  private val borders = luws.map{x => x.map{n => XMLUtil.getAttributionMap(n).getOrElse("B", "_")}}

  private val wordsAndBorders:List[List[(String, String)]] = (words zip borders).toList.map{x => (x._1 zip x._2).toList}

  /*
   * Tagging BIO class and concatenate key and tag.
   *
   * BIO tag
   *  B : Begin of Sentence.                Value is 0.
   *  I : Continuation or end of sentence.  Value is 1.
   *  O : Outside of sentence.              Value is 2.
   */

  def genBIOCorpus(data: List[(String, String)]): List[(String, Int)] = List(("\n", 2)) :::
  data.flatMap{x =>
    val w = ListBuffer.fill(x._1.length)(1)
    if(x._2 == "S")
      w(0) = 0
    x._1.map{x => x.toString} zip w
  } ::: List(("\n", 2))

  val corpus:List[List[(String, Int)]] = wordsAndBorders.map{x => genBIOCorpus(x)}

  def genFullCorpus(data: List[(String, Int)]): List[(String, Int)] = {
    val lengthOfCorpus = data.length
    val l = List.fill(1000 - lengthOfCorpus)("　" -> 2)
    data ::: l
  }

  val fullCorpus:List[List[(String, Int)]] = corpus.map{x => genFullCorpus(x)}

}

object Convert {

  def apply(path: String) = new Convert(path)

}
