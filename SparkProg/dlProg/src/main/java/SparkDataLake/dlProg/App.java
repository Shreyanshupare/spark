package SparkDataLake.dlProg;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.collection.parallel.ParIterableLike.Take;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void dLake() {
		SparkConf conf = new SparkConf().setAppName("MyDataLake");
    	JavaSparkContext sc = new JavaSparkContext(conf);
    	//String path = "adl://gou02rav32.azuredatalakestore.net/myFolder/Drivers.txt";
    	JavaRDD<String> lines = sc.textFile("adl://gou02rav32.azuredatalakestore.net/myFolder/Drivers.txt");
    	long counts = lines.count();
    	System.out.println(counts);
    	List<String> in = lines.take(25);
    	System.out.println(in);
    	//JavaRDD<String> filtering = lines.filter((x)-> x.length()>3);
    	//System.out.println(StringUtils.join(filtering.collect(), ","));
	}
	public static void main( String[] args )
    {
        System.out.println( "Spark DatLake Starting " );
        dLake();
    }
}
