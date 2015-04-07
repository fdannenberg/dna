/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package folding;

import java.util.BitSet;
import writers.PerStepStatistic;

public class PathMetrics
{
	// PathMetrics helps recording statistics about the individual folding path.
	// This is similar to the functionality of Result, which tracks more basic properties.  

	public PerStepStatistic perStepStatistic;
	public boolean boolAnneal, boolMelt;

	private int start, end;
	private boolean recordedStart, recordedEnd;
	private Design design;

	private double annealTemp = 0.0;
	private double meltTemp = 0.0;
	private double[] movingAverage;
	private double currentAverage;
	private int currentPos;
	private int size = 12; // moving average

	// statistics of staple and domain 
	private BitSet[] crossoverTable;
	private BitSet[] domainTable;
	private LegalType finalType;

	public PathMetrics(Design design2)
	{
		design = design2;
		perStepStatistic = new PerStepStatistic(design);

		movingAverage = new double[size];

		for (int i = 0; i < size; i++) {
			movingAverage[i] = 0.0;
		}

		currentAverage = 0.0;
		currentPos = 0;

		crossoverTable = new BitSet[design.time.numOfSteps + 2];
		domainTable = new BitSet[design.time.numOfSteps + 2];
	}

	public void writeCurrentState(State currentState, int timePoint)
	{
		this.recordSteepness(currentState, timePoint);
		this.recordAnnealAndMeltTemps(currentState, timePoint);
		this.recordStapleAndDomainState(currentState, timePoint);
	}

	private void recordStapleAndDomainState(State currentState, int timePoint)
	{
		crossoverTable[timePoint] = (BitSet) currentState.crossover.clone();
		domainTable[timePoint] = (BitSet) currentState.domain.clone();
	}

	private void recordAnnealAndMeltTemps(State currentState, int timePoint)
	{
		currentAverage = currentAverage - movingAverage[currentPos % size];

		double stapleSum = ((double) currentState.domain.cardinality() / (double) (design.usableDomains.cardinality()));
		movingAverage[currentPos % size] = stapleSum / ((double) size);

		currentAverage = currentAverage + movingAverage[currentPos % size];
		currentPos++;

		if (currentAverage > 0.5 && !boolAnneal) {
			boolAnneal = true;
			annealTemp = Util.limit(design.time.tempCelcius((timePoint - (size / 2)) * design.time.step), 100000);
		}

		if (design.getAlsoMelt() && timePoint > (int) 0.5 * design.time.numOfSteps && currentAverage < 0.5 && !boolMelt && boolAnneal) {
			boolMelt = true;
			meltTemp = Util.limit(design.time.tempCelcius((timePoint - (size / 2)) * design.time.step), 100000);

		}

	}

	private void recordSteepness(State currentState, int timePoint)
	{
		if (!recordedStart) {

			if (((double) currentState.domain.cardinality() / (double) currentState.design.scaffoldLength) > Constant.width_lower) {

				start = timePoint;
				recordedStart = true;

			}

		} else if (!recordedEnd) {

			if ((double) (currentState.domain.cardinality() / (double) currentState.design.scaffoldLength) > Constant.width_upper) {

				end = timePoint;
				recordedEnd = true;

			}

		}

	}

	public boolean didFold()
	{
		return recordedStart && recordedEnd;
	}

	public double foldingTime()
	{
		if (didFold()) {

			return design.time.rate * ((double) (end - start)) / Time.samplingRate;

		} else {

			throw new IllegalArgumentException("Please don't ask for folding time when it cannot be defined");

		}
	}

	public double getAnnealTemp()
	{
		return annealTemp;
	}

	public double getMeltTemp()
	{
		return meltTemp;
	}

	public double getDiffTemp()
	{
		return (meltTemp - annealTemp);
	}

	public void storeFinalState(State currentState, Legal legal)
	{
		finalType = legal.getType(currentState);
		System.out.println("Setting final state to " + finalType.type[0] + " " + finalType.type[1]);
	}

	public String getType()
	{// return simple type for the final state		
		return finalType.type[1];
	}

	public BitSet[] getDomainTable()
	{
		return domainTable;
	}

	public BitSet getCrossover(int timePoint)
	{
		return crossoverTable[timePoint];
	}

}
