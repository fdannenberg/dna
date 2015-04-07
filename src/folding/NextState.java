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

public class NextState implements NextStateInterface
{

	private int pos0, pos1; // first position is for domain, second position is for crossover
	private boolean val0, val1; // new values
	private boolean crossover; // true if we are removing or adding crossovers. Otherwise only the first pos,val is to be used. 

	public NextState(int pos, boolean val)
	{
		pos0 = pos;
		val0 = val;
		crossover = false;
	}

	public NextState(int posA, boolean valA, int posB, boolean valB)
	{
		pos0 = posA;
		val0 = valA;
		pos1 = posB;
		val1 = valB;
		crossover = true; // only true if we update a crossover
	}

	public State getState(State state)
	{
		// update state with all the changes	
		if (crossover) {

			state.update(pos0, val0, pos1, val1);

		} else { // not updating crossover

			state.update(pos0, val0);

		}

		return state;
	}

	public String toString()
	{
		return ("cs (" + crossover + ")  pos  (" + pos0 + "," + pos1 + ")  val (" + val0 + "," + val1 + ") ");
	}

	public boolean crossover()
	{
		return crossover;
	}

	public int pos0()
	{
		return pos0;
	}

	public int pos1()
	{
		return pos1;
	}

	public boolean val0()
	{
		return val0;
	}

	public boolean val1()
	{
		return val1;
	}

	public State getStateUndo(State state)
	{
		if (!crossover) {

			state.update(pos0, !val0);

		} else {

			state.update(pos0, !val0, pos1, !val1);

		}

		return state;
	}

}
