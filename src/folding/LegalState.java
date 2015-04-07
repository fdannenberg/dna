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

public class LegalState
{
	public State state;
	private LegalType type;

	public LegalState(Design design2, BitSet set, String[] type2, int scaffoldLength)
	{
		state = new State(design2);
		state.crossover = set;
		type = new LegalType(type2);
	}

	public LegalState(LegalState legal) // copy constructor
	{
		state = new State(legal.state.design);
		state.crossover = (BitSet) legal.state.crossover.clone();
		type = legal.type;
	}

	public LegalType getType()
	{
		return type;
	}

	public String toString()
	{
		return type.toString() + state.crossover;
	}

	public void setType(String string1, String string2)
	{
		type = new LegalType(new String[] { string1, string2 });
	}

}
