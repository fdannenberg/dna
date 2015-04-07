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

public  class LoopCalc
{
	public static final FreeEnergy getCost(Loop loop, BitSet domain, Design design)
	{
		double cost = 0.0;

		for (int i = 0; i < loop.numOfInterval; i++) {

			cost = cost + costOfInterval(loop.getInterval(i), domain, design);

		}

		cost = cost + loop.getNumOfCrossoverSegments() * Constant.csCost;
		cost = cost * 1E-18;

		return new FreeEnergy(0.0, LoopCalc.calcDS(cost, design.model));
	}

	private static double costOfInterval(int[] interval, BitSet domain, Design design)
	{
		double output = 0.0;

		for (int i = interval[0]; i < interval[1]; i++) {

			if (domain.get(i)) { // domain is ds

				output = output + design.rigidity.dsCost[i];

			} else { // domain is ss

				output = output + design.rigidity.ssCost[i];

			}

		}
		return output;
	}

	public static final Double seamEnergy(Model model)
	{
		double cost = 2 * Constant.ssCostSingleSegment * 1.0E-18;
		return LoopCalc.calcDS(cost, model);
	}

	public static double calcDS(double cost, Model model)
	{
		cost = Math.pow((model.preFactor() / cost), model.gamma());
		cost = Constant.kb * Math.log(cost);

		return cost;
	}

	public static double calcDistanceSquared(double numOfDS, double numOfSS, double numOfCS) // ONLY USED FOR PAPER
	{
		double cost = 0.0;

		cost = cost + ((double) numOfDS) * Math.pow(16.0 * Constant.risePerBaseDS, 2.0); // dual stranded domains consist of 1 part of length 16*0.34nm    // 29.59
		cost = cost + ((double) numOfSS) * Constant.numSSperDomainSS * Constant.ssCostSingleSegment; // single stranded domain consist of 5.33 parts of each length 1.8nm 	
		cost = cost + ((double) numOfCS) * Constant.ssCostSingleSegment; // 3.24 per crossover section

		cost = cost * 1.0E-18; // nmsqrd

		System.out.println("DO NOT USE IN PRODUCTION CODE! dist squared is " + cost);

		return cost;
	}

}
