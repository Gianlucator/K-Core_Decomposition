
import graph.{DistributedKCore, GraphReader}
import org.apache.spark._
import org.apache.log4j._
import org.apache.spark.sql.SparkSession

object Main {

  def main(args: Array[String]) {
    // var fileName = "../../resources/facebook_combined.txt"
    var fileName = "s3n://scpproject/facebook_combined.txt"
    println("Size: " + args.size)
    if (args.size > 0) {
      fileName = args(0)
    }

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    var sessionBuilder = SparkSession.builder().appName("KCoreDecomposition")
    if (args.size > 1) {
      sessionBuilder = sessionBuilder.master(args(1))
    }
    val sc = sessionBuilder.getOrCreate()
    val maxIterations = 10
    val graph = new GraphReader().readFile(fileName)
    val kCore = DistributedKCore.decomposeGraph(graph, maxIterations)
    kCore.vertices.collect().take(15).sortBy(_._1).foreach(x => println(x._2))
    sc.close()
  }
}
