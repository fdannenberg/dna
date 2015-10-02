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
import folding.Util;

public class RCommand
{

	public static String getCreateGraphCall(Design design)
	{
		String command = "Rscript  ";
		command = command + Util.workDirectory() + "/RGraphs/dna-graph.R";
		command = command + " --no-restore --no-save ";
		command = command + design.outDir + "   ";
		command = command + (design.stapleCount()) + "   ";
		command = command + design.model.getType() + "/" + design.getDesignString() + "-rate" + design.time.rate + "       ";
		command = command + design.model.getType() + "   ";
		if (design.isDimer()) {
			command = command + 2 + "    ";
		} else {
			command = command + 1 + "    ";
		}
		command = command + " " + design.time.tempStart;
		command = command + " " + design.time.tempEnd;
		command = command + "  " + design.time.rate;
		command = command + "  " + design.getAlsoMelt();

		return command;
	}


	public static String getScatterplotCall(String outDir)
	{
		String scriptDir = Util.workDirectory() + "/RGraphs/scatterplot.R";
		String scatterDir = outDir + "/scatterPlot/";

		String output = "Rscript " + scriptDir;
		output = output + "  " + scatterDir;

		return output;
	}

	public static void execute(String command)
	{
		CommandlineCaller.call(command);
	}

}
