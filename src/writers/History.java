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

import java.util.BitSet;

import folding.Design;

public class History
{
	int[] counts;
	BitSet history;
	BitSet history2;
	Design design;

	public History(Design design2)
	{
		design = design2;
		counts = new int[design.numOfStaples];
		history = new BitSet(design.numOfStaples);
		history2 = new BitSet(design.numOfStaples);

	}

	public boolean get(int i)
	{
		return history.get(i);
	}

	public boolean set(int i)
	{
		if (history2.get(i)) {

			history.set(i);
			return true;

		} else {

			history2.set(i);
			return false;

		}
	}

	public void increaseChangeCounter(int i)
	{
		counts[i]++;
	}

	public void clear(int i)
	{
		history.clear(i);
		history2.clear(i);
	}

	public void merge(History other)
	{
		for (int i = 0; i < counts.length; i++) {

			counts[i] = counts[i] + other.counts[i];

		}
	}

	public int[] getCounts()
	{
		return counts;
	}

}
