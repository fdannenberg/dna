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
import folding.State;

public class PerStepStatistic
{
	// captures information at each step of the simulation..
	Design design;
	public History historic;

	public PerStepStatistic(Design design2)
	{
		design = design2;
		historic = new History(design);
	}

	public void update(State state)
	{
		boolean changed;

		for (int i = 0; i < design.numOfStaples; i++) {

			for (int j = 0; j < 4; j++) {

				if (state.crossover.get(4 * i + j)) {

					if (!historic.get(4 * i + j)) {

						changed = historic.set(4 * i + j);

						if (changed) {

							if (j == 0 || j == 3) {

								if (historic.get(i * 4 + 1) && historic.get(i * 4 + 2)) {
									historic.increaseChangeCounter(i);
								}

								historic.clear(i * 4 + 1);
								historic.clear(i * 4 + 2);

							}

							if (j == 1 || j == 2) {

								if (historic.get(i * 4 + 0) && historic.get(i * 4 + 3)) {
									historic.increaseChangeCounter(i);
								}

								historic.clear(i * 4 + 0);
								historic.clear(i * 4 + 3);

							}
						}
					}
				}

			}

		}

	}

	public void merge(PerStepStatistic other)
	{ // merge two statistics
		historic.merge(other.historic);
	}

}
