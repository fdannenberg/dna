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

public class Energy
{
	Design design;
	DesignLoader loader;

	private double[] dH, dS, tM;

	public String[] sequence, domainSeq;
	public EnergyCalc calc;
	public double neighbourCorrectionDH;
	public double neighbourCorrectionDS;

	public Energy(Design design2, DesignLoader loader2)
	{
		design = design2;
		loader = loader2;
		dH = new double[design.scaffoldLength];
		dS = new double[design.scaffoldLength];
		tM = new double[design.scaffoldLength];
		domainSeq = new String[design.scaffoldLength];
		calc = new EnergyCalc(design);

		this.process();
	}

	private void process()
	{
		if (design.useFullSeq()) { // routine if we use the whole scaffold

			int sum = 0;

			for (int i = 0; i < design.scaffoldLength; i++) {

				int length = loader.domainLength[i % design.simpleScaffoldLength];

				String domain = design.getFullSeq().substring(sum, sum + length);

				dH[i] = calc.getDH(domain);
				dS[i] = calc.getDS(domain);
				tM[i] = calc.calcTM(dH[i], dS[i], design.model.getStapleConcentration());
				domainSeq[i] = domain; // this is 3'-5' (anti-convention) orientation

				sum = sum + length;
				sum = sum % design.getFullSeq().length();

			}

		} else { // we are going to make up the dH, dS from averages

			for (int i = 0; i < design.scaffoldLength; i++) {

				int length = loader.domainLength[i % design.simpleScaffoldLength];

				dH[i] = calc.getDH(length);
				dS[i] = calc.getDS(length);
				tM[i] = calc.calcTM(dH[i], dS[i], design.model.getStapleConcentration());

			}

		}

		for (int i = 0; i < design.scaffoldLength; i++) {

			if (tM[i] > 200.0) {

				System.out.println("WARNING: melting temperature >200 C");

			}

		}

		this.setNeighbourCorrection(design.model.helixBonus());
	}

	private void setNeighbourCorrection(double num)
	{
		// setup neighbourCorrection
		neighbourCorrectionDH = -8.2375 * num;
		neighbourCorrectionDS = -22.0875 * num;
	}

	public double getDH(int domain)
	{
		return dH[domain];
	}

	public double getDS(int domain)
	{
		return dS[domain];
	}

	public final double getTM(int domain)
	{
		return tM[domain];
	}

	public String[] getDomainSeq()
	{
		return domainSeq;
	}

	public String getDomainSeq(int i)
	{
		return domainSeq[i];
	}

}
