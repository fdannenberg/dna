# dna

	╗╦
	╫▓ü
	╚╬½               ..╓╓╓╓
	▒▒╫╬╦    ,╓╥╦╦▒▒▒▒▒╬╬▒╢╢╢▒
	▒▒╢╬╬╬▓╬╬╬╬▓╬å╜╙^╠╬╜  ╠╬╢╬╬╖
	╬▒▒▒╨╙^^╬╬å^   ╓▓╝   ╦╢^╙╢╢▓╕
	å▒▒╢▒╦▓å^    ,╬å^   ╬╝   ╬╬╢▓,
	 ╫▒▒╬╬      ╦╬╜   ╓╬╝   ▓▓╬╬╬▓
	  ╬▒▒╬╦   ╦╬╨    ╦╬²  ╒▓▌ ╘▒╢╬╖
	   å▒▒╬½╦╬╜    ,╬╝   ╓▓▀  ╒╬╢╬▒      ,,╓╓╓,,
	    ╙╬▒╢╬╦,   ╓╬╜   ╦▓*  ╓╬╫╬▒▒▓╬╢╬╬╬╬╬╬╬╬╬╬╬╬╬▒╦,
	      ╙╬▒╢╢╬▒▓╬╬╦╦╦╬╬╬▓▓╬▒╝▒╬╢▒╢^^`  ╢å   ,╬╝╙╩╬╬╬▓╦
	        `╙╙╚╝╢╝▒▒▒▒▒╝╝╜╜^^  å╬▒╠    ╬å   ╓╬╜   ╫╫╬╬╬▓,
	                            ╚╬▒▒∩  ╬╝   ╦╬`  ╓╬╨  å╬╬╫▄
	                            )▓▒▒▒╓▓╝   ╬╝   ╪╬^    ╠╫╬╬▌
	                             ▓▒▒╠╬╜  ╓╬╜  ╔╬╜    ╦▓å╜╫▒╫▌
	                             ╙▓▒╠▒  ╪▒` ,╬╝`  ╓φ╝▀   ╠╬╢╬╦╦╦╥╥╖╓,
	                              å▒▒▒╦╬╨  ╦╬^ ,╦╬▓▒▒╬╢╢▓╢╬╬╬▒╫╬╬╬╬╬╬╬╬╬▓╗
	                               å▒▒▒å╦╗╬▒≡▒╬╬╬å╬╝╚╜^^^^╙╬╢╠▒      7╬▀å╬
	                                `╙╝▒▒▒╚╢╝╝╜^^          ╬╬▒▒      ╬╝
	                                                       ╚╬▒▒⌐    ╬╬
	                                                        ╬▌▒▒   ╔╬` 

This software supports the following publications

- [1] Guiding the folding pathway of DNA origami. Katherine E. Dunn †, **Frits Dannenberg †**, Thomas E. Ouldridge, Marta Kwiatkowska, Andrew J. Turberfield and Jonathan Bath. † equal contribution. *Nature*, 2015.
[Link to Publisher](http://www.nature.com/nature/journal/v525/n7567/full/nature14860.html)

- [2] Modelling DNA Origami Self-Assembly at the Domain Level. **Frits Dannenberg**, Thomas E. Ouldridge, Katherine Dunn, Jonathan Bath, Marta Kwiatkowska, and Andrew J. Turberfield. *The Journal of Chemical Physics*, 2015.
[Link to Publisher](http://scitation.aip.org/content/aip/journal/jcp/143/16/10.1063/1.4933426)

and was used to generate all model results in [1,2]. A selection of results supporting [1] are included. By releasing this code, other researchers can independently verify results. The model implemented in this software is described both in [1,2].

This software has dependencies on some common libraries, which need to be installed first. Instructions for installation and running are found below.

>email: fdannenberg/live/nl



## INSTALLATION

Unzip the folder. The software depends on the following libraries that you should install. Dir lib/ should contain

- commons-math3-3.0.jar or equivalent	http://commons.apache.org/
- jgrapht-jdk1.6.jar or equivalent	http://jgrapht.org/ do not confuse with jgraph
- junit-4.12.jar or equivalent		http://junit.org/ 

In addition, the plotting scripts require R to run. http://www.r-project.org/



###  COMPILE 

```
javac  -cp lib/jgrapht-jdk1.6.jar:lib/commons-math3-3.0.jar src/folding/*.java src/writers/*.java src/plotting/*.java -d bin; 
```


## SETTINGS 

```
THREADS=8 PATHS=20 RATE=0.4; DESIGN0="design23eAll"  DESIGN1="design23eAllEnergy" DESIGN2="methodsPaperAllPlots" DESIGN3="methodsPaperNoCooperativity";

THREADS 	# number of simulation threads
PATHS		# number of folding pathways simulated per thread
RATE 		# temp drop in celsius per minute (rate)

DESIGN0="design23eAll"			# runs 5 different designs
DESIGN1="design23eAllEnergy" 		# runs same designs with sequence-specific domain stability
DESIGN2="methodsPaperAllPlots"  	# methods paper plots and data
DESIGN3="methodsPaperNoCooperativity" 	# special SI table in the methods paper
```


##	RUN

```
java -cp bin:lib/jgrapht-jdk1.6.jar:lib/commons-math3-3.0.jar -Xmx4000m folding.Control $DESIGN0 "distance" $THREADS $PATHS $RATE;
```
Correct calls will output to the commandline something similar to the below, at which point the simulation starts to run. After the run, output is placed in the /output/ dir.

```
design23eAll
distance
8
20
0.4
/mnt/ECFCC295FCC25A0A/workspace/dna-release/output/
The model used here is distance model
Gamma is 1.5
Prefactor is 6.69546718045E-19
numOfSteps is 3900
Number of legal states is 74
neighbourCorrectionDH - neighbourCorrectionDS -0.0 - -0.0
```


## PRE-GENERATED RESULTS

output-pregenerated/overview.txt contains a summary of output for the experiments described in [1]. 




