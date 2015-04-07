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

public class Transition
{
	// Class that stores the target state and the rate to this target state.
	private double rate;
	private NextStateInterface nextState;
	private int reactionType;

	public Transition(NextStateInterface state2, double b, int type2)
	{
		nextState = state2;
		rate = b;
		reactionType = type2;
	}

	public NextStateInterface getNextState()
	{
		return nextState;
	}

	public double getRate()
	{
		return rate;
	}

	public String toString()
	{
		return "(" + nextState.toString() + ")  rate is  " + rate;
	}

	public int getType()
	{
		return reactionType;
	}

	public State update(State currentState)
	{
		return nextState.getState(currentState);
	}

}
