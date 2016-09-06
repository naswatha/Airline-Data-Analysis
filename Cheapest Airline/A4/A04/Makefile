BUCKET_PATH_INPUT = s3://flightprice/A4/data/
BUCKET_PATH_OUTPUT =  s3://flightprice/A4/data/output/
BUCKET_PATH_JAR = s3://flightprice/A4/FlightPrice.jar
CLUSTER_ID = j-1SIFVWH2C8WKF
LOGS = s3://flightprice/A4/log/

StopH:
	stop-dfs.sh --config /home/radioer/A04/conf/
	stop-yarn.sh --config /home/radioer/A04/conf/
	mr-jobhistory-daemon.sh stop historyserver
	jps
StartH:
	start-dfs.sh --config /home/radioer/A04/conf/
	start-yarn.sh --config /home/radioer/A04/conf/
	mr-jobhistory-daemon.sh start historyserver
	jps
Prepare:
	hdfs dfs -rm -R -f hdfs://localhost/A04/
	hdfs dfs -mkdir hdfs://localhost/A04/
	hdfs dfs -mkdir hdfs://localhost/A04/data/
	hdfs dfs -copyFromLocal ~/Downloads/all/* hdfs://localhost/A04/data/
Format:
	hdfs namenode -format
Run:
	hadoop dfs -rm -R -f hdfs://localhost/A04/output
	hadoop --config ./conf jar FlightPrice.jar FlightPrice /A04/data/ /A04/output
	#hadoop --config ./conf jar FlightPrice.jar FlightPrice /A04/data/55.csv.gz /A04/output
	rm -Rf output
	hadoop dfs -copyToLocal hdfs://localhost/A04/output ./
Build:
	javac -classpath opencsv-3.6.jar ../A03/FlightPriceParser.java
	javac -classpath ../A03/:opencsv-3.6.jar:`yarn classpath` FlightPriceMaper.java
	javac -classpath ../A03/:opencsv-3.6.jar:`yarn classpath` FlightPriceReducer.java
	javac -classpath ../A03/:opencsv-3.6.jar:`yarn classpath`:./ FlightPrice.java
	#jar -cvfm FlightPrice.jar Mainfest.am FlightPrice*.class
	jar -cvf FlightPrice.jar FlightPrice*.class
	jar -uvf FlightPrice.jar -C lib/ .
	jar -uvf FlightPrice.jar -C ../A03 FlightPriceParser.class
.PHONY: Test
Test:
	cd output_M && cat `ls -t part*` > combined_result
	javac FlightPriceProcess.java
	java -cp ./ FlightPriceProcess ./output_M/combined_result 1 > N1
	java -cp ./ FlightPriceProcess ./output_M/combined_result 200 > N200
	aws s3 cp N1 s3://flightprice/A4/
	aws s3 cp N200 s3://flightprice/A4/	
CalTest:
	cd Test && javac CalTest.java && java -cp ./ CalTest
WeekMedian:
	javac -classpath opencsv-3.6.jar ../A03/FlightPriceParser.java
	javac -classpath ../A03/:`yarn classpath` MedianMaper.java
	javac -classpath ../A03/:`yarn classpath` MedianReducer.java
	javac -classpath ../A03/:`yarn classpath`:./ Median.java
	jar -cvf Median.jar FlightPrice*.class
	jar -uvf Median.jar -C lib/ .
	jar -uvf Median.jar -C ../A03 FlightPriceParser.class

WeekMedianRun:
	hadoop dfs -rm -R -f hdfs://localhost/A04/output_1
	hadoop --config ./conf jar Median.jar Median /A04/data/ /A04/output_1
	#hadoop --config ./conf jar FlightPrice.jar FlightPrice /A04/data/55.csv.gz /A04/output
	rm -Rf output
	hadoop dfs -copyToLocal hdfs://localhost/A04/output1 ./

Cluster:
	aws emr create-cluster --release-label emr-4.3.0  --service-role EMR_DefaultRole --ec2-attributes InstanceProfile=EMR_EC2_DefaultRole --instance-groups InstanceGroupType=MASTER,InstanceCount=1,InstanceType=m3.xlarge InstanceGroupType=CORE,InstanceCount=4,InstanceType=m3.xlarge
Step:
	aws emr add-steps --cluster-id $(CLUSTER_ID) --steps Type=CUSTOM_JAR,Name=CostomJAR,ActionOnFailure=CONTINUE,Jar=$(BUCKET_PATH_JAR),Args=FlightPrice,$(BUCKET_PATH_INPUT),$(BUCKET_PATH_OUTPUT)
	rm -R -f output
	mkdir output
	aws s3 cp $BUCKET_PATH_OUTPUT ./output --recursive
	
