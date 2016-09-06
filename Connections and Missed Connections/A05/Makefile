#------------------------------------------
#Configuration for AWS
BUCKET_PATH_INPUT = s3://mrclassvitek/data/
BUCKET_PATH_OUTPUT =  s3://HuA05/output
BUCKET_PATH_JAR = s3://HuA05/
NAME_OF_CLASS = AirlineConnection
NAME_OF_JAR = AirlineConnection.jar
LOGS = s3://HuA05/log/

#-----------------------------------------------------------------------
#Building section
JCC = javac
JFLAGS = -g -classpath ./lib/opencsv-3.6.jar:`yarn classpath`:./target/
SRC = ./src/
TARGET = -d ./target/

Build:  AirlineConnection.class
	cd ./target && jar -cvf AirlineConnection.jar *.class
	cd ./target && jar -uvf AirlineConnection.jar -C ../lib/ com
	cd ./target && jar -uvf AirlineConnection.jar -C ../lib/ org
	cd ./target && rm -f *.class

AirlineParser.class:
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)AirlineParser.java

CustomWritable.class: $(SRC)CustomWritable.java
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

CustomWritableComparable.class: $(SRC)CustomWritableComparable.java
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

AirlineConnectionMapper.class: $(SRC)AirlineConnectionMapper.java CustomWritable.class AirlineParser.class
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

AirlineConnectionReducer.class: $(SRC)AirlineConnectionReducer.java CustomWritable.class CustomWritableComparable.class
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

AirlineConnection.class: $(SRC)AirlineConnection.java AirlineConnectionMapper.class AirlineConnectionReducer.class
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

#-----------------------------------------------------------------------
#Setting up the hadoop environment
StopH:
	stop-dfs.sh --config `pwd`/conf/
	stop-yarn.sh --config `pwd`/conf/
	mr-jobhistory-daemon.sh stop historyserver
	jps
StartH:
	start-dfs.sh --config `pwd`/conf/
	start-yarn.sh --config `pwd`/conf/
	mr-jobhistory-daemon.sh start historyserver
	jps
Prepare:
	hdfs dfs -rm -R -f hdfs://localhost/A05/
	hdfs dfs -mkdir hdfs://localhost/A05/
	hdfs dfs -mkdir hdfs://localhost/A05/data/
	hdfs dfs -copyFromLocal ../TestCases/A05/a5/*  hdfs://localhost/A05/data/
Format:
	hdfs namenode -format
#--------------------------------------------------------------------------
#Run the job on local hadoop
Run:
	hadoop dfs -rm -R -f hdfs://localhost/A05/output
	export R_HOME=a && hadoop --config ./conf jar ./target/AirlineConnection.jar AirlineConnection /A05/data/ /A05/output
	#hadoop --config ./conf jar ./target/FlightPrice.jar FlightPrice /A04/data/55.csv.gz /A04/output
	rm -Rf output
	hadoop dfs -copyToLocal hdfs://localhost/A05/output ./

#-------------------------------------------------------------------------
#Run job on remote aws cluster
Step:
	aws s3 cp `pwd`/target/$(NAME_OF_JAR) $(BUCKET_PATH_JAR)
	./Step.sh $(BUCKET_PATH_JAR)$(NAME_OF_JAR) $(BUCKET_PATH_INPUT) $(BUCKET_PATH_OUTPUT) $(NAME_OF_CLASS) $(LOGS)
	rm -Rf aws_output
	mkdir aws_output
	aws s3 cp $(BUCKET_PATH_OUTPUT)/ ./aws_output/ --recursive
	cd aws_output && cat `ls -t part*` > combined_result
	$(JCC) -d ./target/out/ ./src/out/FinalOutput.java 
	java -cp ./target/out/ FinalOutput ./aws_output/combined_result
