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

import java.util.ArrayList;

public class RatesGenerator
{

	Design design;
	private RatesCalc calc;
	private Rates rates;

	public RatesGenerator(Design design2)
	{
		design = design2;
		calc = new RatesCalc(design);
		rates = new Rates(design, calc);
	}

	public ArrayList<Transition> rates(State currentState)
	{
		ArrayList<Transition> output = new ArrayList<Transition>();
		rates.rates(currentState, output);

		return output;
	}

	public void setUnbindRate(double time)
	{
		calc.setUnbindRate(time);
	}

	public Legal getLegal()
	{
		return rates.getLegal();
	}

	public double temp()
	{
		return calc.getTemp();
	}

	public Rates getRatesObject()
	{
		return rates;
	}

}
