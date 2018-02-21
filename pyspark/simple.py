from pyspark import SparkContext, SparkConf
from operator import add

class sparkcore:
    def collection1(self,sc):
        data = [1,2,3,4,5]
        distData = sc.parallelize(data)
        print(distData.count())
    

    def basic(self,sc):
        lines = sc.textFile("C:\\spark-2.2.1-bin-hadoop2.7\\spark-2.2.1-bin-hadoop2.7\\README.md")
        lineLengths = lines.map(lambda s: len(s))
        totalLength = lineLengths.reduce(lambda a, b: a + b)
        lineLengths.persist()
        print(totalLength)
    

    def Flatmap(self,sc):
        rdd = sc.parallelize([2, 3, 4])
        print(sorted(rdd.flatMap(lambda x: range(1, x)).collect()))
    

    def Union(self,sc):
        rdd = sc.parallelize([1, 1, 2, 3])
        rdd1 = sc.parallelize([1, 4, 2, 5])
        print((rdd.union(rdd)).collect())
        print((rdd.intersection(rdd1)).collect())
    
    def Groupbykey(self,sc):
        rdd = sc.parallelize([("a", 1), ("b", 1), ("a", 1), ("c", 2)])
        print(sorted(rdd.groupByKey().mapValues(len).collect()))

    def Reducebykey(self,sc):
        rdd = sc.parallelize([("a", 1), ("b", 1), ("a", 1)])
        print(sorted(rdd.reduceByKey(add).collect()))

    def Sortbykey(self,sc):
        tmp = [('a', 1), ('b', 2), ('1', 3), ('d', 4), ('2', 5)]
        print(sc.parallelize(tmp).sortByKey(True,1).first())

    def Join(self,sc):
        x = sc.parallelize([("a", 1), ("b", 4)])
        y = sc.parallelize([("a", 2), ("a", 3)])
        print(sorted(x.join(y).collect()))

    def Cogroup(self,sc):
        x = sc.parallelize([("a", 1), ("b", 4)])
        y = sc.parallelize([("a", 2)])
        print([(x,tuple(map(list, y))) for x, y in sorted(list(x.cogroup(y).collect()))])
        
    def Pipe(self,sc):
        rdd = sc.parallelize(['1', '2', '', '3'])
        print(rdd.pipe('cat').collect())
        rdd.unpersist()

    def Broadcast(self,sc):
        hoods = ((1, "Mission"), (2, "SOMA"), (3, "Sunset"), (4, "Haight Ashbury"))
        checkins = ((234, 1),(567, 2), (234, 3), (532, 2), (234, 4))
        hoodsRdd = sc.parallelize(hoods)
        checkRdd = sc.parallelize(checkins)
        broadcastedHoods = sc.broadcast(hoodsRdd.collectAsMap())
        rowFunc = lambda x: (x[0], x[1], broadcastedHoods.value.get(x[1], -1))
        def mapFunc(partition):
            for row in partition:
                yield rowFunc(row)

        checkinsWithHoods = checkRdd.mapPartitions(mapFunc,preservesPartitioning=True)
        print(checkinsWithHoods.take(5))


        
conf = SparkConf().setAppName("simple").setMaster("local[*]")
sc = SparkContext(conf=conf)    
x = sparkcore()
#x.basic(sc)
#x.Union(sc)
#x.collection1(sc)
#x.Flatmap(sc)
#x.Groupbykey(sc)
#x.Reducebykey(sc)
#x.Sortbykey(sc)
#x.Join(sc)
#x.Cogroup(sc)
#x.Pipe(sc)    
x.Broadcast(sc)




    

