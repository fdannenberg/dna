/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package writers;

import folding.Design;
import folding.LegalState;
import folding.State;

public class ContactOrder
{
	Design design;

	public double get(LegalState legalState)
	{
		// returns contact order for this legal state
		return get(legalState.state);
	}

	private double get(State state)
	{
		// returns contact order for this state
		int N = 0; // number of contacts
		int L = design.scaffoldLength; // length of the circular scaffold
		int sum = 0; // sum of distances between contacts 

		for (int i = 0; i < design.numOfCrossover; i++) {

			if (state.crossover.get(i)) { // crossover is used in this state

				sum = sum + this.distance(i);
				N++;
			}

		}

		double output = (1.0 / (N * L)) * sum;

		return output;
	}

	private int distance(int index)
	{
		int left = design.getStaple(index, 1);
		int right = design.getStaple(index, 2);

		int distance = Math.min(Math.abs(left - right), design.scaffoldLength - Math.abs(left - right));

		return distance;
	}
}
