/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package plotting;

import java.text.DecimalFormat;

public class SvgSettings
{
	public String type;
	public String svg;
	public String out;
	public boolean useStaples = true; // if false, use domain data for melting temperature plots

	private String selection;
	private int typeCounts;
	private boolean useAbstractValues = false;

	public SvgSettings()
	{

	}

	public SvgSettings(String type, String selection2)
	{
		this(type, selection2, 0.0);
	}

	public SvgSettings(String type2, String selection2, double temp2)
	{
		type = type2;
		selection = selection2;

		DecimalFormat df = new DecimalFormat("#.###");

		if (type == null) {

		} else if (type.contains("staples")) {

			svg = "/RGraphs/svg/";
			out = "/heatMap-staples/" + selection + "/";
			useStaples = true;

		} else if (type.contains("domains")) {

			svg = "/RGraphs/svg-domains/";
			out = "/heatMap-domains/" + selection + "/";
			useStaples = false;

		} else if (type.equals("seamCorrelation2")) {

			svg = "/RGraphs/svg-corr/";
			out = "/heatMap-seamCorrelation/" + df.format(temp2) + "/";
			useStaples = false;

		} else if (type.equals("contactMap")) {

			svg = "/RGraphs/svg-contactMap/";
			out = "/contactMap/" + df.format(temp2) + "/";
			useStaples = false;
			useAbstractValues = true;

		}
	}

	public String getSelection()
	{
		return selection;
	}

	public void setCounts(int count)
	{
		// sets number of paths observed to have the selected type
		typeCounts = count;
	}

	public int getCounts()
	{
		return typeCounts;
	}

	public boolean writeUnits()
	{
		return (type.contains("domains") || type.contains("staples"));
	}

	public boolean isCorrelationMap()
	{
		return (type.contains("seamCorrelation"));
	}

	public boolean useAbstractValues()
	{
		return (useAbstractValues);
	}

}
