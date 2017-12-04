package com.SparkJ.Prog;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import scala.reflect.api.Internals.ReificationSupportApi.SyntacitcSingletonTypeExtractor;

/**
 * Hello world!
 *
 */
public class App 
{
    	//private static String appName;
		//public static String master;
		public static void sparkPrac() {
			SparkConf conf = new SparkConf().setAppName("FirstApp").setMaster("local[2]");
	    	JavaSparkContext sc = new JavaSparkContext(conf);
	    	List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
	    	JavaRDD<Integer> distData = sc.parallelize(data);
	    	JavaRDD<String> lists = sc.parallelize(Arrays.asList("Panda", "I love Panda"));
	    	System.out.println(data);
	    	System.out.println(distData.first());
	    	
	    	
	    	System.out.println(lists.count() + " " + lists.first() + " " + lists.hashCode()+ " " + lists.id()
	    	+ " " + lists.name() + " " + lists.coalesce(1)+ " " + lists.context());
	    	
	    	String path = "/home/bizruntime/fea.txt";
	    	JavaRDD<String> lines = sc.textFile(path);
	    	//System.out.println(lines.map(s -> s.length()).reduce((a, b) -> a + b));
	    	//System.out.println(lines.count()+" "+lines.first() + " " + lines.hashCode() + " " + lines.distinct()
	    	//+ " "+ lines.isEmpty() + " " + lines.getStorageLevel() + " " + lines.top(10) + " "+lines.glom());
	    	/*JavaRDD<String> filterRdd = lines.filter(new Function<String, Boolean>(){
	    		public Boolean call(String x) { return x.contains("floppy");}
	    	});
	    	filterRdd = lines.filter((String x)->x.contains("you"));
	    	System.out.println(StringUtils.join(filterRdd.collect(), ", "));
	    	JavaRDD<String> wordsContain = lines.filter((String x)-> x.contains("DOS"));
	    	System.out.println(StringUtils.join(wordsContain.collect(), ", "));
	    	JavaRDD<String> unionRdd = wordsContain.union(filterRdd);
	    	System.out.println("Input had" + " " + unionRdd.count() + " " + "Concerning Lines");
	    	System.out.println("Here are 10 Examples ");
	    	for(String line : unionRdd.take(10)) {
	    		System.out.println(line);
	    	}*/
	    	
	    	/*JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4));
	    	JavaRDD<Integer> results = rdd.map(new Function<Integer, Integer>(){
	    		public Integer call(Integer x) { return x*x; }
	    	});
	    	System.out.println(StringUtils.join(results.collect(), ", "));
	    	
	    	Integer sum = rdd.reduce((Integer x, Integer y)-> {return x+y;});
	    	System.out.println(sum.intValue());
	    	//System.out.println(sum.fold(0)((Integer x, Integer y)-> {return x+y;}));
	    	
	    	JavaDoubleRDD javaDoubleRdd = rdd.mapToDouble(new DoubleFunction<Integer>() {
	    		public double call(Integer x) {return (double) x * x;
	    		}
	    	});
	    	System.out.println(results.hashCode());
	    	
	    	PairFunction<String, String, String> keyData =
	    			new PairFunction<String, String, String>() {
	    			public Tuple2<String, String> call(String x) {
	    			return new Tuple2(x.split(" ")[0], x);
	    			}
	    			};
	    			JavaPairRDD<String, String> pairs = lines.mapToPair(keyData);
	    			
	    			JavaRDD<String> lines2 = sc.textFile("/home/bizruntime/boost.txt");
	    			JavaPairRDD<String, Integer> pairs3 = lines2.mapToPair(s -> new Tuple2(s, 1));
	    			JavaPairRDD<String, Integer> counts = pairs3.reduceByKey((a, b) -> a + b);
	    			
	    			JavaRDD<String> words = lines2.flatMap(new FlatMapFunction<String, String>() {
	    				public Iterator<String> call(String x) { return (Iterator<String>) Arrays.asList(x.split(" ")); }
	    				});
	    			JavaPairRDD<String, Integer> result1 = words.mapToPair(
	    					new PairFunction<String, String, Integer>() {
	    					public Tuple2<String, Integer> call(String x) { return new Tuple2(x, 1); }
	    					}).reduceByKey(new Function2<Integer, Integer, Integer>() {
	    					public Integer call(Integer a, Integer b) { return a + b; }
	    					});
	    			
	    			
	    			JavaPairRDD<String, String> result = pairs.filter((Function<Tuple2<String, String>, Boolean>) lines2);
	    			
	    			System.out.println(counts.take(25));
	    			counts.mapValues(a->a+1);
	    			System.out.println(counts.take(35));
	    			counts.groupByKey();
	    			System.out.println(counts.collect());
	    			//counts.flatMapValues(a->a );
	    			System.out.println(counts.keys() + " " + counts.values() + " "+counts.sortByKey());
	    			System.out.println(StringUtils.join(counts.collect(), " "));
	    			//counts.rightOuterJoin(rdd);
*/	    			
	    			
	    			
	    	
	    	
	    	JavaRDD<String> lines1 = sc.parallelize(Arrays.asList("Hello world", "hi", "Fine", "Doing"));
	    	JavaRDD<String> words1 = lines1.flatMap(new FlatMapFunction<String, String>() {
	    		@SuppressWarnings("unchecked")
				public Iterator<String> call(String line) {
	    			return (Iterator<String>) Arrays.asList(line.split(" "));
	    		}
			});
	    	System.out.println(words1.first());
	    	
		}
		public static void main(String[] args) {
			sparkPrac();
		}
    	
    
}
