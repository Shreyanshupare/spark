package org.bizruntime.data_frame;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.feature.Binarizer;
import org.apache.spark.ml.feature.DCT;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.PCAModel;
import org.apache.spark.ml.feature.PolynomialExpansion;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class Transformers {

	public static void main(String[] args)
	{
		SparkSession spark = SparkSession
				  .builder()
				  .appName("chisquare")
				  .master("local[2]")
				  .getOrCreate();
		
		//_binarizer(spark);
		//PCA_(spark);
		//PolynomialExpansion_(spark);
		Discrete_Cosine_Transform(spark);

	}
	private static void _binarizer(SparkSession spark)
	{
		List<Row> data = Arrays.asList(
				  RowFactory.create(0, 0.1),
				  RowFactory.create(1, 0.8),
				  RowFactory.create(2, 0.2)
				);
		System.out.println(data);
		
		StructType schema = new StructType(new StructField[]{
				  new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
				  new StructField("feature", DataTypes.DoubleType, false, Metadata.empty())
				});
		Dataset<Row> continuousDataFrame = spark.createDataFrame(data, schema);
		
		Binarizer binarizer = new Binarizer()
				  .setInputCol("feature")
				  .setOutputCol("binarized_feature")
				  .setThreshold(0.5);
		Dataset<Row> binarizedDataFrame = binarizer.transform(continuousDataFrame);

		System.out.println("Binarizer output with Threshold = " + binarizer.getThreshold());
		binarizedDataFrame.show();			
		
	}
	private static void PCA_(SparkSession spark)
	{
		List<Row> data = Arrays.asList(
				  RowFactory.create(Vectors.sparse(5, new int[]{1, 3}, new double[]{1.0, 7.0})),
				  RowFactory.create(Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0)),
				  RowFactory.create(Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0))
				);

				StructType schema = new StructType(new StructField[]{
				  new StructField("features", new VectorUDT(), false, Metadata.empty()),
				});

				Dataset<Row> df = spark.createDataFrame(data, schema);
				
				PCAModel pca = new PCA()
						  .setInputCol("features")
						  .setOutputCol("pcaFeatures")
						  .setK(3)
						  .fit(df);
				Dataset<Row> result = pca.transform(df).select("pcaFeatures");
				result.show(false);
	}
	private static void PolynomialExpansion_(SparkSession spark)
	{
		List<Row> data = Arrays.asList(
				  RowFactory.create(Vectors.dense(2.0, 1.0)),
				  RowFactory.create(Vectors.dense(0.0, 0.0)),
				  RowFactory.create(Vectors.dense(3.0, -1.0))
				);
		StructType schema = new StructType(new StructField[]{
				  new StructField("features", new VectorUDT(), false, Metadata.empty()),
				});
		Dataset<Row> df = spark.createDataFrame(data, schema);
		PolynomialExpansion polyExpansion = new PolynomialExpansion()
						  .setInputCol("features")
						  .setOutputCol("polyFeatures")
						  .setDegree(3);
		Dataset<Row> polyDF = polyExpansion.transform(df);
		polyDF.show(false);
		
	}
	private static void Discrete_Cosine_Transform(SparkSession spark)
	{
		List<Row> data = Arrays.asList(
				  RowFactory.create(Vectors.dense(0.0, 1.0, -2.0, 3.0)),
				  RowFactory.create(Vectors.dense(-1.0, 2.0, 4.0, -7.0)),
				  RowFactory.create(Vectors.dense(14.0, -2.0, -5.0, 1.0))
				);
				StructType schema = new StructType(new StructField[]{
				  new StructField("features", new VectorUDT(), false, Metadata.empty()),
				});
				Dataset<Row> df = spark.createDataFrame(data, schema);
				DCT dct = new DCT()
						  .setInputCol("features")
						  .setOutputCol("featuresDCT")
						  .setInverse(false);

				Dataset<Row> dctDf = dct.transform(df);

				dctDf.select("featuresDCT").show(false);

	}

}
