#!/bin/bash

CLUSTER_ID=$1
BUCKET_PATH_JAR=$2
BUCKET_PATH_INPUT=$3
BUCKET_PATH_OUTPUT=$4
N=$5

set -o verbose
StepID=$(aws emr add-steps --cluster-id $CLUSTER_ID --steps Type=Spark,Name="Spark",ActionOnFailure=CONTINUE,Args=[--deploy-mode,cluster,--class,CheapestAirline,$BUCKET_PATH_JAR,$BUCKET_PATH_INPUT,$BUCKET_PATH_OUTPUT,$N] | ./stepid.py)

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

