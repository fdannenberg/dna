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

public class State
{
	Design design;

	public StateGraph graph; // this allows us to calculate least-distance dijkstra
	public StateSub sub; // this keeps track of substates, required for legal calculations
	public StateLoop loop; // this keeps track of loops (second energy calculation, uses inner and outer loops)

	public BitSet crossover; // keeps track of which crossovers are in place
	public BitSet domain; // keeps track of which domains are dualstranded
	public BitSet halfBound; // keeps track of which domains are bound by a half-bound staple.

	public State(Design design2)
	{
		design = design2;

		crossover = new BitSet(design.numOfCrossover);
		domain = new BitSet(design.scaffoldLength);
		halfBound = new BitSet(design.scaffoldLength);

		if (design.model.useLoop()) {
			loop = new StateLoop(design);
		}
		if (design.model.useDistance()) {
			graph = new StateGraph(design);
		}

		sub = new StateSub(design, domain);
	}

	public State(State other)
	{
		design = other.design;

		crossover = (BitSet) other.crossover.clone();
		domain = (BitSet) other.domain.clone();
		halfBound = (BitSet) other.halfBound.clone();
	}

	public void update(int pos, boolean val) // update adding or removing a half-bound staple 
	{
		halfBound.set(pos, val);
		this.updateDomain(pos, val);
	}

	private void updateDomain(int pos, boolean val)
	{
		domain.set(pos, val);

		if (design.model.useDistance()) {
			graph.setDomain(pos, val);
		}
		if (design.model.useLoop()) {
			loop.update(pos, this);
		}

		if (design.isDimer()) {
			sub.update(pos, val, crossover); // update legal states
		}
	}

	public void update(int pos0, boolean val0, int pos1, boolean val1) // update adding or removing crossover Staple
	{
		int[] leftRight = design.crossoverToDomain[pos1];

		domain.set(pos0, val0); // update the domains
		crossover.set(pos1, val1); // set the crossover

		halfBound.set(leftRight[0], !val1); // sets 2 halfbounds if removing staple,
		halfBound.set(leftRight[1], !val1); // removes 2 halfbounds if adding staple.
		halfBound.set(pos0, false);

		if (design.model.useDistance()) {
			this.updateGraph(pos0, val0, pos1, val1);
		}
		if (design.model.useLoop()) {
			this.updateLoop(pos0, val0, pos1, val1);
		}

		if (design.isDimer()) {
			sub.update(pos0, val0, pos1, val1, crossover);
		}

	}

	private void updateLoop(int pos0, boolean val0, int pos1, boolean val1)
	{
		loop.update(pos0, val0, pos1, val1, this);
	}

	private void updateGraph(int pos0, boolean val0, int pos1, boolean val1)
	{
		graph.setDomain(pos0, val0);

		if (val1) { // adding crossover, so it could turn into a double bond

			this.updateCrossover(pos1, val1);

		} else { // removing crossover. have to test if there is no other crossover in the same place.

			if (design.testDoubleInPlace(pos1, crossover)) {
				// nothing, since other crossover is in place

			} else {

				this.updateCrossover(pos1, val1);

			}

		}

		if (design.printing) {
			System.out.println("Setting crossover in graph  between " + design.getStaple(pos1, 1) + " and " + design.getStaple(pos1, 2));
		}
	}

	private void updateCrossover(int pos1, boolean val1)
	{
		graph.setCrossover(new int[] { design.getStaple(pos1, 1), design.getStaple(pos1, 2) }, val1);
	}

	public String toString()
	{
		String output = "";
		output = output + crossover + "   " + domain + "   " + halfBound;

		return output;
	}

	public void addHalfBoundStapleLeft(int staple)
	{
		this.update(design.crossoverToDomain[staple][0], true);
	}

	public void addHalfBoundStapleRight(int staple)
	{
		this.update(design.crossoverToDomain[staple][1], true);
	}

	public void addFullStaple(int i)
	{
		int[] leftRight = design.crossoverToDomain[i];
		this.update(leftRight[0], true); // put in half staple
		this.update(leftRight[1], true, i, true); // now put in full staple
	}

	public void removeFullStaple(int i)
	{
		int[] leftRight = design.crossoverToDomain[i];
		// put in half staple
		this.update(leftRight[0], false);
		// now put in full staple
		this.update(leftRight[1], false, i, false);

	}

	public BitSet getCrossover()
	{
		return crossover;
	}

	public BitSet getDomain()
	{
		return domain;
	}

	public BitSet getHalfBound()
	{
		return halfBound;
	}

	@Override
	public int hashCode()
	{
		int output = crossover.hashCode() * domain.hashCode() * halfBound.hashCode();

		return output;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof folding.State) {

			folding.State ander = (folding.State) other;

			boolean one = domain.equals(ander.domain);
			boolean two = crossover.equals(ander.crossover);
			boolean three = halfBound.equals(ander.halfBound);

			return (one && two && three);

		} else {

			return false;
		}
	}

	public String getSpecialHash(int left, int right)
	{
		String output = this.toString();
		output = output + left + right;

		return output;
	}

	public void addStaple12(int[] array)
	{
		for (int i = 0; i < array.length; i++) {

			this.addStaple12(array[i]);
		}
	}

	public void addStaple21(int[] array)
	{
		for (int i = 0; i < array.length; i++) {

			this.addStaple21(array[i]);
		}
	}

	public void addStaple12(int pos)
	{
		this.addFullStaple(4 * pos);
		this.addFullStaple(4 * pos + 3);
	}

	public void addStaple21(int pos)
	{
		this.addFullStaple(4 * pos + 1);
		this.addFullStaple(4 * pos + 2);
	}

	public void removeStaple12(int pos)
	{
		this.removeFullStaple(4 * pos);
		this.removeFullStaple(4 * pos + 3);
	}

	public void removeStaple21(int pos)
	{
		this.removeFullStaple(4 * pos + 1);
		this.removeFullStaple(4 * pos + 2);
	}

	public String getColourByDomain(int domain)
	{
		String output = "purple";

		ArrayList<Integer> possible = design.domainToCrossover.get(domain);

		for (Integer bond : possible) {

			if (crossover.get(bond)) { // the domain has a staple in place

				int staple = bond / 4;

				output = design.getColour(staple);

			}

		}

		return output;
	}

	public State clone()
	{ 
		State newState = new State(this);

		if (design.model.useLoop()) {
			newState.loop = new StateLoop(this.loop);
		}
		if (design.model.useDistance()) {
			newState.graph = new StateGraph(this);
		}
		if (design.isDimer()) {
			newState.sub = new StateSub(this.sub);
		}

		return newState;
	}

}
