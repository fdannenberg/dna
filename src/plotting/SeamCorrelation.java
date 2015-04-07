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
import java.util.BitSet;
import java.util.Map;

import writers.ListWriter;
import writers.Result;
import folding.Design;
import folding.LegalType;
import folding.PathMetrics;

public class SeamCorrelation
{
	Design design;
	Result result;
	SvgCreator svgCreator;

	private boolean useFrequency = true; // internally switch between OR or AND observation of seam pairs.
	private double temperature;
	private Map<Integer, ArrayList<Integer>> seam;

	public SeamCorrelation(Design design2, Result result2)
	{
		design = design2;
		svgCreator = new SvgCreator(design);
		result = result2;

		seam = design.seam();
	}

	public void write(double temperature2)
	{
		temperature = temperature2;

		for (int i = 0; i < LegalType.legalTypes.length; i++) {

			SvgSettings settings = new SvgSettings("seamCorrelation2", LegalType.legalTypes[i], temperature);
			this.makeSeamCorrelation2(settings);

		}
	}

	private void makeSeamCorrelation2(SvgSettings settings)
	{

		ColourMap map = this.makeMapping(settings, 81);
		int timeStep = design.time.timeFromTemp(temperature);
		String selection = settings.getSelection();

		map.setLow(0.0);
		map.setTop(2.0);
		map.setMid(0.25, 0.5, 0.75);

		map.setName(selection);

		for (int seam = 0; seam < 5; seam++) {

			for (int oppSeam = 0; oppSeam < 5; oppSeam++) {

				double value = this.computeCorrelation("sameOrientation", timeStep, selection, seam, oppSeam);

				if (seam == oppSeam) {

					map.setValue(75 + seam, value);
					map.setWhite(15 * seam + oppSeam);

				} else {

					map.setValue(15 * seam + oppSeam, value);

				}

			}

			for (int oppSeam = 0; oppSeam < 5; oppSeam++) {

				double value = this.computeCorrelation("oppositeOrientation", timeStep, selection, seam, oppSeam);

				map.setValue(15 * seam + oppSeam + 5, value);

			}

			for (int oppSeam = 0; oppSeam < 5; oppSeam++) {

				double value = this.computeCorrelation("sideOrientation", timeStep, selection, seam, oppSeam);

				map.setValue(15 * seam + oppSeam + 10, value);

			}

		}

		// set the overall body staple frequency
		double value = this.getBodyStapleFreq(timeStep, selection);
		map.setValue(80, value);

		svgCreator.createGraphics(map, settings);

		// save some special properties manually
		this.writeProperties(settings, timeStep, selection);

	}

	private void writeProperties(SvgSettings settings, int timeStep, String selection)
	{

		ListWriter writer;
		int matchingPaths = 0;
		writer = new ListWriter(design.outDir + settings.out, settings.getSelection() + "-prop.txt");

		int prop1 = 0;
		int prop2 = 0;
		int prop3 = 0;

		int prop4 = 0;
		int prop4M = 0;
		int prop5 = 0;
		int prop6 = 0;
		//		int prop4 = 0;

		useFrequency = false;

		for (PathMetrics path : result.getPathMetricsList()) {

			if (selection.equals(LegalType.all) | path.getType().equals(selection)) {

				matchingPaths++;
				BitSet crossover = path.getCrossover(timeStep);

				// prop1 = having at least a four or five-type seam in place
				int fourFiveOccur = this.checkOccurance(3, 3, crossover) + this.checkOccurance(4, 4, crossover);

				if (fourFiveOccur > 0) {

					prop1++;

				}

				// prop2 = having at least 1 seam pair at 90 degree angle
				int sideOccur = 0;

				for (int seam = 0; seam < 5; seam++) {

					for (int oppSeam = 0; oppSeam < 5; oppSeam++) {

						sideOccur = sideOccur + checkSideOccurance(seam, oppSeam, crossover);

					}

				}

				if (sideOccur > 0) {

					prop2++;

				}

				prop3 = prop3 + this.checkSideOccurance(1, 1, crossover);

				// prop 4: having at least a fifth-type seam in place
				prop4 = prop4 + this.checkOccurance(4, 4, crossover);

				// prop 4-Modded: having at least a fifth-type seam in place and no sideways links
				if (sideOccur == 0) {

					prop4M = prop4M + this.checkOccurance(4, 4, crossover);

				}

				// prop 5: having 4th staple in place, and at least one side staple of the first kind in place
				int testFifth = this.checkOccurance(3, 3, crossover) + this.checkSideOccurance(0, 0, crossover);
				if (testFifth > 1) {
					prop5++;
				}

				// prop 6: having the second seam paired with the second seam
				prop6 = prop6 + this.checkSideOccurance(1, 1, crossover);

			}

		}

		writer.writeToFile("prop1 " + prop1);
		writer.writeToFile("prop2 " + prop2);
		writer.writeToFile("prop3 " + prop3);
		writer.writeToFile("prop4 " + prop4);
		writer.writeToFile("prop4M " + prop4M);
		writer.writeToFile("prop5 " + prop5);
		writer.writeToFile("prop6 " + prop6);
		writer.writeToFile("paths " + matchingPaths);

		writer.close();

		useFrequency = true;
	}

	public void close()
	{
		svgCreator.close();
	}

	private double getBodyStapleFreq(int timeStep, String selection)
	{

		int count = 0;
		int matchingPaths = 0;

		for (PathMetrics path : result.getPathMetricsList()) {

			if (selection.equals(LegalType.all) | path.getType().equals(selection)) {

				matchingPaths++;
				count = count + checkBodyStapleOccurance(timeStep, path);

			}
		}

		if (matchingPaths > 0) {

			return (double) count / (double) (1.0 * matchingPaths * design.getBodyStaples().cardinality());

		} else {

			return (0.0);

		}

	}

	private int checkBodyStapleOccurance(int timeStep, PathMetrics path)
	{
		BitSet crossover = (BitSet) path.getCrossover(timeStep).clone();
		BitSet bodyStapleCrossovers = design.getBodyStaples();

		crossover.and(bodyStapleCrossovers);

		return crossover.cardinality();
	}

	private double computeCorrelation(String selector, int timeStep, String selection, int seam, int oppSeam)
	{
		int count = 0;
		int matchingPaths = 0;
		double output = 0.0;

		for (PathMetrics path : result.getPathMetricsList()) {

			if (selection.equals(LegalType.all) | path.getType().equals(selection)) {

				matchingPaths++;
				BitSet crossover = path.getCrossover(timeStep);

				if (selector.equals("sameOrientation")) {

					count = count + this.checkOccurance(seam, oppSeam, crossover);

				} else if (selector.equals("oppositeOrientation")) {

					count = count + this.checkOppositeOccurance(seam, oppSeam, crossover);

				} else if (selector.equals("sideOrientation")) {

					count = count + this.checkSideOccurance(seam, oppSeam, crossover);
				}

			}
		}

		if (matchingPaths > 0) {

			output = (double) count / (double) (2.0 * matchingPaths);

		} else {

			return 0.0;

		}

		if (useFrequency) {
			output = output * 0.5; // compensating since the frequency counts from four sides
		}

		return output;
	}

	private int checkOccurance(int thisSeam, int oppSeam, BitSet crossover)
	{
		// TRUE if the indicated pair of seam and oppSeam occurs at the time step.
		boolean[] thisPresent = this.getSeamPresence(crossover, thisSeam);
		boolean[] oppPresent = this.getSeamPresence(crossover, oppSeam);

		int output = 0;

		if (!useFrequency) {

			boolean observe = false;

			observe = observe || (thisPresent[0] && oppPresent[0]);
			observe = observe || (thisPresent[1] && oppPresent[1]);
			observe = observe || (thisPresent[2] && oppPresent[2]);
			observe = observe || (thisPresent[3] && oppPresent[3]);

			if (observe) {
				output++;
			}

		} else {

			if (thisPresent[0] && oppPresent[0]) {
				output++;
			}
			if (thisPresent[1] && oppPresent[1]) {
				output++;
			}
			if (thisPresent[2] && oppPresent[2]) {
				output++;
			}
			if (thisPresent[3] && oppPresent[3]) {
				output++;
			}

		}

		return output;
	}

	private int checkOppositeOccurance(int thisSeam, int oppSeam, BitSet crossover)
	{
		boolean[] thisPresent = this.getSeamPresence(crossover, thisSeam);
		boolean[] oppPresent = this.getSeamPresence(crossover, oppSeam);

		int output = 0;

		if (!useFrequency) {

			boolean observed = false;
			observed = observed || (thisPresent[0] && oppPresent[1]);
			observed = observed || (thisPresent[1] && oppPresent[0]);
			observed = observed || (thisPresent[2] && oppPresent[3]);
			observed = observed || (thisPresent[3] && oppPresent[2]);

			if (observed) {
				output++;
			}

		} else { // useFrequency true

			if (thisPresent[0] && oppPresent[1]) {
				output++;
			}
			if (thisPresent[1] && oppPresent[0]) {
				output++;
			}
			if (thisPresent[2] && oppPresent[3]) {
				output++;
			}
			if (thisPresent[3] && oppPresent[2]) {
				output++;
			}

			if (thisSeam == oppSeam) {

				return output / 2;

			}

		}

		return output;
	}

	private int checkSideOccurance(int thisSeam, int oppSeam, BitSet crossover)
	{
		boolean[] thisPresent = this.getSeamPresence(crossover, thisSeam);
		boolean[] oppPresent = this.getSeamPresence(crossover, oppSeam);

		int output = 0;

		if (!useFrequency) {

			boolean observed = false;

			observed = observed || (thisPresent[0] && (oppPresent[2] || oppPresent[3]));
			observed = observed || (thisPresent[1] && (oppPresent[2] || oppPresent[3]));
			observed = observed || (thisPresent[2] && (oppPresent[0] || oppPresent[1]));
			observed = observed || (thisPresent[3] && (oppPresent[0] || oppPresent[1]));

			if (observed) {
				output++;
			}

		} else { //  useFrequency true

			if (thisPresent[0] && (oppPresent[2])) {
				output++;
			}
			if (thisPresent[1] && (oppPresent[2])) {
				output++;
			}
			if (thisPresent[2] && (oppPresent[0])) {
				output++;
			}
			if (thisPresent[3] && (oppPresent[0])) {
				output++;
			}
			if (thisPresent[0] && (oppPresent[3])) {
				output++;
			}
			if (thisPresent[1] && (oppPresent[3])) {
				output++;
			}
			if (thisPresent[2] && (oppPresent[1])) {
				output++;
			}
			if (thisPresent[3] && (oppPresent[1])) {
				output++;
			}

			if (thisSeam == oppSeam) {

				return output / 2;

			}

		}

		return output;
	}

	private boolean[] getSeamPresence(BitSet crossover, int thisSeam)
	{
		boolean[] presence = new boolean[4];

		int this0 = seam.get(thisSeam).get(0);
		int this1 = seam.get(thisSeam).get(1);
		int this2 = seam.get(4 - thisSeam).get(0);
		int this3 = seam.get(4 - thisSeam).get(1);

		presence[0] = crossover.get(4 * this0 + 0) || crossover.get(4 * this1 + 0);
		presence[1] = crossover.get(4 * this0 + 3) || crossover.get(4 * this1 + 3);
		presence[2] = crossover.get(4 * (this2) + 1) || crossover.get(4 * (this3) + 1);
		presence[3] = crossover.get(4 * (this2) + 2) || crossover.get(4 * (this3) + 2);

		return presence;
	}

	private ColourMap makeMapping(SvgSettings settings, int size)
	{
		ColourMap mapping = new ColourMap(design, size);
		return mapping;
	}

}
