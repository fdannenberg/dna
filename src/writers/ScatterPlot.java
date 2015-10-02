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

import java.text.DecimalFormat;

import folding.Design;
import folding.Time;

public class ScatterPlot
{
	public double[] predictedAnnealTemp; // for staples
	public double[] predictedMeltTemp; // for staples
	public double[] analyticalAnnealTemp; // predicted = observed in simulation

	public double seamTemp = 0.0;
	public double bodyTemp = 0.0;
	public double averageStapleTemp = 0.0;

	private Design design;
	private ListWriter writer;
	private String outDir;

	public ScatterPlot(Design design2, String outDir2)
	{
		design = design2;
		outDir = outDir2;
	}

	public void genScatterPlot(double[][] stapleProbability)
	{
		this.initWriter();
		this.initWriter();
		this.generateTable(stapleProbability);
		this.writeTable();
		
	}


	private void writeTable()
	{
		// assume predictedAnnealTemp is generated
		DecimalFormat df = new DecimalFormat("#.###");

		for (int i = 0; i < (design.numOfStaples + design.numOfShortStaples); i++) {

			String output = "";

			output = output + df.format(predictedAnnealTemp[i]) + "   ";
			output = output + df.format(analyticalAnnealTemp[i]) + "   ";
			output = output + design.getColour(i);

			writer.writeToFile(output, 0);

			if (i < design.numOfStaples) {

				writer.writeToFile(output, 1);

			}

		}

		for (int i = 0; i < (design.numOfStaples + design.numOfShortStaples); i++) {

			String output = "staple" + i + ":   ";

			output = output + df.format(predictedMeltTemp[i]) + "   ";

			writer.writeToFile(output, 3);
		}

		int numberOfSeamStaples = design.getNumberOfSeamStaples();

		for (int i = 0; i < design.numOfStaples; i++) {

			if (design.isSeamStaple(i)) {

				seamTemp = seamTemp + predictedAnnealTemp[i];

			} else {

				bodyTemp = bodyTemp + predictedAnnealTemp[i];

			}

			averageStapleTemp = averageStapleTemp + predictedAnnealTemp[i];

		}

		seamTemp = seamTemp / (double) numberOfSeamStaples;
		bodyTemp = bodyTemp / (double) (design.numOfStaples - numberOfSeamStaples);
		averageStapleTemp = averageStapleTemp / (double) design.numOfStaples;

		writer.writeToFile("Average seam-staple temp= " + df.format(seamTemp) + "  C ", 4);
		writer.writeToFile("Average body-staple temp= " + df.format(bodyTemp) + "  C ", 4);
		writer.writeToFile("Average staple temp= " + df.format(averageStapleTemp) + "  C ", 4);

	}

	private void generateTable(double[][] stapleProbability)
	{
		// generate predicted annealing temperature

		predictedAnnealTemp = new double[design.numOfStaples + design.numOfShortStaples];
		predictedMeltTemp = new double[design.numOfStaples + design.numOfShortStaples];
		analyticalAnnealTemp = new double[design.numOfStaples + design.numOfShortStaples];

		for (int type = 0; type < (design.numOfStaples + design.numOfShortStaples); type++) {

			predictedAnnealTemp[type] = Time.getPredictedAnnealTemp(design, stapleProbability[type]);
			analyticalAnnealTemp[type] = this.getAnalyticalAnnealTemp(type);

			if (design.getAlsoMelt()) {

				predictedMeltTemp[type] = this.getPredictedMeltTemp(stapleProbability, type);

			}

		}

	}

	private double getPredictedMeltTemp(double[][] stapleProbability, int type)
	{
		boolean meltTemp = false;

		int upTo = design.time.numOfSteps + 1;

		for (int time = (int) (0.5 * upTo); time < (upTo - 1); time++) {

			double stapleSum = stapleProbability[type][time];
			double stapleSumNext = stapleProbability[type][time + 1];

			if (stapleSum < 0.5 && stapleSumNext < 0.5 && !meltTemp) {
				meltTemp = true;
				return (design.time.temp(time * design.time.step) - 273.15);
			}

		}

		return -1.0;

	}

	private double getAnalyticalAnnealTemp(int type)
	{
		double output;

		if (type < design.numOfStaples) { // regular crossover staple

			int left = design.getDomainFromType(type)[0];
			int right = design.getDomainFromType(type)[1];

			output = 0.5 * (design.energy.getTM(left) + design.energy.getTM(right));

		} else { // dealing with a short staple

			int domain = design.getDomainFromType(type)[0];

			output = design.energy.getTM(domain);

		}

		return output;
	}

	private void initWriter()
	{
		writer = new ListWriter(outDir + "/scatterPlot/");

		writer.addWriter("allStaple.txt");
		writer.addWriter("uStaple.txt");
		writer.addWriter("command.txt");

		writer.addWriter("melt-allStaple.txt");
		writer.addWriter("annealTemps");

	}

	public void close()
	{
		if (writer != null) {
			writer.close();
		}
	}

}
