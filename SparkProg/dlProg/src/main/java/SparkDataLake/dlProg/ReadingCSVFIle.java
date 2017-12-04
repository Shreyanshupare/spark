package SparkDataLake.dlProg;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class ReadingCSVFIle {
	
	public static void main(String[] args) {
		SparkSession spark = SparkSession
				.builder()
				.appName("Reading CSV")
//				.config("spark.some.config.option", "Master")
				.getOrCreate();
		Dataset<Row> df = spark.read().csv("adl://gou02rav32.azuredatalakestore.net/myFolder/DriverShiftTrips.csv");
		df.show();
	}
			
	
}
