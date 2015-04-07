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

public class StateLoop
{
	private Loops inner, outer;
	Design design;

	public StateLoop(Design design2)
	{
		design = design2;
		inner = new Loops(design2);
		outer = new Loops(design2);
	}

	public StateLoop(StateLoop loop)
	{ // copy constructor
		if (loop != null) {

			design = loop.design;

			inner = new Loops(loop.inner);
			outer = new Loops(loop.outer);

		}
	}

	public void update(int pos, State state)
	{
		inner.update(pos, state);
		outer.update(pos, state);
	}

	public void update(int pos0, boolean val0, int pos1, boolean val1, State state)
	{
		this.update(pos0, state);

		if (design.isInnerStaple(pos1)) {

			inner.update(pos0, val0, pos1, val1, state);

		} else { // outer staple

			outer.update(pos0, val0, pos1, val1, state);

		}
	}

	public boolean testPseudoKnot(int crossover)
	{
		if (design.isInnerStaple(crossover)) {

			return inner.testPseudoKnot(crossover);

		} else { // outer staple

			return outer.testPseudoKnot(crossover);

		}
	}

	public FreeEnergy getUnbindDiff(int pos, State state, double temp)
	{
		FreeEnergy output = new FreeEnergy();
		output.subtract(this.getEnergy());

		state.update(pos, false);
		output.add(this.getEnergy());

		state.update(pos, true);

		return output;
	}

	public FreeEnergy getBindDiffCS(State state, int domain, int crossover)
	{
		FreeEnergy output = new FreeEnergy();
		output.subtract(this.getEnergy());

		state.update(domain, true, crossover, true);
		output.add(this.getEnergy());

		state.update(domain, false, crossover, false);

		return output;
	}

	public FreeEnergy getEnergy()
	{
		FreeEnergy output = new FreeEnergy(); // defaults to zero

		output.add(inner.getEnergy());
		output.add(outer.getEnergy());

		return output;
	}

}
