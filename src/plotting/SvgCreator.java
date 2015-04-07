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

import java.util.ArrayList;

import writers.ListWriter;
import folding.Design;

public class SvgCreator
{
	Design design;
	PlottingData data;
	private ListWriter listWriter;

	public SvgCreator(Design design2)
	{
		design = design2;
		data = design.getPlottingData();
	}

	public void createGraphics(ColourMap mapping, SvgSettings settings)
	{
		listWriter = new ListWriter(design.outDir + settings.out, mapping.getFileName());
		listWriter.addWriter("values-" + mapping.getName() + ".txt");
		listWriter.writeToFile(new Header(settings).header);

		this.writeMiddle(mapping, settings);

		listWriter.writeToFile(new Foot(mapping, settings).foot);
		listWriter.close();
	}

	public void createGraphics(ArrayList<SvgObject> svg, SvgSettings settings, String fileName)
	{
		listWriter = new ListWriter(design.outDir + settings.out, fileName);
		listWriter.addWriter("values-" + fileName);
		listWriter.writeToFile(new Header(settings).header);

		Middle middle = new Middle(settings);
		this.writeData(svg, middle);

		listWriter.writeToFile(new Foot(null, settings).foot);
	}

	private void writeMiddle(ColourMap mapping, SvgSettings settings)
	{
		Middle middle = new Middle(settings);

		if (settings.useStaples) {
			this.writeColours(middle, data.staples, mapping.getStaple());
			this.writeColours(middle, data.shortStaples, mapping.getShortStaple());
		} else if (settings.isCorrelationMap()) {
			this.writeColours(middle, data.seamCorrelation, mapping.getColours());
			this.writeValues(mapping.getValues());
		} else if (settings.useAbstractValues()) {
			this.writeColours(middle, data.getCustomData(), mapping.getColours());
		} else {
			this.writeColours(middle, data.domains, mapping.getDomain());
		}

	}

	private void writeColours(Middle middle, SvgObject[] svg, String[] mapping)
	{
		for (int i = 0; i < mapping.length; i++) {// staples.length; i++) {

			if (svg[i] != null) {

				this.write(i, svg[i], mapping[i], middle);

			}
		}
	}

	private void writeValues(double[] values)
	{
		for (int i = 0; i < values.length; i++) {

			listWriter.writeToFile(Double.toString(values[i]), 1);

		}
	}

	private void writeValue(SvgObject svg)
	{
		String output = svg.getValue();

		if (!output.equals("")) {

			listWriter.writeToFile(output, 1);

		}
	}

	private void writeData(ArrayList<SvgObject> svg, Middle middle)
	{
		int i = 0;
		for (SvgObject entry : svg) {

			this.write(i, entry, entry.colour, middle);
			this.writeValue(svg.get(i));
			i++;

		}
	}

	private void write(int i, SvgObject svg, String mapping, Middle middle)
	{
		String identifier = Integer.toString(i + 1000);

		String output = "" + middle.mid0 + identifier;
		output += middle.mid1 + svg.coordinate;
		output += middle.mid2 + svg.line;
		output += middle.mid3 + mapping;
		output += middle.mid4;
		output += middle.mid5;

		listWriter.writeToFile(output);
	}

	public void close()
	{
		if (listWriter != null) {
			listWriter.close();
		}
	}
}
