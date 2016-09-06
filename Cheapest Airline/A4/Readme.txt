
Steps to run the job on AWS

-----------
Note: 
1) Before running the script make sure all the bucket path are set to correct s3 folders
2) There are two makefile in A04 and A4WeeklyData, make sure you have correct input of
	s3 folder in both files.
3) The makefile is explanatory, and source files are provided in case you want to build 
	the jar.
4) Make sure cluster-id is correctly provided.
5) Feel free to make any changes in make file to run the MR jobs.


-----------

1) Run the bash script(./test) in the root folder. 
	-- this should run the initial MapReduce to find the carrier at N=1 and N=200.
	-- The output file is taken as input to another MapReduce job for N=1 and N=200
	

2) R script is provided with the assumption that output file part-r-00000 from N=1 and 
	N = 200 are copied to A4WeeklyData folder.

3) Please find the report and rscript to generate graph are available in root folder of
	the project.

4) Please wait for AWS finished the job and copy the result to A04/output then

5) If you find any further difficulties in running build contact us on email.

aswathanarayana.n@husky.neu.edu
hu.ru@husky.neu.edu

