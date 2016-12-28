import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
  * @author zhoujiagen
  */
object WordCount {
	def main(args : Array[String]) {
		// configuration and context
		val conf = new SparkConf().setAppName("Word Count Example")
		val sc = new SparkContext(conf)

		val inputFile = "data/README.md"
		val input = sc.textFile(inputFile)

		val words = input flatMap { line => line.split(" ") }
		val counts = words map { word => (word, 1) } reduceByKey { case (x, y) => x + y }

		// save
		counts.saveAsTextFile("data/output")
	}
}