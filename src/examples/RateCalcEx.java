/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package examples;

import java.util.ArrayList;
import folding.Design;
import folding.EnergyCalc;
import folding.LoopCalc;
import folding.RatesGenerator;
import folding.State;
import folding.Transition;
import folding.Util;

public class RateCalcEx // example rate computation, both using production code & by hand.  
{
	static State stateA;
	static State stateB;
	static State stateC;
	static State stateD;

	public static void main(String[] args)
	{
		String designString = "mock2";
		String modelString = "distance-M1";
		//String modelString = "loop-M1";

		double rate = 1.0;
		Design design = new Design(designString, rate, modelString, true, "Unified-suppressWarnings");

		setUpStateABCD(design);

		RatesGenerator ratesGenerator = new RatesGenerator(design);
		ratesGenerator.setUnbindRate(60 * (85 - 60)); // temp = 60 C 

		//state.loop.setPrinting(true);
		State state = stateA;
		System.out.println(" State is: " + state);

		ArrayList<Transition> rates = ratesGenerator.rates(state);
		System.out.println("Number of transitions is " + rates.size());
		System.out.println("state is " + stateA);
		Util.print(rates);

		main2(args);
		main3(args);
	}

	private static void setUpStateABCD(Design design)
	{
		stateA = Util.generateInitialState(design);

		stateA.addFullStaple(1);
		stateA.update(6, true);

		stateB = Util.generateInitialState(design);
		stateB.addFullStaple(1);

		stateC = Util.generateInitialState(design);
		stateC.addFullStaple(1);
		stateC.update(6, true);
		stateC.update(9, true);

		stateD = Util.generateInitialState(design);
		stateD.addFullStaple(0);
		stateD.addFullStaple(1);
	}

	// second example, compute some values by hand (these figures were not used in the end)
	public static void main2(String[] args)
	{
		String designString = "design23e";
		double coolingRate = 1.0;

		Design design = new Design(designString, coolingRate, "distance-M1", true, "Unified");

		System.out.println("FIGURE 1 160nt");
		double distance = (160 * (1.0 / 3.0) * 1.8 * 1.8) * 1E-18;
		getStats(distance, design);

		System.out.println("FIGURE 1 640nt");
		distance = (640 * (1.0 / 3.0) * 1.8 * 1.8) * 1E-18;
		getStats(distance, design);

		System.out.println("SITUATION A");
		distance = (56 * (1.0 / 3.0) * 1.8 * 1.8 + 1 * 16.0 * 0.34 * 16 * 0.34) * 1E-18;
		getStats(distance, design);

		System.out.println("SITUATION B");
		distance = ((48 * (1.0 / 3.0) + 1) * 1.8 * 1.8 + 1 * 16.0 * 0.34 * 16 * 0.34) * 1E-18;
		getStats(distance, design);
	}

	private static void getStats(double distance, Design design)
	{
		double rate = design.getKPlus() * Math.pow(design.model.preFactor() / distance, design.model.gamma());

		System.out.println("Distance-sqrd is " + distance);
		System.out.println("Rate is " + rate);
		System.out.println("Concentration is " + (rate / design.getKPlus()));

		double dS = LoopCalc.calcDS(distance, design.model);

		System.out.println("G-37 is " + EnergyCalc.calcG37(0, dS));
		System.out.println("G-60 is " + EnergyCalc.calcG60(0, dS));
	}

	// compute the transition for an simple state in the dimer (polymorphic tile)
	public static void main3(String[] args)
	{
		String designString = "design23e";
		double rate = 1.0;
		Design design = new Design(designString, rate, "distance-M1", true, "");

		State state = Util.generateInitialState(design);
		RatesGenerator ratesGenerator = new RatesGenerator(design);

		state.addFullStaple(4 * 16);
		state.update(55, true);

		// by default the rates are printed at temp_start.
		ArrayList<Transition> rates = ratesGenerator.rates(state);
		System.out.println("Number of transitions is " + rates.size());
		System.out.println("state is " + state);
		Util.print(rates);
	}

}
