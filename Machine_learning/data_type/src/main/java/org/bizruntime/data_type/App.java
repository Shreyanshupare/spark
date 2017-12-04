package org.bizruntime.data_type;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.BlockMatrix;
import org.apache.spark.mllib.linalg.distributed.CoordinateMatrix;
import org.apache.spark.mllib.linalg.distributed.IndexedRow;
import org.apache.spark.mllib.linalg.distributed.IndexedRowMatrix;
import org.apache.spark.mllib.linalg.distributed.MatrixEntry;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Matrices;



public class App 
{
	static SparkConf conf = new SparkConf().setAppName("data_type").setMaster("local[2]");
	static JavaSparkContext jsc = new JavaSparkContext(conf);
    public static void main( String[] args )
    {
    	
    	//Vector();
    	//Labelpoint();
    	matrix();
    	//IndexedRowMatrix();
    	//CoordinateMatrix();
    	
    }
    public static void Vector()
    {
        Vector dv = Vectors.dense(1.0, 0.0, 3.0);
    	
    	System.out.println(dv);
    	
    	Vector sv = Vectors.sparse(3, new int[] {0, 2}, new double[] {1.0, 3.0});
    	
    	System.out.println(sv);
    	
    }
    public static void Labelpoint()
    {
    	LabeledPoint dpos = new LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0));
    	
    	System.out.println(dpos);
    	
    	LabeledPoint dneg = new LabeledPoint(0.0, Vectors.dense(1.0, 0.0, 3.0));
    	
    	System.out.println(dneg);
    	
        LabeledPoint spos = new LabeledPoint(1.0, Vectors.sparse(3, new int[] {0, 2}, new double[] {1.0, 3.0}));
    	
    	System.out.println(spos);
    	
    	LabeledPoint sneg = new LabeledPoint(0.0, Vectors.sparse(3, new int[] {0, 2}, new double[] {1.0, 3.0}));
    	
    	System.out.println(sneg);
    	
    	SparkContext sc = new SparkContext();
    	JavaRDD<LabeledPoint> examples =  MLUtils.loadLibSVMFile(sc, "C:\\Users\\bizruntime41\\Documents\\java\\workspace\\Machine_learning\\sample_libsvm_data.txt").toJavaRDD();
        System.out.println(examples.collect());
    }
    public static void matrix()
    {
//    	Matrix dm = Matrices.dense(3, 2, new double[] {1.0, 3.0, 5.0, 2.0, 4.0, 6.0});
//    	System.out.println(dm);
//    	System.out.println("+++++++++++++++");
    	
    	Matrix sm = Matrices.sparse(3, 2, new int[] {0, 1, 3}, new int[] {0, 2, 1}, new double[] {9, 6, 8});
    	System.out.println(sm);
    	System.out.println("=========");
    	Matrix sm1 = Matrices.sparse(3, 3, new int[] {0, 2, 3,6}, new int[] {0, 2, 1, 0, 1, 2}, new double[] {1,2,3,4,5,6});
    	System.out.println(sm1);
    	System.out.println("=========");
    	Matrix sm3 = Matrices.sparse(3, 2, new int[] {0, 3,4}, new int[] {0, 2, 1, 2}, new double[] {1,3,5,6});
    	System.out.println(sm3);
    	System.out.println("=========");
    	
//    	JavaRDD<Vector> rows = jsc.parallelize(Arrays.asList(Vectors. dense(150,60,25), Vectors.dense(300,80,40))) ;
//    	RowMatrix mat = new RowMatrix(rows.rdd());
//        System.out.println(mat);
//    	System.out.println(mat.numRows());
//    	System.out.println(mat.numCols());
    	
  	
    }
    @SuppressWarnings("unused")
	private static void IndexedRowMatrix() {
        IndexedRow iRow1 = new IndexedRow(0, Vectors.sparse(2, new int[]{0}, new double[]{1.0}));
        IndexedRow iRow2 = new IndexedRow(1, Vectors.sparse(2, new int[]{0, 1}, new double[]{3.0, 2.0}));
        IndexedRow iRow3 = new IndexedRow(2, Vectors.sparse(2, new int[]{1}, new double[]{6.0}));

        List<IndexedRow> inputList = new ArrayList<IndexedRow>();
        inputList.add(iRow1);
        inputList.add(iRow2);
        inputList.add(iRow3);
        
        JavaRDD<IndexedRow> rows = jsc.parallelize(inputList);

        IndexedRowMatrix matrix = new IndexedRowMatrix(rows.rdd());
        System.out.println("Row count: " + matrix.numRows() + ", Column count: " + matrix.numCols());
    }
    private static void CoordinateMatrix() {
        MatrixEntry m1 = new MatrixEntry(0, 0, 1.0);
        MatrixEntry m3 = new MatrixEntry(1, 0, 3.0);
        MatrixEntry m4 = new MatrixEntry(1, 1, 2.0);
        MatrixEntry m6 = new MatrixEntry(2, 1, 6.0);

        List<MatrixEntry> matrixEntries = Arrays.asList(new MatrixEntry[]{m1, m3, m4, m6});
        JavaRDD<MatrixEntry> matrixEntryRDD = jsc.parallelize(matrixEntries);

        CoordinateMatrix coordinateMatrix = new CoordinateMatrix(matrixEntryRDD.rdd());
        System.out.println("Row count: " + coordinateMatrix.numRows());
        System.out.println("Column count: " + coordinateMatrix.numCols());
        
        BlockMatrix matA = coordinateMatrix.toBlockMatrix().cache();
        matA.validate();
        BlockMatrix ata = matA.transpose().multiply(matA);
        System.out.println(ata);
    }

	
}
