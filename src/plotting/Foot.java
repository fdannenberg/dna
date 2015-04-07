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

import folding.Util;

public class Foot
{
	String foot;

	public Foot(ColourMap mapping, SvgSettings settings)
	{
		String footPath0 = Util.workDirectory() + settings.svg + "foot0.txt";
		String footPath1 = Util.workDirectory() + settings.svg + "foot1.txt";
		String footPath2 = Util.workDirectory() + settings.svg + "foot2.txt";
		String footPath3 = Util.workDirectory() + settings.svg + "foot3.txt";

		if (settings.writeUnits()) {

			foot = Util.readFile(footPath0) + mapping.getTopUnit();
			foot = foot + Util.readFile(footPath1) + mapping.getLowUnit();
			foot = foot + Util.readFile(footPath2) + mapping.getMidUnit();
			foot = foot + Util.readFile(footPath3);

		} else {

			foot = Util.readFile(footPath0);
			foot = foot + Util.readFile(footPath1);
			foot = foot + Util.readFile(footPath2);
			foot = foot + Util.readFile(footPath3);

		}
	}

}
