package com.SparkJ.Prog;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.reflect.internal.Trees.CollectTreeTraverser;

public class PairRdds {
	public static void pariRdd() {
		SparkConf conf = new SparkConf().setAppName("PairRDDApp").setMaster("local[2]");
    	JavaSparkContext sc = new JavaSparkContext(conf);
		List<Tuple2> data =  Arrays.asList(new Tuple2("Lion", 0),
				new Tuple2("Tiger", 1), 
				new Tuple2("Leopard", 2),
				new Tuple2("Bear", 3),
				new Tuple2("Hyna", 4),
				new Tuple2("Elephant", 5),
				new Tuple2("Giraffe", 6),
				new Tuple2("Deer", 7),
				new Tuple2("Fox", 8),
				new Tuple2("Zebra", 9),
				new Tuple2("Rhinoceros", 10));
	    JavaRDD rdd = sc.parallelize(data);
	    JavaPairRDD<String, Integer> pairRdd = JavaPairRDD.fromJavaRDD(rdd);
	    
	    List<Tuple2> data2 =  Arrays.asList(new Tuple2("Lioness", 0),
				new Tuple2("Tigeress", 1), 
				new Tuple2("Leopardess", 2),
				new Tuple2("Bearess", 3),
				new Tuple2("Hynass", 4),
				new Tuple2("Elephantess", 5),
				new Tuple2("Giraffeess", 6),
				new Tuple2("Deeress", 7),
				new Tuple2("vixen", 8),
				new Tuple2("Zebress", 9),
				new Tuple2("Rhinocerosess", 10));
	    JavaRDD rdd2 = sc.parallelize(data2);
	    JavaPairRDD pair2Rdd = JavaPairRDD.fromJavaRDD(rdd2);
	    
	    pairRdd.cartesian(pair2Rdd);
	    JavaPairRDD keysS = pairRdd.join(pair2Rdd);
	    System.out.println(((Object) keysS).toString());
	    pairRdd.groupByKey();
	    pairRdd.cogroup(pair2Rdd);
	    pairRdd.rightOuterJoin(pair2Rdd);
	    pairRdd.sortByKey(true);
	    pairRdd.sortByKey((o1, o2)-> {return o1.compareTo(o2);});
	    Map<String, Long> keyCount = pairRdd.countByKey();
	    System.out.println(keyCount);
	    //Map<String, Integer> mapEle = pairRdd.collectAsMap();
	    //System.out.println(mapEle);
	    System.out.println(pairRdd.lookup("Tiger"));
	   // pairRdd.saveAsTextFile("/home/bizruntime/files12/details.txt");
	    
	    //JavaRDD parRdd = sc.sequenceFile("/home/bizruntime/new.json", name, age);
	    
	    
	  
	    
	}
	public static void main(String[] args) {
		pariRdd();
	}
	
	
}
