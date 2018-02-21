from pyspark.sql.session import SparkSession

def simple():
    session = SparkSession.builder.master('local').appName("Spark").getOrCreate()
    dataframe = session.read.format('com.crealytics.spark.excel') \
        .option('useHeader', True) \
        .option('timestampFormat', 'MM-dd-yyyy HH:mm:ss') \
        .option('inferSchema', False) \
        .load(r'F:\projects\Spark_Daniel\Spark_Daniel\ds.xlsx')
    dataframe.createOrReplaceTempView("data")
    sqlDF = session.sql("SELECT order_id,_audit__updated_at_utc FROM (\r\n" + 
    "    SELECT *, row_number() OVER (\r\n" + 
    "        PARTITION BY order_id ORDER BY _audit__updated_at_utc DESC\r\n" + 
    "    ) rank FROM data\r\n" + 
    " ) tmp WHERE rank = 1")
    sqlDF.show()
    

if __name__=='__main__':
    simple()
