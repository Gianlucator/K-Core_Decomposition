name := "KCoreDecomposition"

version := "0.1"

scalaVersion := "2.11.12"


lazy val sparkVersion = "2.4.0"
lazy val spark = "org.apache.spark"


libraryDependencies ++= Seq(
  spark %% "spark-core" % sparkVersion,
  spark %% "spark-sql" % sparkVersion,
  spark %% "spark-streaming" % sparkVersion,
  spark %% "spark-graphx" % sparkVersion
)