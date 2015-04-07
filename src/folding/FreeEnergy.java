/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package folding;

public class FreeEnergy
{
	private double dH, dS;

	public FreeEnergy()
	{
		this(0.0, 0.0);
	}

	public FreeEnergy(FreeEnergy other)
	{
		this(other.dH(), other.dS());
	}

	public FreeEnergy(double x, double y)
	{
		dH = x;
		dS = y;
	}

	public void subtract(FreeEnergy other)
	{
		dH = dH - other.dH;
		dS = dS - other.dS;
	}

	public double dH()
	{
		return dH;
	}

	public double dS()
	{
		return dS;
	}

	public void subtract(double x, double y)
	{
		dH = dH - x;
		dS = dS - x;
	}

	public void add(double x, double y)
	{
		dH = dH + x;
		dS = dS + y;
	}

	public void add(FreeEnergy other)
	{
		dH = dH + other.dH();
		dS = dS + other.dS();
	}

	public String toString()
	{
		return "[" + dH + ", " + dS + "]";
	}

	public void printToScreen()
	{
		System.out.println("Free Energy is " + this.toString());
	}

	public boolean equals(FreeEnergy other)
	{
		return (dH == other.dH && dS == other.dS);
	}
}
