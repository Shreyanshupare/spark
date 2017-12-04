package org.bizruntime.algorithms;

import org.apache.spark.ml.clustering.BisectingKMeans;
import org.apache.spark.ml.clustering.BisectingKMeansModel;
import org.apache.spark.ml.clustering.GaussianMixture;
import org.apache.spark.ml.clustering.GaussianMixtureModel;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.clustering.LDA;
import org.apache.spark.ml.clustering.LDAModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class K_means {

	public static void main(String[] args)
	{
		SparkSession spark = SparkSession
				  .builder()
				  .appName("chisquare")
				  .master("local[2]")
				  .getOrCreate();
		//_kmeans(spark);
		//Latent_Dirichlet_allocation(spark);
		//Bisecting_k_means(spark);
		Gaussian_Mixture_Model(spark);

	}
	private static void _kmeans(SparkSession spark)
	{
		Dataset<Row> dataset = spark.read().format("libsvm").load("C:\\Users\\bizruntime41\\Documents\\java\\workspace\\Machine_learning\\sample_kmeans_data.txt");
		KMeans kmeans = new KMeans().setK(3).setSeed(1L);
		KMeansModel model = kmeans.fit(dataset);
		
		double WSSSE = model.computeCost(dataset);
		System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

		// Shows the result.
		Vector[] centers = model.clusterCenters();
		System.out.println("Cluster Centers: ");
		for (Vector center: centers) {
		  System.out.println(center);
		}
	}
	private static void Latent_Dirichlet_allocation(SparkSession spark)
	{
		Dataset<Row> dataset = spark.read().format("libsvm")
				  .load("C:\\Users\\bizruntime41\\Documents\\java\\workspace\\Machine_learning\\sample_lda_libsvm_data.txt");

				// Trains a LDA model.
				LDA lda = new LDA().setK(10).setMaxIter(10);
				LDAModel model = lda.fit(dataset);

				double ll = model.logLikelihood(dataset);
				double lp = model.logPerplexity(dataset);
				System.out.println("The lower bound on the log likelihood of the entire corpus: " + ll);
				System.out.println("The upper bound on perplexity: " + lp);

				// Describe topics.
				Dataset<Row> topics = model.describeTopics(3);
				System.out.println("The topics described by their top-weighted terms:");
				topics.show(false);

				// Shows the result.
				Dataset<Row> transformed = model.transform(dataset);
				transformed.show(false);
	}
	private static void Bisecting_k_means(SparkSession spark)
	{
		Dataset<Row> dataset = spark.read().format("libsvm").load("C:\\Users\\bizruntime41\\Documents\\java\\workspace\\Machine_learning\\sample_kmeans_data.txt");

		
		BisectingKMeans bkm = new BisectingKMeans().setK(3).setSeed(1);
		BisectingKMeansModel model = bkm.fit(dataset);

		
		double cost = model.computeCost(dataset);
		System.out.println("Within Set Sum of Squared Errors = " + cost);

		
		System.out.println("Cluster Centers: ");
		Vector[] centers = model.clusterCenters();
		for (Vector center : centers) {
		  System.out.println(center);
		}
	}
	private static void Gaussian_Mixture_Model(SparkSession spark)
	{
		Dataset<Row> dataset = spark.read().format("libsvm").load("C:\\Users\\bizruntime41\\Documents\\java\\workspace\\Machine_learning\\sample_kmeans_data.txt");

		
		GaussianMixture gmm = new GaussianMixture().setK(2);
		GaussianMixtureModel model = gmm.fit(dataset);
		
		for (int i = 0; i < model.getK(); i++) {
		  System.out.printf("Gaussian %d:\nweight=%f\nmu=%s\nsigma=\n%s\n\n",
		          i, model.weights()[i], model.gaussians()[i].mean(), model.gaussians()[i].cov());
		}
	}
}
