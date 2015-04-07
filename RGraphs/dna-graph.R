# Run this file from commandline using Rscript dna-graph.R
# Run this file from R commandline using source('Graph.R')
# If crash, use setwd(initial.dir), then source('Graph.R') to reset the directory and try again.
#rm(list=ls())
require(splines)


## READ COMMANDLINE
args <- commandArgs(trailingOnly = TRUE)

print(args)

pad			<- args[3];
upTo 			<- as.numeric(args[4]);
design  		<- args[5];
folder  		<- args[6];
dimerOption	 	<- as.logical(args[7]);
tempStart		<- as.numeric(args[8]) - 273.15;
tempEnd 		<- as.numeric(args[9]) - 273.15;
rate			<- as.numeric(args[10]);
alsoMelt		<- as.logical(args[11]);

print(c("AlsoMelt is ", alsoMelt))

splineFactor 	<- 0.35;		#spline factor determines wiggle

xleft 			<- (tempStart-85.0)/rate;
xright			<- (tempStart-50.0)/rate;
xleft2 			<- (tempStart-75.0)/rate;
xright2			<- (tempStart-55.0)/rate;

print(c("xLeft is ",xleft));
print(c("xRight is ",xright));
print(c("xLeft2 is ",xleft2));
print(c("xRight2 is ",xright2));

#save <- xleft		// REVERTS AXIS.
#xleft <- xright
#xright <- save

standardMain <- "Staple probability vs time - ";
xLabel 	     <- "Temperature (C)";


##  COLORS FOR OVERAL PLOTS

	colAnnealing 		<- "blue";
	colAnnealingDiff	<- rgb(0,0,1,0.75);
	colMelting			<- "red";
	colMeltingDiff		<- rgb(1,0,0,0.75);

	colXAnnealing		<- rgb(0,0,0,0.75);
	colXAnnealingDiff	<- "lightblue2";
	colXMelting			<- rgb(0,0,0,0.35);
	colXMeltingDiff		<- "rosybrown2"; 



setwd(pad)


	fluorSet = as.integer(as.vector(read.table("fluor/plot.txt")$V1))
	xSet = as.integer(as.vector(read.table("fluor/xStaples.txt")$V1))
	
	print(c("xSet is ", xSet))

	time = as.numeric(as.vector(read.table("staple0.txt")[,1])) * (1.0/60.0)
	length = length(time)
	temp = matrix(0,1,length(time));
	temp = tempStart - (time)*rate;



	data = matrix(0,length(time),upTo)
	legends <- c()

	for (i in 0:(upTo-1)){

		textname <- paste("staple", i, ".txt",sep="");
		data[,(i+1)] =  as.numeric(read.table(textname)[,2]);
		textname <- paste("staple", i,sep="");
		legends = c(legends,textname);

	 }


	 stapleData     = as.numeric(as.vector(read.table("staple.txt")$V2));
	 stapleData2 	= as.numeric(as.vector(read.table("staple.txt")$V2));
     quenchData 	= as.numeric(as.vector(read.table("quench.txt")$V1));
     quenchData2 	= as.numeric(as.vector(read.table("quench.txt")$V1));
	

#### PRE-PROCESSING FOR ALSOMELT		-- basically redo a bunch of things, could make functions instead
	if(alsoMelt){

		newLength <- floor(length(time)*0.5)
		time <- time[1:newLength];
		
		data <- matrix(0, newLength, upTo)
		data2 <- matrix(0, newLength, upTo)

		for (i in 0:(upTo-1)){

			textname <- paste("staple", i, ".txt",sep="")
			data[,(i+1)] =   as.numeric(as.vector(read.table(textname)[1:newLength,2]))
			data2[,(i+1)] =  as.numeric(as.vector(read.table(textname)[newLength+1 + newLength:1,2]))

		}


		stapleData2 = stapleData[newLength+1+ newLength:1];
		stapleData  = stapleData[1:newLength];

		quenchData2 = quenchData[newLength+1+newLength:1];
		quenchData  = quenchData[1:newLength];

		# Calculate the histeresis, save to file.
		stapleDataDiff = abs(stapleData-stapleData2)
		write(sum(stapleDataDiff)/60, file=paste(pad,"/hysteresis.txt", sep=""))
				
	}	

	colors <- scan("colour.txt", what="");
	timeLim <-time[seq(0*60+1,length(time),5*60)] 
	tempLim <-temp[seq(0*60+1,length(time),5*60)]
	temp = matrix(0,1,length(time));
	temp = tempStart - (time)*rate;


	print("Length is ")
	print(length(time))

	print("timeLim is ")
	print(timeLim)


#### FUNCTIONS


	setGrid <- 	function(){
	
			abline(v=(timeLim), 		col=rgb(0,0,0,0.45), lty="dotted")
			abline(h=(seq(0,1,0.2)), 	col=rgb(0,0,0,0.45), lty="dotted")
	
	}
	
	

	startPlot <- function(name){

		print( c("Starting plot: ", name))
		pdf(file=paste(pad,"/plots/",name,".pdf",sep=""),width=12,height=6)

	}

	plotLines <- function(input, color, diff){
		
		if(diff){
		
			sm <- smooth.spline(head(time,-1), diff(data), spar=splineFactor)
			lines(sm$x, abs(100*sm$y),col=color,lwd=3);
		
		} else {
		
			lines(time, data, col=color,lwd=3);
			
		}
	
	}
	
	getData <- function(type){
	
			return(data[,type])

	}
	
	getData2 <- function(type){
	
			return(data2[,type])

	}

	getTime <- function(){
	
		return(time)
			
	}

	pickColour <- function(i){
		
		return(colors[i]);

	}

	genAxisX <- function(){

	axis(1,at=timeLim, labels= signif(tempLim, digits = 4))
	axis(2)


	}

	plotOveralLines <- function(input,input2) {
	
		if(alsoMelt){			
			
			for (i in 1:length(xSet)){
						
				sm <- smooth.spline(time, getData(xSet[i]+1), spar=splineFactor)
				lines(sm$x, sm$y,col=colXAnnealing,lwd=3);
				
				sm <- smooth.spline(time, getData2(xSet[i]+1), spar=splineFactor)
				lines(sm$x, sm$y,col=colXMelting,lwd=3);
				
			}
			
			lines(time, input2, col=c(colMelting),lwd=3);
			
		}	

		if(alsoMelt){

			legend("topleft", cex=1.25, pch=16, col=c(colAnnealing,colXAnnealing,colMelting,colXMelting), legend=c("Annealing","Annealing - staple","Melting","Melting - staple"), ncol=1, bg="white")

		} else {
		
			legend("topleft", cex=1.25, pch=16, col=c(colAnnealing,colXAnnealing,colMelting,colXMelting), legend=c("Annealing","Annealing - staple","Melting","Melting - staple"), ncol=1)
		
		}

	}


## END FUNCTIONS



## PLOTTING FUNCTION

	individualPlot <- function(prefix){

##Plot 1A   -- individual staple likilhood
startPlot(paste(prefix,"staple"))

	plot(		getTime(),
			getData(1),
			type="l",
			col=colors[1], 
			main=paste("Staple likelihood vs temp", "  " , design ,sep=""), 
			axes=FALSE,
			xlab=xLabel,
			ylim=c(0, max(data)), 
			ylab="Probability" #,lwd=3
		)
		
	setGrid()

	for (i in 1:(upTo)){

		lines(getTime(),getData(i),col=pickColour(i)#,lwd=3 
		)

 	}
	
	genAxisX()

	legend("topleft", cex=0.75, pch=16, col=colors, legend=legends, ncol=2)

dev.off()
	



##Plot 1B   -- individual staple likilhood
startPlot(paste(prefix,"stapleLegend"))

	plot(		getTime(),
			getData(1),
			type="l",
			col=colors[1], 
			main=paste("Staple likelihood vs temp", "  " , design ,sep=""), 
			axes=FALSE,
			xlab=xLabel,
			ylim=c(0,max(data)), 
			ylab="Probability" #,lwd=3
		)
		
	setGrid()

	for (i in 1:(upTo)){

		lines(getTime(),getData(i),col=pickColour(i) ) #,lwd=3 )

 	}
	
	genAxisX()

	#legend("topleft", cex=0.75, pch=16, col=colors, legend=legends, ncol=2)

dev.off()




##Plot 1D   -- individual staple likilhood Smoothed
startPlot(paste(prefix,"stapleSmooth3"))


	plot(	getTime(),
			getData(1),
			type="l",
			col="white", 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, 1), 
			xlim=c(xleft2,xright2), 
			ylab="Probability"
		)
		
	setGrid()

	for (i in 1:(upTo)){
	
		lines(smooth.spline(getTime(), getData(i), spar=splineFactor), col=pickColour(i),lwd=3);
		
	}
	
	genAxisX()


dev.off()


##Plot 1c   -- individual staple likilhood Smoothed
startPlot(paste(prefix,"stapleSmooth2"))


	plot(	getTime(),
			getData(1),
			type="l",
			col="white", 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, max(data)), 
			ylab="Probability"
		)
		
	setGrid()

	for (i in 1:(upTo)){
	
		lines(smooth.spline(getTime(), getData(i), spar=splineFactor), col=pickColour(i),lwd=3);
		
	}
	
	genAxisX()


dev.off()



##Plot 1D   -- individual staple likilhood Smoothed
startPlot(paste(prefix,"stapleSmooth"))


	plot(	getTime(),
			getData(1),
			type="l",
			col="white", 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, max(data)), 
			xlim=c(xleft,xright), 
			ylab="Probability"
		)
		
	setGrid()

	for (i in 1:(upTo)){
	
		lines(smooth.spline(getTime(), getData(i), spar=splineFactor), col=pickColour(i),lwd=3);
		
	}
	
	genAxisX()


dev.off()




##Plot 1E   -- individual staple likilhood Smoothed for fluorescent staples
startPlot(paste("",prefix,"stapleSmoothFluor"))


	plot(	getTime(),
			getData(1),
			type="l",
			col="white", 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, max(data)), 
			xlim=c(xleft,xright), 
			ylab="Probability"
		)
		
	setGrid()

	for (i in 1:length(fluorSet)){
	
		lines(smooth.spline(getTime(),getData(fluorSet[i]+1), 
		spar=splineFactor), col=pickColour(fluorSet[i]+1),lwd=3);
		
	}
	
	genAxisX()

dev.off()


##Plot 1E   -- individual staple likilhood Smoothed for fluorescent staples
startPlot(paste("",prefix,"stapleSmoothFluorDiff"))


	plot(	getTime(),
			getData(1),
			type="l",
			col="white", 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, max(data)), 
			xlim=c(xleft,xright), 
			ylab="Probability"
		)
		
	setGrid()

	for (i in 1:length(fluorSet)){
	
		sm <- smooth.spline(head(time,-1), diff(getData(fluorSet[i]+1)), spar=splineFactor)
		lines(sm$x, abs(100*sm$y), col=pickColour(fluorSet[i]+1),lwd=3);

		##lines(smooth.spline(getTime(),getData(fluorSet[i]+1), spar=splineFactor), col=pickColour(fluorSet[i]+1),lwd=3);
		
	}
	
	genAxisX()

dev.off()


			### LIMIT THIS
	}



	overalPlot <- function(){

##Plot 2A  -- the overal likelihood, folded plot
startPlot("stapleTot");


	plot(	getTime(),
			stapleData,
			type="l",
			col=colAnnealing, 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, max(as.numeric(data))), 
			ylab="Probability",
			lwd=3
		)
		
	setGrid()
	

	genAxisX()
	plotOveralLines(stapleData, stapleData2)


dev.off()




##Plot 2B  -- the overal likelihood, folded plot
startPlot("stapleTotZoom");

	plot(	getTime(),
			stapleData,
			type="l",
			col=colAnnealing, 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0, max(stapleData)), 
			xlim=c(xleft,xright), 
			ylab="Probability",
			lwd=3	
		)
	setGrid()

	genAxisX()
	plotOveralLines(stapleData, stapleData2)
	

dev.off()

##Plot 2C  -- the overal likelihood, zoom2 plot
startPlot("stapleTotZoom2");



	plot(	getTime(),
			stapleData,
			type="l",
			col=colAnnealing, 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0,  max(stapleData)), 
			xlim=c(xleft2,xright2), 
			ylab="Probability",
			lwd=3	
		)
	setGrid()

	genAxisX()
	plotOveralLines(stapleData, stapleData2)

	

dev.off()

}


quenchPlot <- function(){

	startPlot("quenchPlot");

	plot(		getTime(),
			quenchData,
			type="l",
			col="1", 
			main=paste(standardMain, design ,sep=""), 
			axes=FALSE,
			xlab=xLabel, 
			ylim=c(0,  max(stapleData)), 
			ylab="Probability",
			lwd=3
		)
	setGrid()

	genAxisX()

	lines(time, stapleData2, col=c("darkgray"),lwd=3);
	plotOveralLines(quenchData, quenchData2)
	


	dev.off()

}

## END PLOTTING FUNCTION



## SCRIPT STARTS HERE


	
	individualPlot("A-");
	overalPlot();


	if(alsoMelt){
	
		data <- data2;
		individualPlot("M-");
		#quenchPlot();

	}




rm(list=ls())
	





