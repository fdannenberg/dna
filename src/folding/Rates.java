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

public class Rates implements RatesInterface
{
	private Design design;
	private RatesCalc calc;
	private ArrayList<Transition> transitions;
	private Legal legal;
	private State state;
	public boolean printing = false;

	public Rates(Design design2, RatesCalc calc2)
	{
		design = design2;
		legal = new Legal(design);

		calc = calc2;
	}

	@Override
	public ArrayList<Transition> rates(State state2, ArrayList<Transition> input)
	{
		transitions = input;
		state = state2;
		this.rates();

		return transitions;
	}

	private void rates()
	{
		this.bindHalfBound(); // type 2
		this.unbindDomains(); // type 1
		this.bindCrossover(); // type 0
	}

	private void bindCrossover()
	{
		for (int i = 0; i < design.scaffoldLength; i++) {

			if (state.halfBound.get(i)) { // there is a half-bound staple

				ArrayList<Integer> possibleCrossover = design.domainToCrossover.get(i);

				for (Integer pos : possibleCrossover) { // check for each possible crossover if the other domain is unbound

					int oppositeSite;

					if (i == design.crossoverToDomain[pos][0]) {
						oppositeSite = design.crossoverToDomain[pos][1];
					} else {
						oppositeSite = design.crossoverToDomain[pos][0];
					}

					if (!state.domain.get(oppositeSite)) { // check if the other domain is free

						double rate = calc.crossoverRate(state, design.getStaple(pos, 1), design.getStaple(pos, 2), oppositeSite, pos);

						this.addTransition(oppositeSite, true, pos, true, rate, 0);

					}

				}

			}

		}

	}

	private void unbindDomains()
	{
		for (int i = 0; i < design.scaffoldLength; i++) {

			if (state.halfBound.get(i)) { // domain is halfbound

				double rate = calc.getUnbindRate(i, state);
				this.addTransition(i, false, rate, 1);

			}

		}

		for (int i = 0; i < design.numOfCrossover; i++) {

			if (state.crossover.get(i)) { // crossover is in place, so let it unbind at either side

				int[] pos = design.crossoverToDomain[i];

				double rate1 = calc.getUnbindRateCS(pos[1], state);
				double rate0 = calc.getUnbindRateCS(pos[0], state);

				this.addTransition(pos[0], false, i, false, rate0, 1);
				this.addTransition(pos[1], false, i, false, rate1, 1);

			}

		}

	}

	private void bindHalfBound()
	{
		for (int i = 0; i < design.scaffoldLength; i++) {

			if (design.usableDomains.get(i)) {

				if (!state.domain.get(i)) { // domain is empty

					this.addTransition(i, true, calc.getBindRate(), 2);

				}

			}

		}

	}

	private void addTransition(int pos, boolean val, double rate, int type)
	{
		if (legal.test(state, pos, val)) {

			NextStateInterface nextState = new NextState(pos, val);
			addTransition(transitions, nextState, rate, type);

			if (printing) {
				System.out.println("found Legal state");
			}

		} else {

			if (printing) {
				System.out.println("found Illigal state");
			}

		}
	}

	private void addTransition(int pos0, boolean val0, int pos1, boolean val1, double rate, int type)
	{
		if (legal.test(state, pos0, val0, pos1, val1)) {

			NextStateInterface nextState = new NextState(pos0, val0, pos1, val1);
			addTransition(transitions, nextState, rate, type);

			if (printing) {
				System.out.println("found Legal state");
			}

		} else {

			if (printing) {
				System.out.println("found Illigal state");
			}

		}

	}

	public Legal getLegal()
	{
		return legal;
	}

	public final static void addTransition(ArrayList<Transition> list, NextStateInterface nextState, double rate, int type)
	{
		Transition transition = new Transition(nextState, rate, type);
		list.add(transition);
	}

	public ArrayList<Transition> ratesReactions(State state2, ArrayList<Transition> input)
	{
		transitions = input;
		state = state2;

		this.rates();

		return transitions;
	}

	public final ArrayList<Transition> getCrossoverRates(State other)
	{
		state = other;
		transitions = new ArrayList<Transition>();
		this.bindCrossover();

		return transitions;
	}

}
