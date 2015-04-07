/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package plotting;

import java.util.ArrayList;
import writers.ListWriter;
import writers.Result;
import writers.ScatterPlot;
import folding.Design;
import folding.Rates;
import folding.RatesGenerator;
import folding.State;
import folding.Transition;
import folding.Util;

public class Heatmap
{
	Design design;
	SvgCreator svgCreator;
	Result result;
	ScatterPlot scatter;

	public Heatmap(Design design2, Result result2, ScatterPlot scatter2)
	{
		design = design2;
		svgCreator = new SvgCreator(design);
		result = result2;
		scatter = scatter2;
	}

	public void plot(SvgSettings settings)
	{
		ColourMap heatmap, heatmap2, heatmap3, heatmap4, halfBound, unbind, crossover, bindStatistic, bindStatistic2, bindStatistic3, bindStatisticAuto;

		if (design.getDesignString().contains("design23e") || design.getDesignString().contains("mono")) {

			heatmap = this.genTemp(64.0, 68.0, "heatmap", settings);
			heatmap.setUnit(" C");
			this.createGraphics(heatmap, settings);

			heatmap2 = this.genTemp(58.0, 76.0, "heatmap2", settings);
			heatmap2.setUnit(" C");
			this.createGraphics(heatmap2, settings);

			heatmap3 = this.genTemp(60.0, 74.0, "heatmap3", settings);
			heatmap3.setUnit(" C");
			this.createGraphics(heatmap3, settings);

			heatmap4 = this.genTemp(56.0, 76.0, "heatmap4", settings);
			heatmap4.setUnit(" C");
			this.createGraphics(heatmap4, settings);

			heatmap3 = this.genTemp(62.0, 70.0, "heatmap5", settings);
			heatmap3.setUnit(" C");
			this.createGraphics(heatmap3, settings);

			double left = 5.0;
			double right = 25.0;
			bindStatistic = generateColorMap(result.reconfigurationStats(settings), new double[] { left, right }, settings);
			bindStatistic.setName("bindStatistic-" + left + "-" + right);
			bindStatistic.setUnit("");
			this.createGraphics(bindStatistic, settings);

			left = 5.0;
			right = 20.0;
			bindStatistic = generateColorMap(result.reconfigurationStats(settings), new double[] { left, right }, settings);
			bindStatistic.setName("bindStatistic-" + left + "-" + right);
			bindStatistic.setUnit("");
			this.createGraphics(bindStatistic, settings);

			left = 3.0;
			right = 8.0;
			bindStatistic2 = generateColorMap(result.reconfigurationStats(settings), new double[] { left, right }, settings);
			bindStatistic2.setName("bindStatistic-" + left + "-" + right);
			bindStatistic2.setUnit("");
			this.createGraphics(bindStatistic2, settings);

			left = 3.0;
			right = 9.0;
			bindStatistic3 = generateColorMap(result.reconfigurationStats(settings), new double[] { left, right }, settings);
			bindStatistic3.setName("bindStatistic-" + left + "-" + right);
			bindStatistic3.setUnit("");
			this.createGraphics(bindStatistic3, settings);

			bindStatisticAuto = generateColorMap(result.reconfigurationStats(settings), settings);
			bindStatisticAuto.setName("bindStatistic-auto");
			bindStatisticAuto.setUnit("");
			this.createGraphics(bindStatisticAuto, settings);

			if (settings.useStaples) {

				//			bindHalfBound				// type 2
				halfBound = this.generateColoringMapfromDomainCounts(result.attachTable[2], settings);
				halfBound.setName("halfBound");
				halfBound.setUnit("");
				this.createGraphics(halfBound, settings);

				//			unbindDomains				// type 1
				unbind = this.generateColoringMapfromDomainCounts(result.attachTable[1], settings);
				unbind.setName("unbind");
				unbind.setUnit("");
				this.createGraphics(unbind, settings);

				//			bindCrossover				// type 0
				crossover = this.generateColoringMapfromDomainCounts(result.attachTable[0], settings);
				crossover.setName("crossover");
				crossover.setUnit("");
				this.createGraphics(crossover, settings);

			}

			int[] test = this.getTestData();
			ColourMap testMap = generateColorMap(test, settings);
			testMap.setName("testMap");
			testMap.setUnit("");
			this.createGraphics(testMap, settings);

			if (design.isDimer()) {
				ColourMap pRatio = this.generatePRatio();
				pRatio.setName("pRatio");
				pRatio.setUnit("");
				this.createGraphics(pRatio, settings);
			}

		}

	}

	private int[] getTestData()
	{
		int[] output = new int[design.numOfStaples];

		for (int i = 0; i < design.numOfStaples; i++) {
			output[i] = i;
		}

		return output;
	}

	private ColourMap generatePRatio()
	{
		double[] countsByStaple = this.genCountsByStaplePRatio();
		return generateColorMap(countsByStaple, false, new double[] { 0.0, 0.0 }, true, new SvgSettings());
	}

	private double[] genCountsByStaplePRatio()
	{
		double[] output = new double[design.numOfStaples];

		for (int i = 0; i < design.numOfStaples; i++) {

			output[i] = this.generatePRatio(i);

		}

		return output;
	}

	private double generatePRatio(int i)
	{
		double[] rates = this.generateCrossoverRates(i);

		double smallest = Math.min(rates[0], rates[1]);
		double p = smallest / (rates[0] + rates[1]);

		return -Math.log(p);
	}

	private double[] generateCrossoverRates(int staple)
	{
		double[] output = new double[2];

		State state = Util.generateInitialState(design);
		RatesGenerator ratesGenerator = new RatesGenerator(design);
		Rates rates = ratesGenerator.getRatesObject();

		int position = design.getStaple(4 * staple, 0);

		state.update(position, true);

		ArrayList<Transition> transitions = rates.getCrossoverRates(state);

		output[0] = transitions.get(0).getRate();

		if (transitions.size() > 1) {
			output[1] = transitions.get(1).getRate();
		} else {
			output[1] = 1;
		}

		return output;
	}

	private void createGraphics(ColourMap colourMap, SvgSettings settings)
	{
		svgCreator.createGraphics(colourMap, settings);
		this.write(colourMap, settings);
	}

	private void write(ColourMap map, SvgSettings settings)
	{
		ListWriter writer = new ListWriter(design.outDir, settings.out + map.getName() + ".txt");

		for (int i = 0; i < design.numOfStaples; i++) {
			writer.writeToFile("staple" + i + " : " + map.getValue(i));
		}

	}

	private ColourMap generateColoringMapfromDomainCounts(int[] countsByDomain, SvgSettings settings)
	{
		int[] countsByStaple = this.genCountsByStaple(countsByDomain);
		return generateColorMap(countsByStaple, settings);
	}

	private ColourMap generateColorMap(int[] countsByStaple, SvgSettings settings)
	{
		double[] doubleCounts = Util.castAsDoubleArray(countsByStaple);
		double[] bounds = { 0.0, 0.0 };
		return generateColorMap(doubleCounts, true, bounds, true, settings);
	}

	private ColourMap generateColorMap(int[] countsByStaple, double[] bounds, SvgSettings settings)
	{
		double[] doubleCounts = Util.castAsDoubleArray(countsByStaple);
		return generateColorMap(doubleCounts, true, bounds, false, settings);
	}

	private ColourMap generateColorMap(double[] countsByStaple, boolean compensateForPaths, double[] bounds, boolean generateBounds, SvgSettings settings)
	{
		int numOfPaths;
		if (compensateForPaths && settings.useStaples) {

			numOfPaths = design.numOfPaths;

		} else if (!settings.useStaples) {

			numOfPaths = settings.getCounts();

		} else {

			numOfPaths = 1;

		}

		ColourMap map = new ColourMap(design);

		if (generateBounds) {

			bounds[0] = Util.getMin(countsByStaple) / ((double) numOfPaths); // min
			bounds[1] = Util.getMax(countsByStaple) / ((double) numOfPaths); // max

		}

		map.setLow(bounds[0]);
		map.setTop(bounds[1]);
		map.setMid();

		for (int i = 0; i < design.numOfStaples; i++) {

			double value = countsByStaple[i] / ((double) numOfPaths);
			map.setStapleValue(i, value);

			map.setDomainValue(design.getDomainFromType(i)[0], value);
			map.setDomainValue(design.getDomainFromType(i)[1], value);

		}

		if (countsByStaple.length == (design.numOfStaples + design.numOfShortStaples)) {

			for (int i = 0; i < design.numOfShortStaples; i++) {

				map.setShortStapleValue(i, countsByStaple[i + design.numOfShortStaples] / ((double) numOfPaths));

			}

		} else {

			for (int i = 0; i < design.numOfShortStaples; i++) {

				map.setShortStapleValue(i, 0.0);

			}

		}

		return map;
	}

	private int[] genCountsByStaple(int[] countsByDomain)
	{
		int[] output = new int[design.numOfStaples];

		for (int i = 0; i < design.scaffoldLength; i++) {

			int staple = design.domainToStapleType[i];

			if (staple >= 0) {

				output[staple] = output[staple] + countsByDomain[i];

			}

		}

		return output;
	}

	private ColourMap genTemp(double low, double high, String name, SvgSettings settings)
	{
		ColourMap map = new ColourMap(design);

		map.setLow(low);
		map.setTop(high);
		map.setMid();

		map.setName(name);

		if (settings.useStaples) {

			for (int i = 0; i < design.numOfStaples; i++) {

				map.setStapleValue(i, scatter.predictedAnnealTemp[i]);

			}

			for (int i = 0; i < design.numOfShortStaples; i++) {

				map.setShortStapleValue(i, scatter.predictedAnnealTemp[design.numOfStaples + i]);

			}

		} else {

			for (int i = 0; i < design.simpleScaffoldLength; i++) {

				double value = result.pathMeltingTemperatureDomain(i, settings.getSelection());
				map.setDomainValue(i, value);

			}

		}

		return map;
	}

	public void close()
	{
		svgCreator.close();
	}

}
