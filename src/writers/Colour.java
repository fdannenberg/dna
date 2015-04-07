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

public class Colour
{
	Design design;

	public String[] colour;
	public String[] colourShort;

	public static final String orange = "orange";
	public static final String yellow = "yellow";
	public static final String blue = "blue";
	public static final String red = "red";
	public static final String green = "green";
	public static final String darkgreen = "darkgreen";
	public static final String gray = "gray";
	public static final String seamColour1 = "black";
	public static final String seamColour2 = "darkgray";
	public static final String shortStapleColour = "purple";
	public static final String standard = "black";

	public Colour(Design design2)
	{
		design = design2;
	}

	public void lazy(int numOfStaple, int numOfShortStaple)
	{

		if (colour == null) {

			colour = new String[numOfStaple];

			for (int i = 0; i < numOfStaple; i++) {

				colour[i] = "black";

			}

		}

		if (colourShort == null) {

			colourShort = new String[numOfShortStaple];

			for (int i = 0; i < numOfShortStaple; i++) {

				colourShort[i] = "black";

			}

		}

	}

	public void setDesign23(int numOfStaples, int numOfShortStaples)
	{
		this.paintAll(numOfStaples, numOfShortStaples, gray);

		this.set(new int[] { 8, 22, 36, 50, 64, 10, 24, 38, 52, 66 }, orange);
		this.set(new int[] { 7, 21, 35, 49, 63, 11, 25, 39, 53, 67 }, red);
		this.set(new int[] { 0, 13, 27, 41, 55, 69, 5, 19, 33, 47, 61, 75 }, green);
		this.set(new int[] { 1, 14, 28, 42, 56, 70, 4, 18, 32, 46, 60, 74 }, darkgreen);
		this.set(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72 }, blue);
	}

	public void setTwoStaple()
	{
		this.paintAll(2, 0, gray);

		this.set(new int[] { 0 }, green);
		this.set(new int[] { 1 }, red);
	}

	private void paintAll(int numOfStaples, int numOfShortStaples, String col)
	{
		colour = new String[numOfStaples];
		colourShort = new String[numOfShortStaples];

		for (int i = 0; i < numOfStaples; i++) {
			colour[i] = col;
		}

		for (int i = 0; i < numOfShortStaples; i++) {
			colourShort[i] = col;
		}
	}

	public void setAll(String col)
	{
		for (int i = 0; i < colour.length; i++) {
			colour[i] = col;
		}
	}

	public void set(int[] fill, String col)
	{
		for (int i = 0; i < fill.length; i++) {
			colour[fill[i]] = col;
		}
	}

	public void removeStaple(int remove)
	{
		String[] newColour = new String[colour.length - 1];

		for (int i = 0; i < remove; i++) {

			newColour[i] = colour[i];

		}

		for (int i = remove; i < colour.length - 1; i++) {

			newColour[i] = colour[i + 1];

		}

		colour = newColour;
	}

	public void addStaple(String col)
	{
		colour = Util.appendStringArray(colour, col);
	}

	public void addShortStaple()
	{
		this.addShortStaple(gray);
	}

	public void addShortStaple(String col)
	{
		colourShort = Util.appendStringArray(colourShort, col);
	}

	public String getColour(int i)
	{
		if (i < colour.length) {

			return colour[i];

		} else {

			return colourShort[i - colour.length];

		}
	}

}
