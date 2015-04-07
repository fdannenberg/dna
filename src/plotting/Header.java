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

public class Header
{
	String header;

	public Header(SvgSettings settings)
	{
		String headerPath0 = Util.workDirectory() + settings.svg + "header0.txt";
		String headerPath1 = Util.workDirectory() + settings.svg + "header1.txt";
		String headerPath2 = Util.workDirectory() + settings.svg + "header2.txt";
		String headerPath3 = Util.workDirectory() + settings.svg + "header3.txt";
		String headerPath4 = Util.workDirectory() + settings.svg + "header4.txt";
		String headerPath5 = Util.workDirectory() + settings.svg + "header5.txt";

		header = Util.readFile(headerPath0) + Integer.toHexString(Constants.lowCol.getRGB()).substring(2, 8);
		header = header + Util.readFile(headerPath1) + Integer.toHexString(Constants.midlowCol.getRGB()).substring(2, 8);
		header = header + Util.readFile(headerPath2) + Integer.toHexString(Constants.midCol.getRGB()).substring(2, 8);
		header = header + Util.readFile(headerPath3) + Integer.toHexString(Constants.topmidCol.getRGB()).substring(2, 8);
		header = header + Util.readFile(headerPath4) + Integer.toHexString(Constants.topCol.getRGB()).substring(2, 8);
		header = header + Util.readFile(headerPath5) + "\r";
	}

}
