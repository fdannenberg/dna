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

// this class keeps all of the substates accounted for.
public class StateSub
{
	Design design;
	ArrayList<BitSet> subStates;
	ArrayList<BitSet> subCrossovers;
	BitSet domain; // copy from state
	int[] domainMap; // mapping from domain to which substate - substate 0 is used for stand alone bits
	private boolean printing = false;

	public StateSub(Design design2, BitSet domain2)
	{
		design = design2;
		domain = domain2;

		subStates = new ArrayList<BitSet>();
		subCrossovers = new ArrayList<BitSet>();
	}

	public StateSub(StateSub sub) 
	{
		//  only interested in monomers anyway
		if (sub.design.isDimer()) {
			System.out.println("Copy constructor for StateSub not implemented -- ODE mode only supports monomer");
		}

	}

	public void update(int pos, boolean val, BitSet crossover)
	{
		if (val) { // updating adding a staple

			int posLeft = design.getDomainLeft(pos);
			int posRight = design.getDomainRight(pos);

			int subLeft = getSubState(posLeft);
			int subRight = getSubState(posRight);

			if (subLeft > -1 && subRight > -1) {

				setDomain(subLeft, pos, val);
				merge(subLeft, subRight);

			} else if (subLeft > -1) {

				setDomain(subLeft, pos, val);

			} else if (subRight > -1) {

				setDomain(subRight, pos, val);

			} else {

				makeNewState(pos, val);

			}

		} else { // updating removing a staple

			int index = getSubState(pos);

			if (index < 0) {

				System.out.println("ERROR: domain cannot be set to single stranded");

			}

			setDomain(index, pos, val);
			rebuildState(index, crossover);

		}

		this.print();
	}

	public void update(int pos0, boolean val0, int pos1, boolean val1, BitSet crossover)
	{
		if (val1) { // updating adding a crossover

			int[] conn = genConnections(pos0, val0, pos1, val1);

			if ((conn[0] > -1) && (conn[1] > -1) && (conn[2] > -1)) {

				int large = Util.getLarge(conn[0], conn[1], conn[2]);
				int medium = Util.getMedium(conn[0], conn[1], conn[2]);
				int small = Util.getSmall(conn[0], conn[1], conn[2]);

				setDomain(small, pos0, val0, pos1, val1);
				merge(medium, large);
				merge(small, medium);

			} else if ((conn[0] > -1) && (conn[1] > -1)) {

				setDomain(conn[0], pos0, val0, pos1, val1);
				merge(conn[0], conn[1]);

			} else if (conn[0] > -1 && conn[2] > -1) {

				setDomain(conn[0], pos0, val0, pos1, val1);
				merge(conn[0], conn[2]);

			} else if (conn[1] > -1 && conn[2] > -1) {

				setDomain(conn[1], pos0, val0, pos1, val1);
				merge(conn[1], conn[2]);

			} else if (conn[0] > -1) {

				setDomain(conn[0], pos0, val0, pos1, val1);

			} else if (conn[1] > -1) {

				setDomain(conn[1], pos0, val0, pos1, val1);

			} else if (conn[2] > -1) {

				setDomain(conn[2], pos0, val0, pos1, val1);

			} else {

				System.out.println("Something is wrong in StateSub Code A");

			}

		} else { // updating removing a crossover -- may break apart in three sections.. 

			int index = getSubState(pos0);
			if (index < 0) {
				System.out.println("ERROR: domain cannot be set to single stranded");
			}

			setDomain(index, pos0, val0, pos1, val1);
			rebuildState(index, crossover);

		}

		this.print();
	}

	private int[] genConnections(int pos0, boolean val0, int pos1, boolean val1)
	{
		int[] output = new int[3];

		int posLeft = design.getDomainLeft(pos0);
		int posRight = design.getDomainRight(pos0);
		int crossoverConnection = -1;

		int[] leftRight = design.crossoverToDomain[pos1];

		if (leftRight[0] == pos0) {

			crossoverConnection = leftRight[1];

		} else if (leftRight[1] == pos0) {

			crossoverConnection = leftRight[0];

		} else {

			System.out.println("Something is wrong in update() for class StateSub");

		}
		output[0] = getSubState(posLeft);
		output[1] = getSubState(posRight);
		output[2] = getSubState(crossoverConnection);

		return output;
	}

	private void rebuildState(int index, BitSet crossover)
	{
		while (!subStates.get(index).isEmpty()) {

			BitSet newState = makeNewState();
			subStates.add(newState);
			BitSet newCrossover = makeNewCrossover();
			subCrossovers.add(newCrossover);

			int occupiedPos = getOccupiedPos(subStates.get(index));
			ArrayList<Integer> toCheck = new ArrayList<Integer>();

			if (occupiedPos > -1) {
				toCheck.add(occupiedPos);
			}

			while (!toCheck.isEmpty()) {

				toCheck = funnel(newState, subStates.get(index), newCrossover, subCrossovers.get(index), toCheck, crossover);
			}
		}

		subStates.remove(index);
		subCrossovers.remove(index);
	}

	private ArrayList<Integer> funnel(BitSet newState, BitSet oldState, BitSet newCrossover, BitSet oldCrossover, ArrayList<Integer> toCheck, BitSet crossover)
	{
		// checks for positions in toCheck if they are set in oldState, and transfers them to newState if possible. We add 
		// new values to toCheck  for each domain we transfer to the new state.

		ArrayList<Integer> newToCheck = new ArrayList<Integer>();
		ArrayList<Integer> newToCheckCrossovers = new ArrayList<Integer>();

		for (int pos : toCheck) {

			if (oldState.get(pos)) {

				oldState.clear(pos);
				newState.set(pos);
				addConnections(newToCheck, newToCheckCrossovers, pos, crossover);

			}

		}

		for (int pos : newToCheckCrossovers) {

			if (oldCrossover.get(pos)) {

				oldCrossover.clear(pos);
				newCrossover.set(pos);

			}

		}

		return newToCheck;
	}

	private void addConnections(ArrayList<Integer> toCheck, ArrayList<Integer> toCheckCrossover, int pos, BitSet crossover)
	{
		toCheck.add(design.getDomainLeft(pos));
		toCheck.add(design.getDomainRight(pos));

		ArrayList<Integer> possibleCrossovers = design.domainToCrossover.get(pos);

		for (Integer cross : possibleCrossovers) {

			if (crossover.get(cross)) {

				toCheck.add(design.crossoverToDomain[cross][0]);
				toCheck.add(design.crossoverToDomain[cross][1]);
				toCheckCrossover.add(cross);

			}

		}
	}

	private int getOccupiedPos(BitSet BitSet)
	{
		return BitSet.nextSetBit(0);
	}

	private void makeNewState(int pos, boolean val)
	{
		BitSet newState = makeNewState();
		newState.set(pos, val);
		subStates.add(newState);

		BitSet newCrossover = makeNewCrossover();
		subCrossovers.add(newCrossover);
	}

	private void merge(int left, int right)
	{
		if (left != right) {

			subStates.get(left).or(subStates.get(right));
			subStates.remove(right);

			subCrossovers.get(left).or(subCrossovers.get(right));
			subCrossovers.remove(right);
		}
	}

	private void setDomain(int index, int pos, boolean val)
	{
		subStates.get(index).set(pos, val);
	}

	private void setDomain(int index, int pos0, boolean val0, int pos1, boolean val1)
	{
		subStates.get(index).set(pos0, val0);
		subCrossovers.get(index).set(pos1, val1);
	}

	public int getSubState(int posLeft)
	{
		// returns -1 if the position is not occupied in any substate
		// returns index of substate if present
		
		for (int i = 0; i < subStates.size(); i++) {

			if (subStates.get(i).get(posLeft)) {

				return i;
			}
		}
		return -1;
	}

	private BitSet makeNewState()
	{
		return new BitSet(design.scaffoldLength);
	}

	private BitSet makeNewCrossover()
	{
		return new BitSet(design.numOfCrossover);
	}

	private void print()
	{
		if (printing) {
			this.doPrint();
		}
	}

	public void doPrint()
	{
		System.out.println("Printing subStates");

		for (BitSet set : subStates) {

			System.out.println(set);

		}
		System.out.println("Done printing subStates");
	}

}
