#!/bin/bash

BUCKET_PATH_JAR=$1
BUCKET_PATH_INPUT=$2
BUCKET_PATH_OUTPUT=$3
NAME_OF_JAR=$4
LOG_FILE=$5
#set -o verbose

CLUSTER_ID=$(aws emr create-cluster --release-label emr-4.4.0  --service-role EMR_DefaultRole --ec2-attributes InstanceProfile=EMR_EC2_DefaultRole --enable-debugging --log-uri $LOG_FILE --instance-groups InstanceGroupType=MASTER,InstanceCount=1,InstanceType=m3.xlarge InstanceGroupType=CORE,InstanceCount=10,InstanceType=m3.xlarge | ./cid.py)
echo "ClusterID: $CLUSTER_ID"
StepID=$(aws emr add-steps --cluster-id $CLUSTER_ID --steps Type=CUSTOM_JAR,Name=CostomJAR,ActionOnFailure=CONTINUE,Jar=$BUCKET_PATH_JAR,Args=$NAME_OF_JAR,$BUCKET_PATH_INPUT,$BUCKET_PATH_OUTPUT | ./stepid.py)
echo $StepID
State=$(aws emr list-steps --cluster-id $CLUSTER_ID --step-ids "$StepID" | ./step.py)
echo $State
while [ "$State" == "RUNNING" ] || [ "$State" == "PENDING" ]
do
        State=$(aws emr list-steps --cluster-id $CLUSTER_ID --step-ids "$StepID" | ./step.py)
	echo $State
        sleep 10
done
echo $State
#aws emr terminate-clusters --cluster-ids $CLUSTER_ID

