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

public class Loop
{
	Design design;

	static final int arraySize = 40;
	static final int arraySizeDouble = arraySize * 2;
	int[][] interval;
	int numOfInterval;

	FreeEnergy cost; // cost can only be updated by calling setCost

	public Loop(Design design2)
	{
		this(design2, 0, design2.scaffoldLength);
	}

	public Loop(Design design2, int start, int end)
	{

		design = design2;

		interval = newInterval();

		interval[0][0] = start;
		interval[0][1] = end;
		numOfInterval = 1;

		cost = new FreeEnergy();

	}

	public Loop(Design design2, int[][] setup)
	{
		this(setup);
		design = design2;
	}

	public Loop(int[][] setup)
	{

		interval = newInterval();

		cost = new FreeEnergy();

		for (int i = 0; i < setup.length; i++) {

			interval[i] = setup[i];

		}

		numOfInterval = setup.length;

	}

	public Loop(Loop other)
	{ // copy constructor

		design = other.design;

		interval = newInterval();
		numOfInterval = other.numOfInterval;

		for (int i = 0; i < numOfInterval; i++) {

			interval[i][0] = other.interval[i][0];
			interval[i][1] = other.interval[i][1];

		}

		cost = new FreeEnergy(other.getCost());

	}

	public int[] getInterval(int i)
	{

		return interval[i];

	}

	public FreeEnergy getCost()
	{
		return cost;
	}

	private int[][] newInterval()
	{

		int[][] output = new int[arraySize][2];

		for (int i = 0; i < arraySize; i++) {
			for (int j = 0; j < 2; j++) {
				output[i][j] = -1;
			}
		}

		return output;

	}

	public int getCardinality()
	{
		// Returns how many segments are in the loop
		int output = 0;

		for (int i = 0; i < numOfInterval; i++) {

			output = output + interval[i][1] - interval[i][0];

		}

		return output;
	}

	public int getCardinalityDS(BitSet domain)
	{
		// Returns how many segments are double stranded
		int output = 0;

		for (int i = 0; i < numOfInterval; i++) {

			output = output + domain.get(interval[i][0], interval[i][1]).cardinality();

		}

		return output;
	}

	public int getNumOfCrossoverSegments()
	{

		int output = numOfInterval;

		if (interval[numOfInterval - 1][1] == design.scaffoldLength) {

			output = output - 1;

		}

		return output;

	}

	public void split(int small, int large, Loop newLoop)
	{
		int at = 0;
		int atNewLoop = 0;
		int atNewInterval = 0;
		int[][] newInterval = newInterval();

		while (!contains(interval[at], small)) {

			newInterval[atNewInterval] = interval[at];
			at++;
			atNewInterval++;

		} // assert contains(interval[at],small) is true

		newLoop.interval[atNewLoop][0] = small;

		if (small != interval[at][0]) { // save the first bit of the interval

			newInterval[atNewInterval][0] = interval[at][0];
			newInterval[atNewInterval][1] = small;
			atNewInterval++;

		}

		while (true) { // fail fast by getting stuck

			if (contains(interval[at], large)) {

				newLoop.interval[atNewLoop][1] = large;
				atNewLoop++;

				if (large != interval[at][1]) { // save the remainer of the interval

					newInterval[atNewInterval][0] = large;
					newInterval[atNewInterval][1] = interval[at][1];
					atNewInterval++;

				}
				at++;
				break;

			} else { // assert contains(interval[at], large) == false

				newLoop.interval[atNewLoop][1] = interval[at][1];
				newLoop.interval[atNewLoop + 1][0] = interval[at + 1][0];
				at++;
				atNewLoop++;

			}

			if (at > arraySize || atNewLoop > arraySize) {
				System.out.println("something went wrong - class Loop");
			}

		}

		while (at < numOfInterval) { // remainer intervals

			newInterval[atNewInterval] = interval[at];
			at++;
			atNewInterval++;

		}

		newLoop.numOfInterval = atNewLoop;

		interval = newInterval;
		numOfInterval = atNewInterval;

	}

	public void mergeWith(Loop other)
	{
		int[][] newInterval = this.newInterval();

		int at = 0;
		int atOther = 0;
		int atNewInterval = 0;

		while (true) {

			int valThis = get(interval, at);
			int valOther = get(other.interval, atOther);

			if (valThis < valOther) {

				set(newInterval, atNewInterval, valThis);
				at++;
				atNewInterval++;

			} else if (valThis > valOther) {

				set(newInterval, atNewInterval, valOther);
				atOther++;
				atNewInterval++;

			} else if (valThis != 9999) { // assert valThis == valOther, so just skip over this

				at++;
				atOther++;

			}

			if ((get(interval, at) == 9999) && (get(other.interval, atOther) == 9999)) {

				break;

			}

		}

		interval = newInterval;
		numOfInterval = (atNewInterval / 2);

		if ((atNewInterval % 2) == 1) {

			System.out.println("something went wrong  class Loop");

		}

	}

	private int get(int[][] interval2, int at)
	{

		int x = at / 2;
		int y = at % 2;

		if (at == (arraySizeDouble) || interval2[x][y] == -1) {

			return 9999;

		} else {

			return interval2[x][y];

		}

	}

	private void set(int[][] newInterval, int at, int nextEntry)
	{
		int x = at / 2;
		int y = at % 2;

		newInterval[x][y] = nextEntry;

	}

	public boolean contains(int pos)
	{

		for (int i = 0; i < numOfInterval; i++) {

			if (contains(interval[i], pos)) {
				return true;
			}

		}

		return false;

	}

	private boolean contains(int[] range, int value)
	{

		if ((range[0] <= value) && (value <= range[1])) {

			return true;

		} else {
			return false;
		}
	}

	public String toString()
	{

		String output = "Start loop \n";

		output = output + "numOfInterval=" + numOfInterval + " \n";

		for (int i = 0; i < numOfInterval; i++) {

			output = output + interval[i][0] + " " + interval[i][1] + " \n";

		}

		output = output + "Stop loop";

		return output;

	}

	public void setCost(BitSet domain)
	{
		cost = LoopCalc.getCost(this, domain, design);
	}

}
