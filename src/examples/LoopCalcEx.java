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

import folding.Constant;
import folding.Design;
import folding.EnergyCalc;
import folding.FreeEnergy;
import folding.LoopCalc;
import folding.State;
import folding.Util;

public class LoopCalcEx
{
	// example dG and rate calc for SI methods paper
	public static void main(String[] args)
	{
		String designString = "mock3";
		double rate = 1.0;
		Design design = new Design(designString, rate, "loop-M2", true, "");

		double costA1 = 97.0 * 1.0E-18;
		double costA2 = 119.2 * 1.0E-18;
		double costA3 = 209.7 * 1.0E-18;

		double costB1 = 97.0 * 1.0E-18;
		double costB2 = 100.2 * 1.0E-18;
		double costB3 = 37.8 * 1.0E-18;
		double costB4 = 222.0 * 1.0E-18;

		double costC1 = 97.0 * 1.0E-18;
		double costC2 = 106.9 * 1.0E-18;
		double costC3 = 197.3 * 1.0E-18;

		double costD1 = 97.0 * 1.0E-18;
		double costD2 = 131.5 * 1.0E-18;
		double costD3 = 222.0 * 1.0E-18;

		double dSA1 = LoopCalc.calcDS(costA1, design.model);
		double dSA2 = LoopCalc.calcDS(costA2, design.model);
		double dSA3 = LoopCalc.calcDS(costA3, design.model);

		double dSB1 = LoopCalc.calcDS(costB1, design.model);
		double dSB2 = LoopCalc.calcDS(costB2, design.model);
		double dSB3 = LoopCalc.calcDS(costB3, design.model);
		double dSB4 = LoopCalc.calcDS(costB4, design.model);

		double dSC1 = LoopCalc.calcDS(costC1, design.model);
		double dSC2 = LoopCalc.calcDS(costC2, design.model);
		double dSC3 = LoopCalc.calcDS(costC3, design.model);

		double dSD1 = LoopCalc.calcDS(costD1, design.model);
		double dSD2 = LoopCalc.calcDS(costD2, design.model);
		double dSD3 = LoopCalc.calcDS(costD3, design.model);

		double dSA = dSA1 + dSA2 + dSA3;
		double dSB = dSB1 + dSB2 + dSB3 + dSB4;
		double dSC = dSC1 + dSC2 + dSC3;
		double dSD = dSD1 + dSD2 + dSD3;

		double diffAB = (dSA - dSB);
		double diffAC = (dSA - dSC);
		double diffDA = (dSD - dSA);

		System.out.println("dSA = " + dSA);
		System.out.println("dSB = " + dSB);
		System.out.println("dSC = " + dSC);
		System.out.println("DifferenceAB delta G 37 is " + EnergyCalc.calcG37(0, diffAB));
		System.out.println("DifferenceAC delta G 37 is " + EnergyCalc.calcG37(0, diffAC));
		System.out.println("DifferenceDA delta G 37 is " + EnergyCalc.calcG37(0, diffDA));

		main2(args);

	}

	public static void main2(String[] args)
	{
		String designString = "mock3";

		double rate = 1.0;

		Design design = new Design(designString, rate, "loop-M2", true, "");
		State state = Util.generateInitialState(design);

		state.addFullStaple(0);
		state.update(3, true);

		FreeEnergy energyA = state.loop.getEnergy();

		state.update(6, true, 1, true);
		FreeEnergy energyB = state.loop.getEnergy();

		state.removeFullStaple(1);
		FreeEnergy energyC = state.loop.getEnergy();

		state.update(3, true);
		state.update(6, true);
		FreeEnergy energyD = state.loop.getEnergy();

		FreeEnergy differenceAB = new FreeEnergy(energyA);
		differenceAB.subtract(energyB);
		double g37AB = EnergyCalc.calcG37(differenceAB.dH(), differenceAB.dS());
		System.out.println("g37AB is " + g37AB);
		System.out.println("conc AB " + Math.exp(-differenceAB.dS() / Constant.kb));

		FreeEnergy differenceAC = new FreeEnergy(energyA);
		differenceAC.subtract(energyC);
		double g37AC = EnergyCalc.calcG37(differenceAC.dH(), differenceAC.dS());
		System.out.println("g37AC is " + g37AC);
		System.out.println("conc AC " + Math.exp(differenceAC.dS() / Constant.kb));

		FreeEnergy differenceDA = new FreeEnergy(energyD);
		differenceDA.subtract(energyA);
		double g37DA = EnergyCalc.calcG37(differenceDA.dH(), differenceDA.dS());
		System.out.println("g37DA is " + g37DA);
		System.out.println("conc DA " + Math.exp(differenceDA.dS() / Constant.kb));
	}

}
