build:
	cd st/;javac -classpath opencsv-3.6.jar:../ ChpPrice.java
profiling:
	pwd
	sh ./benchmark
Running:
	cp ./st/ChpPrice.class ./
	pwd
	java  -cp ./st/opencsv-3.6.jar:./:./st/:./st/ChpPrice.class ChpPrice ~/Downlos/323.csv.gz 4 1 5
