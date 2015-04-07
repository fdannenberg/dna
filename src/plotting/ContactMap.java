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
import java.util.BitSet;

import writers.Result;
import folding.Design;
import folding.LegalType;
import folding.PathMetrics;

public class ContactMap
{
	Design design;
	Result result;
	SvgCreator svgCreator;

	public ContactMap(Design design2, Result result2)
	{
		design = design2;
		result = result2;
		svgCreator = new SvgCreator(design2);
	}

	public void write(double temperature)
	{
		this.makeContactMap(temperature, "contactMap", LegalType.all);
		this.makeContactMap(temperature, "contactMap", LegalType.fiveZero);
		this.makeContactMap(temperature, "contactMap", LegalType.fourOne);
		this.makeContactMap(temperature, "contactMap", LegalType.threeTwo);
		this.makeContactMap(temperature, "contactMap", LegalType.MCType);
		this.makeContactMap(temperature, "contactMap", LegalType.noType);
	}

	private void makeContactMap(double temperature, String name, String selection)
	{
		SvgSettings settings = new SvgSettings(name, selection, temperature);
		int gridSize = design.scaffoldLength * design.scaffoldLength;

		int timePoint = design.time.timeFromTemp(temperature);
		design.getPlottingData().init(gridSize);

		ColourMap map = this.makeMapping(settings, gridSize);
		map.setLow(0.0);
		map.setTop(1.0);
		map.setMid();
		map.setName(selection);

		// basic map
		ArrayList<SvgObject> svgObjects = new ArrayList<SvgObject>();

		for (int i = 0; i < design.scaffoldLength; i++) {

			for (int j = 0; j < design.scaffoldLength; j++) {

				if (i == j) {

					String posX = Double.toString(200 + 10 * i);
					String posY = Double.toString(200 + 10 * j);

					SvgObject obj = new SvgObject(Integer.toString(i), posX, posY, map.calcGray(1.0));
					svgObjects.add(obj);

				}

			}

		}
		// manually put top right, bottom right joins.
		SvgObject obj1 = new SvgObject(Integer.toString(design.scaffoldLength), Double.toString(200 + 10 * 0),
				Double.toString(200 + 10 * design.scaffoldLength), map.calcGray(1.0));
		SvgObject obj2 = new SvgObject(Integer.toString(design.scaffoldLength), Double.toString(200 + 10 * design.scaffoldLength), Double.toString(200.0),
				map.calcGray(1.0));
		svgObjects.add(obj1);
		svgObjects.add(obj2);

		int matchingPaths = 0;
		int[] counts = new int[design.numOfCrossover];

		for (PathMetrics path : result.getPathMetricsList()) {

			if (selection.equals(LegalType.all) | path.getType().equals(selection)) {

				matchingPaths++;

				BitSet crossover = path.getCrossover(timePoint);

				for (int j = 0; j < design.numOfCrossover; j++) {

					if (crossover.get(j)) {

						counts[j]++;

					}

				}

			}
		}

		for (int i = 0; i < design.numOfCrossover; i++) {

			if (matchingPaths > 0) {

				int left = design.crossoverToDomain[i][0];
				int right = design.crossoverToDomain[i][1];

				String posX = Double.toString(200 + 10 * left);
				String posY = Double.toString(200 + 10 * right);

				double value = counts[i] / (double) (matchingPaths);

				String colour = map.calcGray(value);

				SvgObject object1 = new SvgObject(Integer.toString(i), posX, posY, colour, Integer.toString(left) + " " + Integer.toString(right) + " "
						+ Double.toString(value));
				SvgObject object2 = new SvgObject(Integer.toString(i), posY, posX, colour);

				svgObjects.add(object1);
				svgObjects.add(object2);
			}

		}

		svgCreator.createGraphics(svgObjects, settings, map.getName());
	}

	private ColourMap makeMapping(SvgSettings settings, int i)
	{
		ColourMap mapping = new ColourMap(design, i);
		return mapping;
	}

}
