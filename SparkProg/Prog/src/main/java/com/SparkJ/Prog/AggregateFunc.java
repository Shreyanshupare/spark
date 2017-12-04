package com.SparkJ.Prog;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

public class AggregateFunc implements Serializable {
	
		/*public AggregateFunc(int total, int num) {
		this.total = total;
		this.num = num;
		}
		public int total;
		public int num;
		public double avg() {
		return total / (double) num;
		}
		
		static Function2<AggregateFunc, Integer, AggregateFunc> addAndCount =
		new Function2<AvgCount, Integer, AvgCount>() {
		public AvgCount call(AvgCount a, Integer x) {
		a.total += x;
		a.num += 1;
		return a;
		}
		};
		static Function2<AggregateFunc, AggregateFunc, AggregateFunc> combine =
				new Function2<AvgCount, AvgCount, AvgCount>() {
				public AvgCount call(AvgCount a, AvgCount b) {
				a.total += b.total;
				a.num += b.num;
				return a;
				}
				};*/

/*
 val keysWithValuesList = Array("foo=A", "foo=A", "foo=A", "foo=A", "foo=B", "bar=C", "bar=D", "bar=D")

val data = sc.parallelize(keysWithValuesList)

//Create key value pairs

val kv = data.map(_.split("=")).map(v => (v(0), v(1))).cache()

val initialSet = mutable.HashSet.empty[String]

val addToSet = (s: mutable.HashSet[String], v: String) => s += v

val mergePartitionSets = (p1: mutable.HashSet[String], p2: mutable.HashSet[String]) => p1 ++= p2

val uniqueByKey = kv.aggregateByKey(initialSet)(addToSet, mergePartitionSets)
 */

		
		
		public static void NewRdd() {
			
			SparkConf conf = new SparkConf().setAppName("PairRDDApp").setMaster("local[2]");
			JavaSparkContext sc = new JavaSparkContext(conf);
			List<String> data = Arrays.asList("foo=A", "foo=A", "foo=A", "foo=A", "foo=B", "bar=C", "bar=D", "bar=D");
			JavaRDD rdd = sc.parallelize(data);
			//JavaRDD<String> dv = rdd.aggregate(0, seqOp, combOp)
		}
}
