package sparkStreamingPorg.streams;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function0;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.util.LongAccumulator;

import com.google.common.io.Files;

import scala.Tuple2;

class JavaWordBlacklist {
	private static volatile Broadcast<List<String>> instance = null;
	public static Broadcast<List<String>> getInstance(JavaSparkContext jsc) {
		if (instance == null) {
			synchronized (JavaWordBlacklist.class) {
				if (instance == null) {
					List<String> wordBlacklist = Arrays.asList("a", "b", "c");
					instance = jsc.broadcast(wordBlacklist);
				}
			}
		}
			return instance;
	}
}

class JavaDroppedWordsCounter {
	private static volatile LongAccumulator instance = null;
	public static LongAccumulator getInstance(JavaSparkContext jsc) {
		if (instance == null) {
			synchronized (JavaDroppedWordsCounter.class) {
				if (instance == null) {
					instance = jsc.sc().longAccumulator("WordsInBlacklistCounter");
				}
			}
	}
return instance;
}
}
public class JavaRecoverableNetworkWordCount {
	//private static final Pattern SPACE = Pattern.compile(" ");
	
	private static JavaStreamingContext createContext(String ip, int port,String checkpointDirectory, String outputPath) {
	// If you do not see this printed, that means the StreamingContext has been loaded
	// from the new checkpoint
	System.out.println("Creating new context");
	File outputFile = new File(outputPath);
	if (outputFile.exists()) {
	outputFile.delete();
	}
	SparkConf sparkConf = new SparkConf().setAppName("JavaRecoverableNetworkWordCount").setMaster("local[2]");
	// Create the context with a 1 second batch size
	JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(1));
	ssc.checkpoint(checkpointDirectory);
	// Create a socket stream on target ip:port and count the
	// words in input stream of \n delimited text (eg. generated by 'nc')
	JavaReceiverInputDStream<String> lines = ssc.socketTextStream(ip, port);
	JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
	JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
			 .reduceByKey((i1, i2) -> i1 + i2);
	wordCounts.foreachRDD((rdd, time) -> {
	// Get or register the blacklist Broadcast
	Broadcast<List<String>> blacklist =
	JavaWordBlacklist.getInstance(new JavaSparkContext(rdd.context()));
	// Get or register the droppedWordsCounter Accumulator
	LongAccumulator droppedWordsCounter =
	JavaDroppedWordsCounter.getInstance(new JavaSparkContext(rdd.context()));
	// Use blacklist to drop words and use droppedWordsCounter to count them
	String counts = rdd.filter(wordCount -> {
		if (blacklist.value().contains(wordCount._1())) {
			droppedWordsCounter.add(wordCount._2());
			return false;
				} else {
					return true;
				}
		}).collect().toString();
	String output = "Counts at time " + time + " " + counts;
	System.out.println(output);
	System.out.println("Dropped " + droppedWordsCounter.value() + " word(s) totally");
	System.out.println("Appending to " + outputFile.getAbsolutePath());
	Files.append(output + "\n", outputFile, Charset.defaultCharset());
	});
	return ssc;
	}
	public static void main(String[] args) throws Exception {
		/*if (args.length != 4) {
		System.err.println("You arguments were " + Arrays.asList(args));
		System.err.println(
		"Usage: JavaRecoverableNetworkWordCount <hostname> <port> <checkpoint-directory>\n" +
		" <output-file>. <hostname> and <port> describe the TCP server that Spark\n" +
		" Streaming would connect to receive data. <checkpoint-directory> directory to\n" +
		" HDFS-compatible file system which checkpoint data <output-file> file to which\n" +
		" the word counts will be appended\n" +
		"\n" +
		"In local mode, <master> should be 'local[n]' with n > 1\n" +
		"Both <checkpoint-directory> and <output-file> must be absolute paths");
		System.exit(1);
		}*/
		String ip = "localhost";
		int port = 9999;
		String checkpointDirectory = "localhost:9999\\home\\bizruntime\\files12";
		String outputPath = "\\home\\bizruntime\\files12";
		// Function to create JavaStreamingContext without any output operations
		// (used to detect the new context)
		Function0<JavaStreamingContext> createContextFunc =
		() -> createContext(ip, port, checkpointDirectory, outputPath);
		JavaStreamingContext ssc =
		JavaStreamingContext.getOrCreate(checkpointDirectory, createContextFunc);
		 ssc.start();
		 ssc.awaitTermination();
		 }
}
