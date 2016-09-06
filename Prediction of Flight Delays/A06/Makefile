# fill in the appropriate bucked details. 
YourBucketName = delayprediction3
BUCKET_PATH_JAR = s3://$(YourBucketName)/FlightPrediction.jar
BUCKET_PATH_INPUT = s3://mrclassvitek/a6history/
BUCKET_PATH_OUTPUT = s3://$(YourBucketName)/output
CLUSTER_ID = j-211EROTDPLCYZ

# provide EC2 key name file below.
EC2KEYNAME = sujith

# provide the local file path for 98redacted.csv.gz ad 98validate.csv.gz
REDACTED_FILE_PATH = /home/naveenaswa/Desktop/History/TestFile/98redacted.csv.gz
VALIDATE_FILE_PATH = /home/naveenaswa/Desktop/History/Validate/98validate.csv.gz

LOCAL_NAME=A06

JCC = javac
JFLAGS = -g -classpath ./libs/*:`yarn classpath`:./target/
SRC = ./src/
TARGET = -d ./target/

# for pseudo mode 
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
	hdfs dfs -rm -R -f hdfs://localhost/$(LOCAL_NAME)/
	hdfs dfs -mkdir hdfs://localhost/$(LOCAL_NAME)/
	hdfs dfs -mkdir hdfs://localhost/$(LOCAL_NAME)/data/
	hdfs dfs -copyFromLocal ../TestCases/A06/a6history/105.csv.gz  hdfs://localhost/$(LOCAL_NAME)/data/
Format:
	hdfs namenode -format
Run:
	hadoop dfs -rm -R -f hdfs://localhost/$(LOCAL_NAME)/output
	export R_HOME=a && hadoop --config ./conf jar ./target/FlightPrediction.jar FlightPrediction /$(LOCAL_NAME)/data/ /$(LOCAL_NAME)/output
	#hadoop --config ./conf jar ./target/FlightPrice.jar FlightPrice /A04/data/55.csv.gz /A04/output
	rm -Rf output
	hadoop dfs -copyToLocal hdfs://localhost/$(LOCAL_NAME)/output ./


# for running on AWS - this will setup cluster with m3.xlarge of 5 nodes.
Cluster:
	
	aws s3 mb s3://$(YourBucketName)
	aws s3 cp ./src/emR_bootstrap.sh s3://$(YourBucketName)/A06_NR/
	aws s3 cp ./src/hdfs_permission.sh s3://$(YourBucketName)/A06_NR/ 
	aws emr create-cluster --release-label emr-4.3.0  --service-role EMR_DefaultRole --ec2-attributes '{"InstanceProfile":"EMR_EC2_DefaultRole","KeyName":"$(EC2KEYNAME)"}' \
	--instance-groups InstanceGroupType=MASTER,InstanceCount=1,InstanceType=m3.xlarge \
	 InstanceGroupType=CORE,InstanceCount=5,InstanceType=m3.xlarge \
	--bootstrap-actions Path=s3://$(YourBucketName)/A06_NR/emR_bootstrap.sh,Name=CustomAction,Args=[--rhdfs] \
	--steps Name=HDFS_tmp_permission,Jar=s3://elasticmapreduce/libs/script-runner/script-runner.jar,Args=s3://$(YourBucketName)/A06_NR/hdfs_permission.sh \
	--region us-east-1  --no-auto-terminate --name A06 \
	--log-uri 's3://$(YourBucketName)/elasticmapreduce/'

# step execution of mapreduce job on AWS
Step:
	aws s3 cp ./target/FlightPrediction.jar s3://$(YourBucketName)/
	cd ./bin && ./Step.sh $(CLUSTER_ID) $(BUCKET_PATH_JAR) $(BUCKET_PATH_INPUT) $(BUCKET_PATH_OUTPUT)
	rm -Rf aws_output
	aws s3 cp s3://$(YourBucketName)/output/ ./aws_output/ --recursive

# build the class files and jar to run on AWS.
Build:  FlightPrediction.class
	cd target && jar -cvf FlightPrediction.jar *.class
	cd target && jar -uvf FlightPrediction.jar -C ../libs/ com
	cd target && jar -uvf FlightPrediction.jar -C ../libs/ org

FlightPriceParser.class: $(SRC)FlightPriceParser.java
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

FlightPredictionMapper.class: $(SRC)FlightPredictionMapper.java FlightPriceParser.class
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

FlightPredictionReducer.class: $(SRC)FlightPredictionReducer.java
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

FlightPrediction.class: $(SRC)FlightPrediction.java FlightPredictionMapper.class FlightPredictionReducer.class
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)


# builds the class file for java program which divides the prediction dataset
divide:	DivideDataset.class
	mkdir -p ./DivideDataset
	mkdir -p ./Rouput
	cd ./target && java -cp ./:../libs/ DivideDataset $(REDACTED_FILE_PATH) ../DivideDataset

DivideFlightParser.class: $(SRC)DivideFlightParser.java
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

DivideDataset.class: $(SRC)DivideDataset.java DivideFlightParser.class
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)

# builds the class file for java program which computes the confusion matrix.
confusion: ConfusionMatrix.class
	cd ./target && java -cp ./:../libs/ ConfusionMatrix $(VALIDATE_FILE_PATH) ../result.csv.gz

ConfusionMatrix.class: $(SRC)ConfusionMatrix.java
	$(JCC) $(JFLAGS) $(TARGET) $(SRC)$(<F)
	
Predict:
	R -f predict.r 
	for i in `ls ./Routput`; do cat ./Routput/$$i >>result.csv; done;
	gzip result.csv

Clean:
	aws s3 rb s3://$(YourBucketName) --force
