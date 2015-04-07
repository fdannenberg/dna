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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import writers.Result;

public class Simulator implements Runnable
{

	/**
	 * Frits Dannenberg 26/01/2012
	 * Gillespie simulation, multithreaded
	 */

	// simulation objects
	private double time;
	private int k, ID, steps;
	private State currentState;
	private Random generator, generatorAccept;
	private SimWriter simWriter;

	private Design design;
	private int numOfPaths;
	private RatesGenerator ratesGenerator;
	private Result result;

	private PathMetrics pathMetric;

	public Simulator(Design origamiDesign2, RatesGenerator ratesGenerator2, Result origamiResult, int numOfPaths2, int ID2)
	{
		design = origamiDesign2;
		ratesGenerator = ratesGenerator2;
		result = origamiResult;
		numOfPaths = numOfPaths2;
		ID = ID2;
		simWriter = new SimWriter(design, ID);

		this.setUnbindRate(0);
	}

	public void doSimulation()
	{
		generator = new Random();
		System.nanoTime(); // extra delay
		generatorAccept = new Random();

		// start the simulation
		for (int i = 0; i < numOfPaths; i++) {

			pathMetric = new PathMetrics(design);

			currentState = Util.generateInitialState(design);
			this.simPath(currentState);
			System.out.println("Thread ID " + ID + "   Steps taken = " + steps + " Paths done: " + (i + 1)); // print some info to user

		}

	}

	// print out what state the system is in after numOfSteps * step time
	// simulates entire path.
	private void simPath(State currentState2)
	{
		k = 0;
		time = 0;
		steps = 0;

		boolean keepGoing = true;

		while (keepGoing) {

			this.moveToNextState();
			this.writePerStepStatistic(currentState);

			steps++;

			// if enough reactions have been simulated, write the current state
			// to the file. also, break the while loop.
			while (time >= (design.time.step * (k + 1))) {

				k++;

				this.setUnbindRate(design.time.step * k);
				this.writeCurrentState(k);
				this.printInfo();

				if (k > design.time.numOfSteps) {

					this.writeFinalState();
					keepGoing = false;

				}

			}

		}
	}

	private void writePerStepStatistic(State currentState2)
	{
		if (design.isDimer()) {
			pathMetric.perStepStatistic.update(currentState2);
		}
	}

	private void printInfo()
	{
		if (numOfPaths == 1) {
			if (time % 1.0 == 0) {
				System.out.println(currentState.toString());
				simWriter.write(currentState, design.time.temp(time));

			}
		}
	}

	// updates currentState to the next state, and updates time.
	// If reaction time > maxTau, then stay in currentState and update time with
	// maxTau.
	private void moveToNextState()
	{
		// fetch the exit rates and target states (possibly from cache)
		ArrayList<Transition> currentStateTransition = ratesGenerator.rates(currentState);

		// a0 is the sum of exit rates
		double a0 = 0.0;
		Iterator<Transition> iterator = currentStateTransition.iterator();
		while (iterator.hasNext()) {
			a0 = a0 + iterator.next().getRate();
		}
		// Two uniform random variables needed to do the Gillespie simulation
		// first, generate the time until next jump;
		double r1 = generator.nextDouble();
		double tau = -Math.log(r1) / a0;

		// second, select the next state
		// j becomes the index of the randomly chosen transition
		// Exception: If the predicted reaction time is too large, then do not
		// update the current state.

		if ((time + tau) > ((k + 1) * design.time.step)) {

			time = (k + 1) * design.time.step;

		} else {

			time = time + tau;
			currentState = this.selectState(a0, currentStateTransition);

		}
	}

	private State selectState(double a0, ArrayList<Transition> currentStateTransition)
	{
		double a, r2;
		boolean exit = false;
		int j = 0;

		j = 0;
		r2 = generatorAccept.nextDouble();
		a = r2 * a0;

		while (exit == false) {

			a = a - currentStateTransition.get(j).getRate();

			if (a < 0) {
				exit = true;
			} else {
				j++;
			}
		}

		Transition selected = currentStateTransition.get(j);
		result.updateTransition(selected);

		return selected.update(currentState);
	}

	private void setUnbindRate(double time)
	{ // sets time, which sets temperature. This fixes any thermodynamic Boltzmann dist
		ratesGenerator.setUnbindRate(time);
	}

	// keep track of interesting properties during folding
	private void writeCurrentState(int timePoint)
	{
		result.writeCurrentState(currentState, timePoint);
		pathMetric.writeCurrentState(currentState, timePoint);
	}

	private void writeFinalState()
	{
		pathMetric.storeFinalState(currentState, ratesGenerator.getLegal());
		result.store(pathMetric);

		result.incrementFinalState(new State(currentState));
		result.mergePerStepStatistic(pathMetric.perStepStatistic);
	}

	public void run()
	{
		this.doSimulation();
		simWriter.finalize();
	}

}
