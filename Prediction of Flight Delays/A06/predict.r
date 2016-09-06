library(randomForest)
inputFolder <- './DivideDataset/'
modelFolder <- './models/r_t/'
outputFolder <- './Routput/'
printResult <- function(x){
  date <- paste(x[4], x[5], x[6], sep="-")
  label <- paste(x[10], date, x[8], sep="_")
  label <- paste(label, x[11], sep=',')
  print(label)
}
models = list.files(modelFolder, full.names = TRUE)
for (model in models){
  load(model)	
}
inputs = list.files(inputFolder)
for (input in inputs){
  filePath = paste(inputFolder, input, sep="")
  subset = read.csv(file=filePath, header=FALSE, colClasses = c(NA, NA, NA, "integer", "integer", "integer", "integer", "integer", "integer", NA))
  colnames(subset) <- c("carrier", "origin", "destination", "year", "month", "dayOfMonth", "crsArrTime", "crsDepTime", "crsElapsedTime", "flightNumber")
  subset[, "delay"] <- c(rep(FALSE, length(subset$month)))
  chooseCols <- c("carrier", "origin", "destination", "year", "month", "dayOfMonth", "crsArrTime", "crsDepTime", "crsElapsedTime", "delay")
  name = paste(subset$origin[1], subset$month[1], sep="")
  nameT = paste(name, "_t", sep="")
  assign("nameTD",get(nameT))
  subset$destination <- factor(subset$destination, levels = levels(nameTD$destination))
  subset$carrier <- factor(subset$carrier, levels = levels(nameTD$carrier))
  assign("tempModel",get(name))
  name = paste(name, 'r', sep="")
  assign(name, predict(tempModel, rbind(subset[,chooseCols], nameTD))[1:length(subset$month)] > 0.5)
  subset$delay <- get(name)
  rr <- apply(subset, 1, printResult)
  filename = paste(outputFolder, name, '.csv', sep="")
  write.csv(rr, file=filename)
}
