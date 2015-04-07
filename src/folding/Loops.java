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
import java.util.BitSet;
import java.util.Iterator;

public class Loops
{
	Design design;
	RatesCalc calc;

	private ArrayList<Loop> set; // set of loops
	private int nillLoops;
	boolean printing = false;

	FreeEnergy overalCost; // Free energy in the system as measured from an arbitrary zero point.

	public Loops(Design design2)
	{
		design = design2;
		calc = new RatesCalc(design2);
		set = new ArrayList<Loop>();

		// add initial loop, assume initial state
		Loop loop = new Loop(design);
		loop.setCost(new BitSet());
		set.add(loop);

		overalCost = new FreeEnergy();
		overalCost.add(loop.cost);
	}

	public Loops(Loops loop)
	{ // copy constructor
		design = loop.design;
		calc = new RatesCalc(design);

		set = new ArrayList<Loop>();

		Iterator<Loop> it = loop.set.iterator();
		while (it.hasNext()) {
			Loop e = it.next();
			set.add(new Loop(e));
		}

		nillLoops = loop.nillLoops;
		printing = loop.printing;
		overalCost = new FreeEnergy(loop.overalCost);
	}

	public void update(int domain, State state)
	{
		int index = findLoopContainingDomain(domain);
		updateCost(index, state);

		if (printing) {
			System.out.println("updated2 " + domain + " " + state);
			this.print();
		}
	}

	private void updateCost(Integer pos, State state)
	{
		Loop loop = set.get(pos);

		overalCost.subtract(loop.cost);
		loop.setCost(state.domain);
		overalCost.add(loop.cost);
	}

	public void print()
	{
		System.out.println("Set size is " + set.size() + "  num of nill loops is " + nillLoops);
		Util.printArrayList(set);
	}

	public void update(int pos0, boolean val0, int pos1, boolean val1, State state)
	{
		if (val1) {//adding crossover

			if (design.testDoubleInPlace(pos1, state.crossover)) { // crossover is already in place

				nillLoops++;
				overalCost.add(calc.nillLoops(1));

			} else { // crossover generates a new loop

				this.addLoop(pos1, state);

			}

		} else { // removing crossover

			if (design.testDoubleInPlace(pos1, state.crossover)) { // crossover is already in place

				nillLoops--;
				overalCost.subtract(calc.nillLoops(1));

			} else { // crossover generates a new loop

				this.mergeLoop(pos1, state);

			}

		}

		if (printing) {
			System.out.println(" updated4 " + pos0 + " " + val0 + " " + pos1 + " " + val1);
			this.print();
		}
	}

	private void addLoop(int crossover, State state)
	{
		if (design.getStaple(crossover, 1) != design.getStaple(crossover, 2)) { // staple is not continuous

			int[] domains = design.getCrossoverToCut(crossover);
			int index = findLoop(domains);
			this.splitLoops(index, domains, state);

		} else {

			throw new RuntimeException(
					"Loops.java, method addLoop: Continuous staple detected. This specification is depriciated, so update model specification.");

		}
	}

	private void splitLoops(int index, int[] domains, State state)
	{
		if (printing) {
			System.out.println("small, large " + domains[0] + "  " + domains[1]);
		}

		Loop oldLoop = set.get(index);
		Loop newLoop = new Loop(design);

		overalCost.subtract(oldLoop.cost);

		oldLoop.split(domains[0], domains[1], newLoop);

		this.updateAndAddCosts(oldLoop, state.domain);
		this.updateAndAddCosts(newLoop, state.domain);

		set.add(newLoop);
	}

	private void updateAndAddCosts(Loop loop, BitSet domain)
	{
		loop.setCost(domain);
		overalCost.add(loop.cost);
	}

	private void mergeLoop(int crossover, State state)
	{
		// find the two loops that need to be merged
		int[] pos = design.getCrossoverToCut(crossover);
		int[] index = findTwoLoops(pos);

		if (printing) {
			System.out.println("remove crossover " + crossover);
			System.out.println("left,right" + pos[0] + "," + pos[1]);
			System.out.println("Merging " + index[0] + "  " + index[1]);
		}

		overalCost.subtract(set.get(index[1]).cost); // delete cost of loop1
		set.get(index[0]).mergeWith(set.get(index[1])); // merge loop1 into loop0, loop0 cost is not updated
		updateCost(index[0], state); // removes previous cost of loop0, updated cost loop0, adds cost loop0

		set.remove(index[1]); // let go of loop1;
	}

	private int findLoopContainingDomain(int domain)
	{
		return findLoop(new int[] { domain, design.getDomainRight(domain) });
	}

	private int findLoop(int[] pos)
	{
		int size = set.size();

		for (int i = 0; i < size; i++) {

			if (set.get(i).contains(pos[0]) && set.get(i).contains(pos[1])) {

				return i;
			}

		}

		throw new RuntimeException("Error in Loops.findLoop. Could not find loop when expect to do so.");
	}

	public boolean testPseudoKnot(int crossover)
	{
		// find the two loops that need to be merged
		int[] pos = design.getCrossoverToCut(crossover);
		return (findLoop(pos) == -1);
	}

	private int[] findTwoLoops(int[] pos) //TODO could also use dynamic programming to keep check of this
	{
		int[] output = { -1, -1 };
		int size = set.size();
		boolean found = false;

		for (int i = 0; i < size; i++) {

			if (!found && set.get(i).contains(pos[0]) && set.get(i).contains(pos[1])) {

				output[0] = i;
				found = true;

			} else if (found && set.get(i).contains(pos[0]) && set.get(i).contains(pos[1])) {

				output[1] = i;
				break;
			}

		}

		return output;
	}

	public void setPrinting(boolean bool)
	{
		printing = bool;
	}

	public FreeEnergy getEnergy()
	{
		return overalCost;
	}

}
