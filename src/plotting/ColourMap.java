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

import java.awt.Color;
import folding.Design;
import folding.Util;

public class ColourMap
{
	Design design;

	private String[] staple; // these contain the colours
	private String[] shortStaple; // for seperate staple
	private String[] domain; // colours for domains
	private double[] stapleValue;
	private double[] shortStapleValue;
	private double[] domainValue;

	private String[] colours;
	private double[] values;

	private String name, unit; // 
	private Double top, low;
	private double rounding = 10000.0;

	private Double mid1, mid2, mid3;

	public ColourMap(Design design2, int size)
	{
		design = design2;

		colours = new String[size];
		values = new double[size];
	}

	public ColourMap(Design design2)
	{
		design = design2;

		staple = new String[design.numOfStaples];
		stapleValue = new double[design.numOfStaples];

		shortStaple = new String[design.numOfShortStaples];
		shortStapleValue = new double[design.numOfShortStaples];

		domain = new String[design.simpleScaffoldLength];
		domainValue = new double[design.simpleScaffoldLength];
	}

	public void setName(String name2)
	{
		name = name2;
	}

	public void setLow(double val)
	{
		low = val;
	}

	public void setMid()
	{
		mid2 = (top + low) / 2.0;
		mid1 = (mid2 + low) / 2.0;
		mid3 = (top + mid2) / 2.0;
	}

	public void setMid(double d, double e, double f)
	{
		mid1 = d;
		mid2 = e;
		mid3 = f;
	}

	public void setTop(double val)
	{
		top = val;
	}

	public String getFileName()
	{
		return name + ".svg";
	}

	public String[] getStaple()
	{
		return staple;
	}

	public String[] getShortStaple()
	{
		return shortStaple;
	}

	public String[] getDomain()
	{
		return domain;
	}

	public String getTopUnit()
	{
		String output = Util.limit(top, rounding) + unit;
		return output;
	}

	public String getMidUnit()
	{
		return Util.limit(mid2, rounding) + unit;
	}

	public String getLowUnit()
	{
		return Util.limit(low, rounding) + unit;
	}

	public void setDomainValue(int i, double value)
	{
		domainValue[i] = value;

		String colour = calcColour(value);
		domain[i] = colour;
	}

	public void setShortStapleValue(int i, double value)
	{
		shortStapleValue[i] = value;
		if (design.isDimer()) {
			this.setDomainValue(design.shortToDomain[2 * i], value);
		} else {
			this.setDomainValue(design.shortToDomain[i], value);
		}

		String colour = calcColour(value);
		shortStaple[i] = colour;
	}

	public void setStapleValue(int i, double value)
	{
		stapleValue[i] = value;

		String colour = calcColour(value);
		staple[i] = colour;
	}

	public void setValue(int i, double value)
	{
		values[i] = value;
		colours[i] = calcColour(value);
	}

	public String calcColour(double value)
	{
		double factor;
		double width;

		if (value >= low && value <= top) {

			if (value >= mid1 && value <= mid2) {

				width = Math.abs(mid2 - mid1);
				factor = (value - mid1) / width;

				return Util.interpolate(factor, Constants.midlowCol, Constants.midCol);

			} else if (value >= mid2 && value <= mid3) {

				width = Math.abs(mid3 - mid2);
				factor = (value - mid2) / width;

				return Util.interpolate(factor, Constants.midCol, Constants.topmidCol);

			} else if (value >= mid3 && value <= top) {

				width = Math.abs(top - mid3);
				factor = (value - mid3) / width;

				return Util.interpolate(factor, Constants.topmidCol, Constants.topCol);

			} else {

				width = Math.abs(mid1 - low);
				factor = (value - low) / width;

				return Util.interpolate(factor, Constants.lowCol, Constants.midlowCol);

			}

		} else if (value < low) {

			return "0000CC";

		} else if (value > top) {

			return "CC0000";

		}

		return "-1"; // should not be reached
	}

	public String getName()
	{
		return name;
	}

	public double getValue(int i)
	{
		return stapleValue[i];
	}

	public String[] getColours()
	{
		return colours;
	}

	public double[] getValues()
	{
		return values;
	}

	public void setUnit(String unit2)
	{
		unit = unit2;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setWhite(int i)
	{
		values[i] = -99.0;
		colours[i] = "FFFFFF";
	}

	public void setGrayValue(int i, double value)
	{
		values[i] = value;

		String colour = calcGray(value);
		colours[i] = colour;
	}

	// dedicated grayscale calculator
	public String calcGray(double value)
	{
		double factor;
		double width = Math.abs(top - low);

		if (value >= low && value <= top) {

			factor = (value - low) / width;

			int val = (int) Math.round((1.0 - factor) * 255.0);
			Color color = new Color(val, val, val);

			return Integer.toHexString(color.getRGB()).substring(2, 8);

		} else if (value < low) {

			return "ffffff";

		} else if (value > top) {

			return "000000";

		}

		return "-1"; // should not be reached
	}

}
