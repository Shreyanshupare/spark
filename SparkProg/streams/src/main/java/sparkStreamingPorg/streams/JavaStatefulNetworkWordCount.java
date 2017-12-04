package sparkStreamingPorg.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import scala.Tuple2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.*;

public class JavaStatefulNetworkWordCount {
	
	private static final Pattern SPACE = Pattern.compile(" ");
	
	public static void main(String[] args) throws Exception {
		Thread.sleep(1000);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the hostname and port : ");
		String hostName = sc.nextLine();
		int portnum = sc.nextInt();
		
		if (hostName.length() < 2) {
		System.err.println("Usage: JavaStatefulNetworkWordCount <hostname> <port>");
		System.exit(1);
		}
		
		//StreamingExamples.setStreamingLogLevels();
		// Create the context with a 1 second batch size
		SparkConf sparkConf = new SparkConf().setAppName("JavaStatefulNetworkWordCount").setMaster("local[2]");
		JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
		ssc.checkpoint(".");
		// Initial state RDD input to mapWithState
		@SuppressWarnings("unchecked")
		List<Tuple2<String, Integer>> tuples = Arrays.asList(new Tuple2<>("hello", 1), new Tuple2<>("world", 1));
		JavaPairRDD<String, Integer> initialRDD = ssc.sparkContext().parallelizePairs(tuples);
		JavaReceiverInputDStream<String> lines = ssc.socketTextStream(hostName, portnum);
		JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
		JavaPairDStream<String, Integer> wordsDstream = words.mapToPair(s -> new Tuple2<>(s, 1));

		// Update the cumulative count function
		Function3<String, Optional<Integer>, State<Integer>, Tuple2<String, Integer>> mappingFunc =
		(word, one, state) -> {
		int sum = one.orElse(0) + (state.exists() ? state.get() : 0);
		Tuple2<String, Integer> output = new Tuple2<>(word, sum);
		state.update(sum);
		return output;
		};
		
		// DStream made of get cumulative counts that get updated in every batch
		JavaMapWithStateDStream<String, Integer, Integer, Tuple2<String, Integer>> stateDstream = 
				wordsDstream.mapWithState(StateSpec.function(mappingFunc).initialState(initialRDD));
		//JavaPairDStream<String, Integer> runningCounts = stateDstream.updateStateByKey(mappingFunc);
		stateDstream.print();
		ssc.start();
		ssc.awaitTermination();
		}

}
