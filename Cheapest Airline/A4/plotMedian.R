df <- read.table(file="part-r-00000")
plot(df$V1,df$V2, type="l",ylab="Price", xlab="YEAR-WEEK",main="Weekly Median Price",las=1)
