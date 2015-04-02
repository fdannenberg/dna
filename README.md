# dna

This software supports the forthcoming publications

[1] Guiding the folding pathway of DNA origami. Katherine E. Dunn*, Frits Dannenberg*, Thomas E. Ouldridge, Marta Kwiatkowska, Andrew J. Turberfield and Jonathan Bath. * equal contribution

[2] Modelling DNA Origami Self-Assembly at the Domain Level. Frits Dannenberg, Thomas E. Ouldridge, Katherine Dunn, Jonathan Bath, Marta Kwiatkowska, and Andrew J. Turberfield.

and was used to generate all model results in [1,2]. A selection of pregenerated results in [1] are included. By releasing this code, other researchers can independently verify results. The model implemented in this software is described both in [1,2].

This software has dependencies on some common libraries, see lib/dependencies.txt for details. To run the software see resource/compile.txt.

email: fdannenberg/live/nl


.
├── bin
├── lib
│   └── dependencies.txt
├── output
├── output-pregenerated
│   ├── alternative_seam.txt
│   ├── enlongated_u_staples.txt
│   ├── full_design.txt
│   ├── half_seam.txt
│   ├── lower_right.txt
│   ├── overview.txt
├── README.TXT
├── resource
│   ├── compile.txt
│   ├── compile.txt~
│   ├── design23e.pdf
│   └── shapes.png
├── RGraphs
│   ├── dna-correlation.R
│   ├── dna-graph.R
│   ├── svg
│   │   ├── foot0.txt
│   │   ├── foot1.txt
│   │   ├── foot2.txt
│   │   ├── foot3.txt
│   │   ├── header0.txt
│   │   ├── header1.txt
│   │   ├── header2.txt
│   │   ├── header3.txt
│   │   ├── header4.txt
│   │   ├── header5.txt
│   │   ├── middle0.txt
│   │   ├── middle1.txt
│   │   ├── middle2.txt
│   │   ├── middle3.txt
│   │   ├── middle4.txt
│   │   └── middle5.txt
│   ├── svg-contactMap
│   │   ├── contactmap.svg
│   │   ├── foot0.txt
│   │   ├── foot1.txt
│   │   ├── foot2.txt
│   │   ├── foot3.txt
│   │   ├── header0.txt
│   │   ├── header1.txt
│   │   ├── header2.txt
│   │   ├── header3.txt
│   │   ├── header4.txt
│   │   ├── header5.txt
│   │   ├── middle0.txt
│   │   ├── middle1.txt
│   │   ├── middle2.txt
│   │   ├── middle3.txt
│   │   ├── middle4.txt
│   │   └── middle5.txt
│   ├── svg-corr
│   │   ├── foot0.txt
│   │   ├── foot1.txt
│   │   ├── foot2.txt
│   │   ├── foot3.txt
│   │   ├── header0.txt
│   │   ├── header1.txt
│   │   ├── header2.txt
│   │   ├── header3.txt
│   │   ├── header4.txt
│   │   ├── header5.txt
│   │   ├── middle0.txt
│   │   ├── middle1.txt
│   │   ├── middle2.txt
│   │   ├── middle3.txt
│   │   ├── middle4.txt
│   │   ├── middle5.txt
│   │   └── prime.svg
│   └── svg-domains
│       ├── bindStatistic_new2.svg
│       ├── foot0.txt
│       ├── foot1.txt
│       ├── foot2.txt
│       ├── foot3.txt
│       ├── header0.txt
│       ├── header1.txt
│       ├── header2.txt
│       ├── header3.txt
│       ├── header4.txt
│       ├── header5.txt
│       ├── middle0.txt
│       ├── middle1.txt
│       ├── middle2.txt
│       ├── middle3.txt
│       ├── middle4.txt
│       └── middle5.txt
└── src
    ├── examples
    │   ├── DistanceCalcEx.java
    │   ├── LoopCalcEx.java
    │   ├── LoopEx.java
    │   └── RateCalcEx.java
    ├── folding
    │   ├── Constant.java
    │   ├── Control.java
    │   ├── Design.java
    │   ├── DesignLoader.java
    │   ├── EnergyCalc.java
    │   ├── Energy.java
    │   ├── FreeEnergy.java
    │   ├── Legal.java
    │   ├── LegalState.java
    │   ├── LegalType.java
    │   ├── LoopCalc.java
    │   ├── Loop.java
    │   ├── Loops.java
    │   ├── Model.java
    │   ├── NextStateInterface.java
    │   ├── NextState.java
    │   ├── PathMetrics.java
    │   ├── RatesCalc.java
    │   ├── RatesGenerator.java
    │   ├── RatesInterface.java
    │   ├── Rates.java
    │   ├── Rigidity.java
    │   ├── Simulator.java
    │   ├── SimWriter.java
    │   ├── StateGraph.java
    │   ├── State.java
    │   ├── StateLoop.java
    │   ├── StateSub.java
    │   ├── Time.java
    │   ├── Transition.java
    │   └── Util.java
    ├── plotting
    │   ├── ColourMap.java
    │   ├── Constants.java
    │   ├── ContactMap.java
    │   ├── Foot.java
    │   ├── Header.java
    │   ├── Heatmap.java
    │   ├── Middle.java
    │   ├── PlottingData.java
    │   ├── SeamCorrelation.java
    │   ├── SvgCreator.java
    │   ├── SvgObject.java
    │   └── SvgSettings.java
    ├── tests
    │   └── LoopTest.java
    └── writers
        ├── Colour.java
        ├── CommandlineCaller.java
        ├── ContactOrder.java
        ├── History.java
        ├── InfoPrinter.java
        ├── JsonParser.java
        ├── ListWriter.java
        ├── PerStepStatistic.java
        ├── RCommand.java
        ├── Result.java
        ├── ResultWriter.java
        └── ScatterPlot.java

