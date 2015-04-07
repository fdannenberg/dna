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

public class Legal
{

	Design design;
	boolean alwaysLegal;

	public Legal(Design design2)
	{
		design = design2;
		this.setAlwaysLegal();
	}

	private void setAlwaysLegal()
	{
		if (!design.isDimer()) {

			alwaysLegal = true;

		}
		if (design.getDesignString().contains("mock")) {

			alwaysLegal = true;

		}
	}

	public boolean test(State state, int pos, boolean val)
	{
		// returns TRUE if allowed
		if (alwaysLegal) {
			return true;
		}

		if (val) { // adding half-staple

			int posLeft = design.getDomainLeft(pos);
			int posRight = design.getDomainRight(pos);

			int subLeft = state.sub.getSubState(posLeft);
			int subRight = state.sub.getSubState(posRight);

			if (subLeft > -1 && subRight > -1) {

				return testMerge(state, subLeft, subRight);

			} else {

				return true;

			}

		} else { // unbinding is always allowed

			return true;

		}
	}

	public boolean test(State state, int pos0, boolean val0, int pos1, boolean val1)
	{
		if (val1 && design.model.useLoop() && design.isDimer()) {
			// if the crossover creates a pseudoknot, we will not allow this
			// legacy feature for loop model combined with dimer model

			if (isPseudoKnot(state, pos1)) {

				return false;

			}
		}

		if (alwaysLegal) {

			return true;

		}

		if (val1) { // adding a crosslink

			int posLeft = design.getDomainLeft(pos0);
			int posRight = design.getDomainRight(pos0);
			int crossoverConnection = -1;
			int subLeft = state.sub.getSubState(posLeft);
			int subRight = state.sub.getSubState(posRight);

			int[] leftRight = design.crossoverToDomain[pos1];

			if (leftRight[0] == pos0) {

				crossoverConnection = leftRight[1];

			} else if (leftRight[1] == pos0) {

				crossoverConnection = leftRight[0];

			} else {

				System.out.println("Something is wrong in update() for class StateSub");

			}

			int subConnection = state.sub.getSubState(crossoverConnection);

			if (subLeft > -1 && subRight > -1 && subConnection > -1) {

				return test(state, subLeft, subRight, subConnection, pos1, val1);

			} else if (subLeft > -1 && subRight > -1) {

				return test(state, subLeft, subRight, pos1, val1);

			} else if (subLeft > -1 && subConnection > -1) {

				return test(state, subLeft, subConnection, pos1, val1);

			} else if (subRight > -1 && subConnection > -1) {

				return test(state, subRight, subConnection, pos1, val1);

			} else if (subLeft > -1) {

				return test(state, subLeft, pos1, val1);

			} else if (subRight > -1) {

				return test(state, subRight, pos1, val1);

			} else if (subConnection > -1) {

				return test(state, subConnection, pos1, val1);

			} else {

				System.out.println("Something is wrong in StateSub Code A");

			}

			return true;

		} else { // removing a crosslink, always allowed	

			return true;

		}
	}

	private boolean isPseudoKnot(State state, int pos1)
	{
		return state.loop.testPseudoKnot(pos1);
	}

	private boolean test(State state, int index, int pos1, boolean val1)
	{
		BitSet testState = (BitSet) state.sub.subCrossovers.get(index).clone();
		testState.set(pos1, val1);

		return testState(testState);
	}

	private boolean test(State state, int index1, int index2, int pos1, boolean val1)
	{
		BitSet testState = (BitSet) state.sub.subCrossovers.get(index1).clone();
		testState.or(state.sub.subCrossovers.get(index2));
		testState.set(pos1, val1);

		return testState(testState);
	}

	private boolean test(State state, int index1, int index2, int index3, int pos1, boolean val1)
	{
		BitSet testState = (BitSet) state.sub.subCrossovers.get(index1).clone();
		testState.or(state.sub.subCrossovers.get(index2));
		testState.or(state.sub.subCrossovers.get(index3));
		testState.set(pos1, val1);

		return testState(testState);
	}

	private boolean testMerge(State state, int index0, int index1)
	{
		// returns TRUE if the merge is a subset of legal states
		BitSet testState = (BitSet) state.sub.subCrossovers.get(index0).clone();
		testState.or(state.sub.subCrossovers.get(index1));

		return testState(testState);
	}

	public boolean testState(BitSet state)
	{
		//  return TRUE if state is subset of a legal state
		for (LegalState legal : design.getLegalStates()) {

			if (isSubSet(state, legal)) {

				return true;

			}

		}

		return false;
	}

	public LegalType getType(State state)
	{
		for (LegalState legal : design.getLegalStates()) {

			if (isFullSubSet(state, legal)) {

				return legal.getType();

			}

		}

		return design.noType;
	}

	private boolean isSubSet(BitSet crossovers, LegalState legal)
	{
		// return TRUE if state is subset of LEGAL
		BitSet crossoversClone = (BitSet) crossovers.clone();
		int val = crossoversClone.cardinality();

		crossoversClone.and(legal.state.crossover);

		if (crossoversClone.cardinality() == val) {

			return true;

		}

		return false;
	}

	private boolean isFullSubSet(State state, LegalState legal)
	{
		BitSet stateClone = (BitSet) state.crossover.clone();

		stateClone.and(legal.state.crossover);

		if (stateClone.cardinality() == legal.state.crossover.cardinality()) {

			return true;

		}

		return false;
	}

}
