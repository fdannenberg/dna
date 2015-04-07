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

public class SvgObject
{
	String coordinate;
	String line;
	String id;
	String colour;

	private String value;

	public SvgObject(String ident, String coor, String line2)
	{
		this(ident, coor, line2, null);
	}

	public SvgObject(String ident, String coor, String line2, String colour2)
	{
		this(ident, coor, line2, colour2, "");
	}

	public SvgObject(String ident, String coor, String line2, String colour2, String value2)
	{
		id = ident;
		coordinate = coor;
		line = line2;
		colour = colour2;

		value = value2;
	}

	public void setCoorLine(String coor, String line2)
	{
		coordinate = coor;
		line = line2;
	}

	public String getValue()
	{
		return value;
	}

}
