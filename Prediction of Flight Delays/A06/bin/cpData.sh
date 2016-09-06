#!/bin/bash

CLUSTER_ID=$1
echo $CLUSTER_ID
ips=$(aws emr list-instances --instance-group-types "CORE" --cluster-id $CLUSTER_ID | ./bin/nodeips.py)
echo $ips
echo $2
rm -Rf models
mkdir -p models
for i in $ips
	do
		scp  -r -i $2 hadoop@$i:/home/hadoop/r_t/ ./models/
done
