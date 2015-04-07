# Run this file from commandline using Rscript dna-graph.R
# Run this file from R commandline using source('Graph.R')
# If crash, use setwd(initial.dir), then source('Graph.R') to reset the directory and try again.
#rm(list=ls())
require(splines)


## READ COMMANDLINE
args <- commandArgs(trailingOnly = TRUE)

print(args)

pad				<- args[3];
upTo 			<- as.numeric(args[4]);
dimerOption	 	<- as.logical(args[7]);


setwd(pad)

no_col <- max(count.fields("correlation.txt", sep = ","))
data <- read.table("correlation.txt",sep=",",fill=TRUE,col.names=1:no_col, )
data





