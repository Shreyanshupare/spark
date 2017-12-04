package com.SparkJ.Prog;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class LoadingFiles {
	public static void loadFile() {
		SparkConf conf = new SparkConf().setAppName("FirstApp2").setMaster("local[2]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaPairRDD<String, String> input = sc.wholeTextFiles("/home/bizruntime/files12");
		
		JavaPairRDD<String, String> result = input.mapValues(x -> x.toLowerCase().trim());
		result.coalesce(6, true);
		result.rdd().count();
		result.groupByKey();
		System.out.println(StringUtils.join(result.collect(), " "));
		
		String path1 = "/home/bizruntime/files12/details.txt";
		String path2 = "/home/bizruntime/files12/moreDet.txt";
		JavaRDD<String> file1 = sc.textFile(path1);
		JavaRDD<String> file2 = sc.textFile(path2);
		
		File f = new File("/home/bizruntime/files12/combo.txt");
		try {
			boolean done = f.createNewFile();
			System.out.println(done);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JavaRDD<String> res = file1.union(file2);
		System.out.println(res.collect());
		res.cartesian(file2);
		System.out.println(res.take(25));
		//res.saveAsTextFile("/home/bizruntime/files12");
		
		
		
		
	}
	public static void main(String[] args) {
		loadFile();
	}
}
