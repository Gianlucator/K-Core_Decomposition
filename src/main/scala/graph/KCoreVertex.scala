package scala.graph

import org.apache.spark.graphx.VertexId

import scala.collection.Map
import scala.collection.immutable.HashMap

class KCoreVertex(id: VertexId) extends Serializable {
  val nodeId = id
  var updated = false
  var est: Map[VertexId, Int] = Map[VertexId, Int]()
  var coreness = 0
  var receivedMsg = 0
  var iterationToConverge = 0

  /**
    * Stima la coreness di un nodo, date le stime dei suoi vicini.
    * L'algoritmo si basa sulla proprietà di localita' secondo cui sono sufficienti
    * le informazioni sul vicinato per stimare la propria coreness. Ovviamente, l'algoritmo
    * e' di natura iterativa. Tale valore è il maggior valore di i tale che ci siano almeno i
    * in est di valore maggiore o uguale a i.
    * @return il maggiore valore di i tale che ci siano almeno i elementi maggiori o uguali di i in est
    */
  def computeIndex() = {
    val count = Array.fill[Int](this.coreness + 1)(0) //inizializzo un array di supporto a 0
    est.foreach(tuple => {
      val j = Math.min(coreness, tuple._2)
      count(j) = count(j) + 1
    })
    //nel vettore di supporto conto quante occorrenze ci sono con lo stesso valore dell'indice
    var i = coreness
    while (i > 2) {
      count(i - 1) = count(i - 1) + count(i)
      i = i - 1
    }
    //eseguo una somma prefissa inversa sul vettore di supporto
    i = coreness
    while (i > 1 && count(i) < i) {
      i = i - 1
    }//trovo l'effettivo valore di i che soddisfa la proprieta' di cui sopra
    i
  }

  /**
    * Informazioni che caratterizzano il nodo: identificativo e coreness
    * @return
    */
  override def toString: String = "\tCoreness:\t" + coreness + "\tReceived messages:\t" + receivedMsg + "\tDegree:\t" + est.size + "\tIterations to converge:\t" + iterationToConverge

  /**
    * Incrementa il numero di messaggi ricevuti
    * @param dim
    */
  def incReceived(dim: Int) = {
    receivedMsg = receivedMsg + dim
  }
}
