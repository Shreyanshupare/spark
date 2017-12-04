package com.SparkJ.Prog;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.avro.ipc.specific.Person;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import com.fasterxml.jackson.databind.ObjectMapper;

class ParseJson implements FlatMapFunction<Iterator<String>, Person> {
	public Iterator<Person> call(Iterator<String> lines) throws Exception {
	ArrayList<Person> people = new ArrayList<Person>();
	ObjectMapper mapper = new ObjectMapper();
	while (lines.hasNext()) {
	String line = lines.next();
	try {
	people.add(mapper.readValue(line, Person.class));
	} catch (Exception e) {
	// skip records on failure
		// skip records on failure
	}
	}
	return (Iterator<Person>) people;
	}
	}
public class LoadingJson {
	
	public static void loadingJson() {
		SparkConf conf = new SparkConf().setAppName("JsonApp").setMaster("local[2]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> input = sc.textFile("/home/bizruntime/new.json");
		JavaRDD<Person> result = input.mapPartitions(new ParseJson());
		result.foreach(x-> System.out.println(x));
		System.out.println(result.name());
	}
	public static void main(String[] args) {
		loadingJson();
	}
}
