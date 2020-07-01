package org.bizruntime.Statistics;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrices;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.stat.KernelDensity;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.mllib.stat.test.BinarySample;
import org.apache.spark.mllib.stat.test.ChiSqTestResult;
import org.apache.spark.mllib.stat.test.StreamingTest;
import org.apache.spark.mllib.stat.test.StreamingTestResult;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.spark_project.guava.collect.ImmutableMap;

import scala.Tuple2;

public class App 
{
	static SparkConf conf = new SparkConf().setAppName("Statistics").setMaster("local[2]");
	static JavaSparkContext jsc = new JavaSparkContext(conf);
    public static void main( String[] args )
    {
    	//MultivariateStatisticalSummary();
    	//Correlation();
    	//Stratified_sampling();
    	//Hypothesis_testing();
    	//Streaming_Significance_Testing();
    	Kernel_density_estimation();
    }
    private static void MultivariateStatisticalSummary()
    {
    	List<Vector> list = Arrays.asList(
    		    Vectors.dense(1.0, 10.0, 100.0),
    		    Vectors.dense(2.0, 20.0, 200.0),
    		    Vectors.dense(3.0, 30.0, 300.0)
    		  );
    	System.out.println(list);
    	JavaRDD<Vector> mat = jsc.parallelize(list);
    	System.out.println(mat.count());
    	System.out.println("++++++++++++++++++++++++++++++");
    	MultivariateStatisticalSummary  summary = Statistics.colStats(mat.rdd());
    	System.out.println(summary);
    	System.out.println("++++++++++++++++++++++++++++++");
    	System.out.println(summary.mean());
    	System.out.println("++++++++++++++++++++++++++++++");
    	System.out.println(summary.count());
    	System.out.println("++++++++++++++++++++++++++++++");
    	System.out.println(summary.variance());
    	System.out.println("++++++++++++++++++++++++++++++");
    	System.out.println(summary.normL1());
    	System.out.println("++++++++++++++++++++++++++++++");
    	System.out.println(summary.normL2());
    	System.out.println("++++++++++++++++++++++++++++++");
    }
    private static void Correlation(){
    	JavaDoubleRDD seriesX = jsc.parallelizeDoubles(Arrays.asList(1.0, 2.0, 3.0, 3.0, 5.0));
    	JavaDoubleRDD seriesY = jsc.parallelizeDoubles(Arrays.asList(11.0, 22.0, 33.0, 33.0, 555.0));
    	
    	Double correlation = Statistics.corr(seriesX.srdd(), seriesY.srdd(), "pearson");
    	System.out.println("Correlation is: " + correlation);
    	
    	JavaRDD<Vector> data = jsc.parallelize(
    			  Arrays.asList(
    			    Vectors.dense(1.0, 10.0, 100.0),
    			    Vectors.dense(2.0, 20.0, 200.0),
    			    Vectors.dense(5.0, 33.0, 366.0)
    			  )
    			);
    	
    	Matrix correlMatrix = Statistics.corr(data.rdd(), "pearson");
    	System.out.println(correlMatrix.toString());
    }
    private static void Stratified_sampling()
    {
    	List<Tuple2<Integer, Character>> list = Arrays.asList(
    			new Tuple2<>(1, 'a'),
    		    new Tuple2<>(1, 'b'),
    		    new Tuple2<>(2, 'c'),
    		    new Tuple2<>(2, 'd'),
    		    new Tuple2<>(2, 'e'),
    		    new Tuple2<>(3, 'f')
    		);
    	System.out.println(list);
    	System.out.println("++++++++++++++++++");
    	JavaPairRDD<Integer, Character> data = jsc.parallelizePairs(list);
    	//System.out.println(data);
    	System.out.println("++++++++++++++++++");
    	
    	ImmutableMap<Integer, Double> fractions = ImmutableMap.of(1, 0.1, 2, 0.6, 3, 0.3);
    	System.out.println(fractions);
    	
    	JavaPairRDD<Integer, Character> approxSample = data.sampleByKey(false, fractions);
    	//System.out.println(approxSample.collectAsMap());
    	System.out.println(approxSample.collect());
    	//System.out.println(approxSample.id());
    	//System.out.println(approxSample.collect().size());
    	
    	
    	
    	JavaPairRDD<Integer, Character> exactSample = data.sampleByKeyExact(false, fractions);
    	System.out.println(exactSample.collect());
    	System.out.println(exactSample.count());
    }
    private static void Hypothesis_testing()
    {
    	Vector vec = Vectors.dense(0.1, 0.15, 0.2, 0.3, 0.25);
    	Vector vec1 = Vectors.dense(0.2, 0.18, 0.3, 0.4, 0.29);
    	ChiSqTestResult goodnessOfFitTestResult = Statistics.chiSqTest(vec,vec1);
    	System.out.println(goodnessOfFitTestResult + "\n");
    	
    	Matrix mat = Matrices.dense(3, 2, new double[]{1.0, 3.0, 5.0, 2.0, 4.0, 6.0});
    	System.out.println(mat);
    	ChiSqTestResult independenceTestResult = Statistics.chiSqTest(mat);
    	System.out.println(independenceTestResult);
    	
    	System.out.println("++++++++++++++++++++");
    	JavaRDD<LabeledPoint> obs = jsc.parallelize(
    			  Arrays.asList(
    			    new LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0)),
    			    new LabeledPoint(1.0, Vectors.dense(1.0, 2.0, 0.0)),
    			    new LabeledPoint(-1.0, Vectors.dense(-1.0, 0.0, -0.5))
    			  )
    			);
    	
    	System.out.println(obs);
    	ChiSqTestResult[] featureTestResults = Statistics.chiSqTest(obs.rdd());
    	int i = 1;
    	for (ChiSqTestResult result : featureTestResults) {
    		  System.out.println("Column " + i + ":");
    		  System.out.println(result + "\n");  // summary of the test
    		  i++;
    		}
    }
    private static void Streaming_Significance_Testing()
    {
    	JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1000));
    	JavaDStream<BinarySample> data = ssc.textFileStream("").map(line -> {
    		  String[] ts = line.split(",");
    		  boolean label = Boolean.parseBoolean(ts[0]);
    		  double value = Double.parseDouble(ts[1]);
    		  return new BinarySample(label, value);
    		});
    	StreamingTest streamingTest = new StreamingTest()
    			  .setPeacePeriod(0)
    			  .setWindowSize(0)
    			  .setTestMethod("welch");

    	JavaDStream<StreamingTestResult> out = streamingTest.registerStream(data);
    	out.print();
    }
    private static void Kernel_density_estimation()
    {
    	JavaRDD<Double> data = jsc.parallelize(Arrays.asList(1.0, 1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 5.0, 6.0, 7.0, 8.0, 9.0, 9.0));
    	KernelDensity kd = new KernelDensity().setSample(data).setBandwidth(3.0);
    	double[] densities = kd.estimate(new double[]{-1.0, 2.0, 5.0});

    	System.out.println(Arrays.toString(densities));

    }
}








