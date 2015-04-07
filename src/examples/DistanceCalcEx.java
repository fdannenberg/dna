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

import folding.Design;
import folding.EnergyCalc;
import folding.LoopCalc;

public class DistanceCalcEx
{

	// compute E[R^2] values for main paper SI.
	public static void main(String[] args)
	{

		String designString = "design23eEnergy";
		double rate = 0.4;
		Design design = new Design(designString, rate, "distance"); // model defaults to gamma = 1.5

		double loopCost;
		loopCost = LoopCalc.calcDS(LoopCalc.calcDistanceSquared(0, 28, 0), design.model);
		System.out.println("Loop cost for 448/3 nt G60 is " + EnergyCalc.calcG60(0, loopCost));

		loopCost = LoopCalc.calcDS(LoopCalc.calcDistanceSquared(1, 138, 0), design.model);
		System.out.println("Loop cost 2xDS+2208 nt G60 is " + EnergyCalc.calcG60(0, loopCost));

		loopCost = LoopCalc.calcDS(LoopCalc.calcDistanceSquared(3, 25, 1), design.model);
		System.out.println("Loop cost 2xDS+2208 nt G60 is " + EnergyCalc.calcG60(0, loopCost));

		loopCost = LoopCalc.calcDS(LoopCalc.calcDistanceSquared(1, 55, 0), design.model);
		System.out.println("Loop cost 2xDS+2208 nt G60 is " + EnergyCalc.calcG60(0, loopCost));

	}

}
