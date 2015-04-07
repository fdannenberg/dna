/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package writers;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import plotting.ContactMap;
import plotting.SvgSettings;
import plotting.Heatmap;
import plotting.SeamCorrelation;
import folding.Constant;
import folding.Design;
import folding.Legal;
import folding.LegalState;
import folding.LegalType;
import folding.State;
import folding.Time;
import folding.Util;

// given Result, this class writes its results

public class ResultWriter
{
	private Design design;
	private Result result;
	private String outDir;
	private int numOfPaths;

	private ScatterPlot scatter;
	private Heatmap heatmap;
	private SeamCorrelation seamCorrelation;
	private ContactMap contactMap;

	private JsonParser jsonParser;
	private Legal legal;
	private String exportFilePath;
	private double[][] stapleProbability;

	private ListWriter writerFluor;
	private ListWriter writerStaples;
	private ListWriter writerFinalStates;
	private ListWriter writerLegalStates;
	private ListWriter writerMetrics;
	private ListWriter writerColour;

	public ResultWriter(Result result2, Design design2, int numOfPaths2)
	{
		design = design2;
		result = result2;
		outDir = design.outDir;
		numOfPaths = numOfPaths2;
		scatter = new ScatterPlot(design, outDir);
		heatmap = new Heatmap(design, result, scatter);
		seamCorrelation = new SeamCorrelation(design, result);
		contactMap = new ContactMap(design, result);

		jsonParser = new JsonParser(design);
		legal = new Legal(design);
		this.initialise(); // init twice.. (hack to make folders)
		this.initialise();

	}

	public void write()
	{
		this.writeStaples();
		this.writeRunStatistic();
		this.writeFinalStates();
		this.writeLegalStates();
		scatter.genScatterPlot(stapleProbability);
		this.writeHeatMaps();
		this.writeSeamCorrelation();
		this.writeContactMaps();
		this.writeColours();
		this.writePlottingStaples();
		this.writeMetrics();
	}

	private void writeContactMaps()
	{
		if (design.isDimer()) {

			for (double i = 56.0; i < 66.0; i = i + 0.25) {

				contactMap.write(i);

			}
		}
	}

	private void writeSeamCorrelation()
	{
		if (design.isDimer()) {

			for (double i = 56.0; i < 66.0; i = i + 0.25) {

				seamCorrelation.write(i);

			}

			seamCorrelation.write(50.0);
			seamCorrelation.write(40.0);
			seamCorrelation.write(30.0);
			seamCorrelation.write(21.0);

			seamCorrelation.write(scatter.seamTemp);
			seamCorrelation.write(scatter.bodyTemp);
			seamCorrelation.write(64.21);

			seamCorrelation.close();

		}

	}

	private void writeHeatMaps()
	{
		for (int i = 0; i < LegalType.legalTypes.length; i++) {

			SvgSettings staples = new SvgSettings("staples", LegalType.legalTypes[i]);
			heatmap.plot(staples); // old heatmap

			SvgSettings domains = new SvgSettings("domains", LegalType.legalTypes[i]);
			heatmap.plot(domains); // new heatmap
		}
	}

	private void writeMetrics()
	{
		DescriptiveStatistics annealTemp = result.statisticsAnnealTemp();
		writerMetrics.writeToFile("AnnealTemp:\n" + Util.limit(annealTemp.getMean(), 100000) + " C", 1);
		writerMetrics.writeToFile("S.D. annealTemp: " + Util.limit(annealTemp.getStandardDeviation(), 100000) + " C\n", 1);

		DescriptiveStatistics meltTemp = result.statisticsMeltTemp();
		writerMetrics.writeToFile("MeltTemp:\n" + Util.limit(meltTemp.getMean(), 100000) + " C", 1);
		writerMetrics.writeToFile("S.D. meltTemp: " + Util.limit(meltTemp.getStandardDeviation(), 100000) + " C\n", 1);

		DescriptiveStatistics diffTemp = result.statisticsDiffTemp();
		writerMetrics.writeToFile("DiffTemp:\n" + Util.limit(diffTemp.getMean(), 100000) + " C", 1);
		writerMetrics.writeToFile("S.D. diffTemp: " + Util.limit(diffTemp.getStandardDeviation(), 100000) + " C\n", 1);

		DescriptiveStatistics foldingWidth = result.statisticsFoldingWidth();
		String output = "Average folding width between " + Constant.width_lower + " and " + Constant.width_upper + "\n"
				+ Util.limit(foldingWidth.getMean(), 100000);
		output += "\n";
		output += "S.D. folding width: " + Util.limit(foldingWidth.getStandardDeviation(), 100000) + "\n";
		writerMetrics.writeToFile(output, 1);
	}

	private void writePlottingStaples()
	{
		for (int i = 0; i < design.getFluorStaples().length; i++) {

			writerFluor.writeToFile("" + design.getFluorStaples()[i] + "");

		}

		for (int i = 0; i < design.getXStaples().length; i++) {

			writerFluor.writeToFile("" + design.getXStaples()[i] + "", 1);

		}

	}

	public void writeColours()
	{
		for (int i = 0; i < design.stapleCount(); i++) {

			writerColour.writeToFile(design.getColour(i));

		}
	}

	// set up writers
	private void initialise()
	{
		exportFilePath = outDir;

		writerStaples = new ListWriter(exportFilePath, "/staple", design.stapleCount());
		writerFinalStates = new ListWriter(exportFilePath);
		writerLegalStates = new ListWriter(exportFilePath + "/json");
		writerMetrics = new ListWriter(exportFilePath);
		writerColour = new ListWriter(exportFilePath);
		writerFluor = new ListWriter(exportFilePath + "/fluor");

		// also print staple over time (sybergold measurement)
		writerMetrics.addWriter("/staple.txt"); // also print active states		0
		writerMetrics.addWriter("/foldingStatistcs.txt"); //	1

		// also print finalStates
		writerFinalStates.addWriter("/state.txt"); //	0	
		writerFinalStates.addWriter("/stateMap.txt"); //	1
		writerFinalStates.addWriter("/contactOrder.txt"); //	2

		// set LegalStatesWriter
		writerLegalStates.addWriter("/combined-legal.json"); // 0
		writerLegalStates.addWriter("/combined.json"); //	1

		// set colourWriter
		writerColour.addWriter("/colour.txt"); //w0

		// set writerFluor
		writerFluor.addWriter("/plot.txt"); //w0
		writerFluor.addWriter("/xStaples.txt");

		// add writer for quench
		writerStaples.addWriter("/quench.txt");
		writerStaples.addWriter("/quenchObserved.txt");

		// make directory plots for dna-graph.R
		new File(exportFilePath + "/plots").mkdirs();

	}

	private void writeStaples()
	{
		double stapleSum;
		int upTo = design.time.numOfSteps + 1;
		stapleProbability = new double[design.stapleCount()][upTo];

		if (!design.isDimer()) {

			for (int i = 0; i < design.numOfStaples; i++) {
				for (int j = 0; j < upTo; j++) {

					double probability = ((double) result.stapleTable[i][j] / (double) (numOfPaths));

					this.storeStapleProbability(i, j, probability);

				}
			}

			// also write shortStaples
			for (int i = 0; i < design.numOfShortStaples; i++) {
				for (int j = 0; j < upTo; j++) {

					double probability = ((double) result.stapleTable[design.numOfStaples + i][j] / (double) (numOfPaths));
					this.storeStapleProbability(design.numOfStaples + i, j, probability);

				}
			}

		} else { // we have a dimer.

			for (int i = 0; i < design.numOfStaples; i++) {
				for (int j = 0; j < upTo; j++) {

					stapleProbability[i][j] = ((double) (result.stapleTable[4 * i][j] + result.stapleTable[4 * i + 1][j] + result.stapleTable[4 * i + 2][j] + result.stapleTable[4 * i + 3][j]) / (double) (double) (2 * numOfPaths));

					String output = (j * design.time.step) + " " + stapleProbability[i][j];
					writerStaples.writeToFile(output, i);

				}
			}

			// also write shortStaples
			for (int i = 0; i < design.numOfShortStaples; i++) {
				for (int j = 0; j < upTo; j++) {

					stapleProbability[design.numOfStaples + i][j] = (double) (result.stapleTable[design.numOfCrossover + (2 * i)][j] + result.stapleTable[design.numOfCrossover
							+ (2 * i) + 1][j])
							/ (double) (2 * numOfPaths);

					String output = (j * design.time.step) + " " + stapleProbability[design.numOfStaples + i][j];

					writerStaples.writeToFile(output, i + design.numOfStaples);

				}
			}

		}

		// generate overal staple count
		for (int j = 0; j < upTo; j++) {

			stapleSum = ((double) result.domainTable[j]) / ((double) (design.usableDomains.cardinality()) * design.numOfPaths);
			String output = (j * design.time.step) + " " + Double.toString(stapleSum);
			writerMetrics.writeToFile(output, 0);

		}

		// write quench signal
		if (!(design.getQuenchStaples() == null)) {

			double[] quenchSignal = new double[upTo];

			for (int j = 0; j < upTo; j++) {

				quenchSignal[j] = result.quench[j] / ((double) design.numOfPaths);
				writerStaples.writeToFile(Double.toString(quenchSignal[j]), design.stapleCount());

			}

			double quenchAnneal = Time.getPredictedAnnealTemp(design, quenchSignal);
			writerStaples.writeToFile("Quench observed at: " + quenchAnneal, design.stapleCount() + 1);

			if (design.getAlsoMelt()) {

				double quenchMelt = Time.getPredictedMeltTemp(design, quenchSignal);
				writerStaples.writeToFile("Quench observed during melting: " + quenchMelt, design.stapleCount() + 1);

			}

		}

	}

	private void storeStapleProbability(int i, int j, double probability)
	{
		String output;

		stapleProbability[i][j] = probability;
		output = (j * design.time.step) + " " + probability;
		writerStaples.writeToFile(output, i);
	}

	private void writeLegalStates()
	{
		String output = "";
		writerLegalStates.writeToFile("\n [", 0);

		for (int i = 0; i < design.getLegalStates().size(); i++) {

			output = jsonParser.makeInitialJSON(design.legalStates(i).state);
			output = jsonParser.writeFinalJson(output, "legal", design.legalStates(i).getType().toString(), 0.012);
			if (i < (design.getLegalStates().size() - 1)) {
				output = output + " \n ,";
			}

			writerLegalStates.writeToFile(output + "\n ", 0);
		}

		writerLegalStates.writeToFile("\n ]", 0);
	}

	private void writeRunStatistic()
	{
		String output = "Time taken is " + (System.currentTimeMillis() - design.time.startComputation) / 60000 + " min";
		output = output + "\n";
		output = output + "Experiment: " + design.getDesignString();
		output = output + "\n";
		output = output + "\n";

		writerFinalStates.writeToFile(output, 0);
		writerFinalStates.writeToFile(output, 1);
	}

	private void writeFinalStates()
	{
		HashMap<String, Integer> types = this.getTypes(legal, 0);
		HashMap<String, Integer> simpleTypes = this.getTypes(legal, 1);

		for (Entry<State, Integer> entry : result.stateFinalHash.entrySet()) {

			String type = legal.getType(entry.getKey()).type[0];
			String simpleType = legal.getType(entry.getKey()).type[1];

			writerFinalStates.writeToFile(entry.getKey() + "   " + entry.getValue() + "  " + type, 0);
			types.put(type, types.get(type) + entry.getValue());
			simpleTypes.put(simpleType, simpleTypes.get(simpleType) + entry.getValue());

		}

		for (Entry<String, Integer> entry : simpleTypes.entrySet()) {

			writerFinalStates
					.writeToFile(((double) entry.getValue() / (double) design.numOfPaths) + "\t   " + entry.getKey() + " \t    " + entry.getValue(), 1);
		}
		writerFinalStates.writeToFile("\n \n", 1);

		for (Entry<String, Integer> entry : types.entrySet()) {

			writerFinalStates.writeToFile((double) entry.getValue() / (double) design.numOfPaths + "\t  " + entry.getKey() + "\t   " + entry.getValue(), 1);

		}

		writerFinalStates.writeToFile("\n \n", 1);
		writerFinalStates.writeToFile("Number of paths: \t" + design.numOfPaths, 1);
		writerFinalStates.writeToFile("TempStart is " + design.time.tempStart);
		writerFinalStates.writeToFile("\n \n", 1);

		writerLegalStates.writeToFile("\n [", 1);

		boolean toggle = false;
		for (Entry<State, Integer> entry : result.stateFinalHash.entrySet()) {

			String output = "";

			if (toggle) {
				output = output + " \n ,";
			} else {
				toggle = true;
			}
			output = output + jsonParser.makeInitialJSON(entry.getKey());
			output = jsonParser.writeFinalJson(output, "Model", legal.getType(entry.getKey()).type, entry.getValue());

			writerLegalStates.writeToFile(output + "\n ", 1);

		}

		writerLegalStates.writeToFile("\n ]", 1);
	}

	private HashMap<String, Integer> getTypes(Legal legal2, int i)
	{
		HashMap<String, Integer> output = new HashMap<String, Integer>();

		for (LegalState state : design.getLegalStates()) {

			if (!output.containsKey((state.getType()))) {

				output.put(state.getType().type[i], 0);

			}

		}

		output.put(design.noType.type[i], 0);

		return output;
	}

	public void writeRCommand(String command)
	{
		writerFinalStates.writeToFile(command, 1);
	}

	public void writeStaple(int i, double temperature, double concentration)
	{
		String output = "" + temperature + "  " + concentration;
		writerStaples.writeToFile(output, i);
	}

	public void writeStaple(double temperature, double concentration)
	{
		String output = "" + temperature + "  " + concentration;
		writerMetrics.writeToFile(output, 0);
	}

	public void close()
	{
		writerStaples.close();
		writerFinalStates.close();
		writerLegalStates.close();
		writerMetrics.close();
		writerColour.close();
		writerFluor.close();
		scatter.close();
		heatmap.close();
	}

}
