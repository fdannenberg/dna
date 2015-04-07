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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import plotting.SvgSettings;
import folding.Design;
import folding.LegalType;
import folding.PathMetrics;
import folding.State;
import folding.Time;
import folding.Transition;

public class Result
{

	Design design;
	public int[][] stapleTable; // Staples printing info
	public int[] quench; // indicate if quenching is witnessed 
	public int[][] stateTable; // Staples printing info
	public int[] domainTable; // domain bind info
	public int[][] attachTable; // staple attach count
	ArrayList<ArrayList<String>> seamStatistic; // conditionalized on what seam is first formed, keep statistic in mind for final distribution.
	public HashMap<State, Integer> stateFinalHash;
	public HashMap<State, String> jsonArray;

	private ArrayList<PathMetrics> pathMetricsList;
	private JsonParser jsonParser;
	private PerStepStatistic perStepStatistic;

	public Result(Design design2)
	{
		design = design2;
		int upTo = design.time.numOfSteps + 50;

		stapleTable = new int[design.numOfCrossover + design.numOfShortDomain()][upTo];
		quench = new int[upTo];
		stateTable = new int[design.numOfStaples][upTo];
		domainTable = new int[upTo];
		stateFinalHash = new HashMap<State, Integer>();
		jsonArray = new HashMap<State, String>();
		jsonParser = new JsonParser(design);
		seamStatistic = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 5; i++) {
			seamStatistic.add(new ArrayList<String>());
		}

		attachTable = new int[4][design.scaffoldLength];

		perStepStatistic = new PerStepStatistic(design);
		pathMetricsList = new ArrayList<PathMetrics>();
	}

	public synchronized void incrementFinalState(State state)
	{
		Integer count = 0;

		if (stateFinalHash.containsKey(state)) {

			count = stateFinalHash.get(state);

		} else {

			count = 0;

		}

		stateFinalHash.put(state, count + 1);

		if (count == 0) {

			jsonArray.put(state, jsonParser.makeInitialJSON(state));

		}
	}

	public synchronized void writeCurrentState(State state, int timePoint)
	{
		//  increment the staple count
		for (int i = 0; i < design.numOfCrossover; i++) {

			if (state.crossover.get(i)) {

				stapleTable[i][timePoint]++;

			}

		}

		for (int i = 0; i < design.numOfShortDomain(); i++) {

			if (state.domain.get(design.shortToDomain[i])) {
				stapleTable[i + design.numOfCrossover][timePoint]++;
			}

		}

		// now increment the domains
		int sum = state.domain.cardinality(); // stores how many domains are bound currently
		domainTable[timePoint] += sum;

		// write if the quenching is observed or not

		if (!(design.getQuenchStaples() == null) && design.getQuenchStaples().length == 2) {

			if (state.getCrossover().get(design.getQuenchStaples()[0]) && state.getCrossover().get(design.getQuenchStaples()[1])) {

				quench[timePoint]++;

			}

		}

	}

	public synchronized void updateTransition(Transition transition)
	{
		int type = transition.getType();
		attachTable[type][transition.getNextState().pos0()]++;
	}

	public synchronized void mergePerStepStatistic(PerStepStatistic other)
	{
		perStepStatistic.merge(other);
	}

	public synchronized void store(PathMetrics foldingResult)
	{
		pathMetricsList.add(foldingResult);
	}

	public DescriptiveStatistics statisticsFoldingWidth()
	{
		DescriptiveStatistics output = new DescriptiveStatistics();

		for (int i = 0; i < pathMetricsList.size(); i++) {

			if (pathMetricsList.get(i).didFold()) {

				output.addValue(pathMetricsList.get(i).foldingTime());

			}

		}

		return output;
	}

	public DescriptiveStatistics statisticsAnnealTemp()
	{
		DescriptiveStatistics output = new DescriptiveStatistics();

		for (int i = 0; i < pathMetricsList.size(); i++) {

			if (pathMetricsList.get(i).boolAnneal) {

				output.addValue(pathMetricsList.get(i).getAnnealTemp());

			}

		}

		return output;
	}

	public DescriptiveStatistics statisticsMeltTemp()
	{
		DescriptiveStatistics output = new DescriptiveStatistics();

		for (int i = 0; i < pathMetricsList.size(); i++) {

			if (pathMetricsList.get(i).boolMelt) {

				output.addValue(pathMetricsList.get(i).getMeltTemp());

			}

		}

		return output;
	}

	public DescriptiveStatistics statisticsDiffTemp()
	{
		DescriptiveStatistics output = new DescriptiveStatistics();

		for (int i = 0; i < pathMetricsList.size(); i++) {

			if (pathMetricsList.get(i).boolMelt && pathMetricsList.get(i).boolAnneal) {

				output.addValue(pathMetricsList.get(i).getDiffTemp());

			}

		}

		return output;
	}

	public double pathMeltingTemperatureDomain(int domain, String selection)
	{
		// this method returns the melting temperature of a domain based on the individual simulation paths.
		double[] domainProbability = new double[design.time.numOfSteps + 2];
		int count = 0;

		for (PathMetrics path : pathMetricsList) {

			if (selection.equals(LegalType.all) | path.getType().equals(selection)) {

				count++;
				BitSet[] domainTable = path.getDomainTable();

				for (int k = 1; k < design.time.numOfSteps + 2; k++) {

					if (domainTable[k].get(domain)) {

						domainProbability[k]++;

					}

				}
			}

		}

		if (count > 0) {

			for (int k = 0; k < design.time.numOfSteps + 2; k++) {

				domainProbability[k] = (double) domainProbability[k] / (double) count;

			}

			return Time.getPredictedAnnealTemp(design, domainProbability);

		} else {

			return (-100.0);

		}

	}

	public int[] reconfigurationStats(SvgSettings settings)
	{
		if (settings.useStaples) {

			return perStepStatistic.historic.getCounts();

		} else {

			// this method returns the reconfiguration stats of a domain based on the individual simulation paths.

			History history = new History(design);
			String selection = settings.getSelection();
			int count = 0;

			for (PathMetrics path : pathMetricsList) {

				if (selection.equals(LegalType.all) | path.getType().equals(selection)) {

					count++;

					history.merge(path.perStepStatistic.historic);

				}
			}

			if (count > 0) {

				settings.setCounts(count);
				return history.getCounts();

			} else {

				return ((new History(design)).getCounts());

			}

		}
	}

	public ArrayList<PathMetrics> getPathMetricsList()
	{
		return pathMetricsList;
	}
}
