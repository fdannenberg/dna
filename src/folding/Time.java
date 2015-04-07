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

public class Time
{
	public static final int samplingRate = 60;

	public double tempStart, tempEnd, step, rate, timeEnd;
	public int numOfSteps;
	public long startComputation;

	public Time(double rate2, double tempStart2, boolean alsoMelt, String hiddenOptions)
	{
		rate = rate2;
		tempStart = tempStart2;

		if (hiddenOptions.contains("EndAt70")) {

			tempEnd = 59.0 + 273.15;

		} else {

			tempEnd = 20 + 273.15;

		}

		step = 60 / (rate * samplingRate); // time in sec before moving updating temp
		numOfSteps = (int) ((tempStart - tempEnd) * samplingRate);

		timeEnd = (tempStart - tempEnd) * (60.0 / rate);

		if (alsoMelt) {
			this.alsoMelt();
		}

	}

	public void printStepsInfo()
	{
		System.out.println("numOfSteps is " + numOfSteps);
	}

	public double temp(double time)
	{
		int numOfSteps2 = (int) ((tempStart - tempEnd) * samplingRate);
		double temp = tempStart - (Math.max(0.0, time) / step) * ((tempStart - tempEnd) / numOfSteps2);

		if (temp < tempEnd) {

			temp = (2.0 * tempEnd) - temp;

		}

		return temp;
	}

	public double tempCelcius(double time)
	{
		return (this.temp(time) - 273.15);
	}

	public double graphTime(double time)
	{
		return -(tempStart - (time / step) * ((tempStart - tempEnd) / numOfSteps) - 273.15);
	}

	private void alsoMelt()
	{
		numOfSteps = 2 * numOfSteps;
		this.printStepsInfo();
	}

	public static double getPredictedAnnealTemp(Design design, double[] stapleProbability)
	{
		boolean annealTemp = false;

		int upTo = design.time.numOfSteps + 1;

		for (int time = 0; time < (upTo - 1); time++) {

			double stapleSum = stapleProbability[time];
			double stapleSumNext = stapleProbability[time + 1];

			if (stapleSum > 0.5 && stapleSumNext > 0.5 && !annealTemp) {
				annealTemp = true;
				return design.time.tempCelcius(((double) time) * design.time.step);
			}

		}

		return -1.0;
	}

	public static double getPredictedMeltTemp(Design design, double[] stapleProbability)
	{
		int upTo = design.time.numOfSteps + 1;

		if (design.getAlsoMelt()) {

			for (int time = 0; time < (upTo - 1); time++) {

				if (time > (int) (0.5 * design.time.numOfSteps) && (stapleProbability[time] < 0.5)) {

					return design.time.tempCelcius(((double) time) * design.time.step);

				}

			}

		}

		return -1.0;
	}

	public int timeFromTemp(double temperature)
	{
		int output = (int) (numOfSteps * (tempStart - temperature - 273.15) / (tempStart - tempEnd));

		if (output < 0 || output > (numOfSteps)) {

			System.out.println("WRONG TEMPERATURE COMPUTED");
			return 11;

		}

		return output;

	}

}
