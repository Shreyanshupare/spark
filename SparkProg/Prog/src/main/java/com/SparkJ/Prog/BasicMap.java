package com.SparkJ.Prog;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class BasicMap {
	public static void main(String[] args) throws Exception {
		String master;
		if (args.length > 0) {
		master = args[0];
		} else {
		master = "local";
		}
		JavaSparkContext sc = new JavaSparkContext(
		master, "basicmap", System.getenv("SPARK_HOME"), System.getenv("JARS"));
		JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4));
		JavaRDD<Integer> result = rdd.map(x -> (x*x));
		System.out.println(StringUtils.join(result.collect(), ","));
		JavaRDD<Integer> result2 = rdd.map(x -> (x/x));
		System.out.println(StringUtils.join(result2.collect(), ", "));
		JavaRDD result3 = rdd.map(x -> (x*x*x));
		System.out.println(StringUtils.join(result3.collect(), ", "));
		}
	}

