# please modify the bucket path to your S3 folders.
BUCKET_PATH_INPUT = s3://flightprice/A4/data/
BUCKET_PATH_OUTPUT =  s3://flight/A4/data/output/
BUCKET_PATH_JAR = s3://flightprice/A4/FlightPrice.jar
PATH_N1 = s3://flightprice/A4/N1
PATH_N2 = s3://flightprice/A4/N200


# change the cluster id to the cluster you have created.
# before executing the job on cluster make sure you have uploaded the jar file to your S3.
CLUSTER_ID = yourClusterID
LOGS = s3://flightprice/A4/log/
JAR = WeeklyMedian.jar
MAINCLASS = LeastExpensive


#path where you have stored all the input files.
INPUT_PATH = /please/enter/your/path/to/all/files
HADOOP_INPUT_PATH = /path/where/all/files/stored/in/hdfs

# if you want to buid the jar file from given source file.
buildweeklyjar:
	javac -cp /usr/local/hadoop/share/hadoop/mapreduce/lib/hadoop-annotations-2.6.3.jar:/usr/local/hadoop/share/hadoop/common/hadoop-common-2.6.3.jar:/usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.6.3.jar:/usr/local/hadoop/share/hadoop/common/lib/commons-lang-2.6.jar:/usr/local/hadoop/share/hadoop/common/lib/avro-1.7.4.jar weeklySrc/LeastExpensive.java weeklySrc/LeastExpensiveMapper.java weeklySrc/LeastExpensiveReducer.java
	jar -cvf $(JAR) *.class


moveallfiles:
	hadoop fs -p -mkdir /user/airline
	hadoop fs -put $(INPUT_PATH) /user/airline

pseudon1:
	hadoop jar $(JAR) $(MAINCLASS) $(HADOOP_INPUT_PATH) /user/airline/output ../A04/N1
	
# remove previous output folder if present.
stepn1:
	aws emr add-steps --cluster-id $(CLUSTER_ID) --steps Type=CUSTOM_JAR,Name=$(JAR),ActionOnFailure=CONTINUE,Jar=$(BUCKET_PATH_JAR),Args=LeastExpensive,$(BUCKET_PATH_INPUT),$(BUCKET_PATH_OUTPUT),$(PATH_N1)
	aws s3 sync $(BUCKET_PATH_OUTPUT)

pseudon200:
	hadoop jar $(JAR) $(MAINCLASS) $(HADOOP_INPUT_PATH) /user/airline/output ../A04/N200

# remove previous output folder if present.
stepn200:
	aws emr add-steps --cluster-id $(CLUSTER_ID) --steps Type=CUSTOM_JAR,Name=$(JAR),ActionOnFailure=CONTINUE,Jar=$(BUCKET_PATH_JAR),Args=LeastExpensive,$(BUCKET_PATH_INPUT),$(BUCKET_PATH_OUTPUT),$(PATH_N200)
	aws s3 sync $(BUCKET_PATH_OUTPUT)



