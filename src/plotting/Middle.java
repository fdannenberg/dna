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

public class Middle
{
	final String mid0;
	final String mid1;
	final String mid2;
	final String mid3;
	final String mid4;
	final String mid5;

	public Middle(SvgSettings settings)
	{
		mid0 = Util.readFile(Util.workDirectory() + settings.svg + "middle0.txt");
		mid1 = Util.readFile(Util.workDirectory() + settings.svg + "middle1.txt");
		mid2 = Util.readFile(Util.workDirectory() + settings.svg + "middle2.txt");
		mid3 = Util.readFile(Util.workDirectory() + settings.svg + "middle3.txt");
		mid4 = Util.readFile(Util.workDirectory() + settings.svg + "middle4.txt");
		mid5 = Util.readFile(Util.workDirectory() + settings.svg + "middle5.txt");
	}

}
