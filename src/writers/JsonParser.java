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

public class JsonParser
{

	Design design;

	public JsonParser(Design design2)
	{
		design = design2;
	}

	// make JSON description (first half) as soon as final state occurs
	public String makeInitialJSON(State state)
	{

		int left, right, counter = 0;
		boolean toggle = false;

		String output = " \n { "; // newline

		output += " \t \"code\"            \t:\t \"" + state.toString() + "\" ,\n";
		output += " \t \"scaffoldLength\"  \t:\t \"" + design.scaffoldLength + "\" ,\n";
		output += " \t \"bonds\"           \t: [ \n";

		for (int i = 0; i < design.numOfCrossover; i++) { // this sets the crossoverbonds

			if (state.crossover.get(i)) {

				if (toggle) {
					output += ", \n";
				} else {
					toggle = true;
				}

				left = design.getStaple(i, 1);
				right = design.getStaple(i, 2);

				output += " \t \t \t \t \t { \"left\" : " + left + ", \"right\" : " + right + " }";
			}
		}

		output += " \n \t \t \t \t ], \n";

		output += " \t \"dsSections\"       \t: [ \n";

		for (int i = 0; i < design.scaffoldLength; i++) { // this sets the dualstranded sections

			if (state.domain.get(i)) {

				if (counter > 0) {
					output += ", \n";
				} else {
					counter++;
				}

				left = i;
				right = (i + 1 + design.scaffoldLength) % design.scaffoldLength;
				String colour = "\"" + state.getColourByDomain(left) + "\"";
				int length = design.getDomainLength()[i % design.simpleScaffoldLength];

				output += " \t \t \t \t \t { \"left\" : " + left;
				output += ", \"right\" : " + right;
				output += ", \"colour\" : " + colour;
				output += ", \"dist\" : " + length;
				output += " }";

			}
		}

		output += "\t \t \t \t ], \n";

		return output;
	}

	public String writeFinalJson(String input, String stateName, String[] type, double prob)
	{
		return writeFinalJson(input, stateName, type[0], prob);
	}

	public String writeFinalJson(String input, String stateName, String type, double prob)
	{
		String output;

		output = input;
		output += "\t \"name\"           \t:\t" + "\"" + stateName + "\"  ,\n";
		output += "\t \"type\"           \t:\t" + "\"" + type + "\" ,\n";
		output += "\t \"likelihood\"     \t:\t" + prob + "\n";
		output += "  }";

		return output;
	}

	public String writeJsonSequential(State state, double probability)
	{
		String stateName = state.toString();
		String init = this.makeInitialJSON(state);
		String output = writeFinalJson(init, stateName, "", probability);

		return output;
	}

}
