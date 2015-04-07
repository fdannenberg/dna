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

// 	Rigidity stores and operates nm^{2} contributions of each domain. 
public class Rigidity
{
	Design design;
	public double[] dsCost;
	public double[] ssCost;

	public Rigidity(Design design2)
	{
		design = design2;
	}

	public void generateCost(int[] domainLength, int scaffoldLength, int simpleScaffoldLength)
	{
		// generate SS, DS costs from number of bases per domain.
		dsCost = new double[scaffoldLength];
		ssCost = new double[scaffoldLength];

		if (simpleScaffoldLength == 0) {

			simpleScaffoldLength = scaffoldLength;
		}

		for (int i = 0; i < scaffoldLength; i++) {

			int numOfBases = domainLength[i % simpleScaffoldLength];

			if (design.getDesignString().contains("NoCooperativity")) {

				dsCost[i] = this.calcSsCost(numOfBases);

			} else {

				dsCost[i] = this.calcDsCost(numOfBases);

			}

			ssCost[i] = this.calcSsCost(numOfBases);
		}
	}

	private double calcSsCost(int numOfBases)
	{
		double output = numOfBases * Constant.risePerBaseSS / Constant.kuhnSS;
		output = output * Math.pow(Constant.kuhnSS, 2.0);

		return output;
	}

	private double calcDsCost(int numOfBases)
	{
		double output = numOfBases * Constant.risePerBaseDS;
		output = Math.pow(output, 2.0);

		return output;
	}

}
