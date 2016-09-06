#!/bin/bash

CLUSTER_ID=$1
BUCKET_PATH_JAR=$2
BUCKET_PATH_INPUT=$3
BUCKET_PATH_OUTPUT=$4
set -o verbose
StepID=$(aws emr add-steps --cluster-id $CLUSTER_ID --steps Type=CUSTOM_JAR,Name=CostomJAR,ActionOnFailure=CONTINUE,Jar=$BUCKET_PATH_JAR,Args=FlightPrediction,$BUCKET_PATH_INPUT,$BUCKET_PATH_OUTPUT | ./stepid.py)
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

