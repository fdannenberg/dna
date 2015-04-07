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

public class EnergyCalc
{
	Design design;

	// CONSTANTS AS IN SANTALUCIA, HICKS 2004 
	private final static double DHAverage = (-2.0 * 7.6 - 7.2 - 7.2 - 2.0 * 8.5 - 2.0 * 8.4 - 2.0 * 7.8 - 2.0 * 8.2 - 10.6 - 9.8 - 2.0 * 8.0) / 16.0;
	private final static double DSAverage = (-2.0 * 21.3 - 20.4 - 21.3 - 2.0 * 22.7 - 2.0 * 22.4 - 2.0 * 21.0 - 2.0 * 22.2 - 27.2 - 24.4 - 2.0 * 19.9) / 16.0;

	private final static double[] AA = { -7.6, -21.3 };
	private final static double[] AT = { -7.2, -20.4 };
	private final static double[] TA = { -7.2, -21.3 };
	private final static double[] CA = { -8.5, -22.7 };
	private final static double[] GT = { -8.4, -22.4 };
	private final static double[] CT = { -7.8, -21.0 };
	private final static double[] GA = { -8.2, -22.2 };
	private final static double[] CG = { -10.6, -27.2 };
	private final static double[] GC = { -9.8, -24.4 };
	private final static double[] GG = { -8.0, -19.9 };

	private final static double[] init = { 0.2, -5.7 };
	private final static double[] termAT = { 2.2, 6.9 };
	private final static double[] termCG = { 0.0, 0.0 };

	private final static double saltCorrection = 0.368 * Math.log(0.5 * Constant.concentrationTris + 3.3 * Math.pow(Constant.concentrationMagnesium, 0.5));

	public EnergyCalc(Design design2)
	{
		design = design2;
	}

	public double getDH(int length) // return average value of energy according to length of domain
	{
		double output = ((length - 1) * DHAverage) + init[0] + termAT[0] + termCG[0]; //	(including  initiation constant, including AT penalty)		
		return output;
	}

	public double getDS(int length) // return average value of energy according to length of domain (domain length is number of bases)
	{
		double output = ((length - 1) * DSAverage) + init[1] + termAT[1] + termCG[1]; // including intitial contstant, including AT penalty
		output = output + (length - 1) * saltCorrection;

		return output;
	}

	public double getDH(String domain)
	{
		double[] dHdS = this.getEnergiesR(domain);
		return dHdS[0];
	}

	public double getDS(String domain)
	{
		double[] dHdS = this.getEnergiesR(domain);
		return dHdS[1];
	}

	// domain is in the 3' to 5' orientation (reverse)
	private double[] getEnergiesR(String domain)
	{
		int length = domain.length();
		domain = Util.reverse(domain); // domain is now  5' to 3' orientation.		
		double[] output = { 0.0, 0.0 };
		Util.add(output, selectInitial());

		for (int i = 0; i < (length - 1); i++) {

			String sub = domain.substring(i, i + 2);
			double[] energies = selectEnergies(sub);
			Util.add(output, energies);

		}

		double[] terminal = getTerminal(domain.charAt(length - 1));
		Util.add(output, terminal);
		terminal = getTerminal(domain.charAt(0));
		Util.add(output, terminal);

		// salt correction
		output[1] = output[1] + (domain.length() - 1) * saltCorrection;

		return output;
	}

	private double[] getTerminal(char input)
	{
		if (input == "A".charAt(0) || input == "T".charAt(0)) {

			return termAT;

		} else {

			return termCG;

		}
	}

	private double[] selectEnergies(String sub)
	{
		if (sub.equals("AA") || sub.equals("TT")) {

			return AA;

		} else if (sub.equals("AT")) {

			return AT;

		} else if (sub.equals("TA")) {

			return TA;

		} else if (sub.equals("CA") || sub.equals("TG")) {

			return CA;

		} else if (sub.equals("GT") || sub.equals("AC")) {

			return GT;

		} else if (sub.equals("CT") || sub.equals("AG")) {

			return CT;

		} else if (sub.equals("GA") || sub.equals("TC")) {

			return GA;

		} else if (sub.equals("CG")) {

			return CG;

		} else if (sub.equals("GC")) {

			return GC;

		} else if (sub.equals("GG") || sub.equals("CC")) {

			return GG;

		}

		return null;
	}

	private double[] selectInitial()
	{
		return init;
	}

	public static double calcG37(double dH, double dS)
	{
		double output = 1000.0 * dH - (273.15 + 37.0) * dS;
		return (output / 1000.0);
	}

	public static double calcG60(double dH, double dS)
	{
		double output = 1000.0 * dH - (273.15 + 60.0) * dS;
		return (output / 1000.0);
	}

	public double calcTM(double dH, double dS, double stapleConcentration)
	{
		return ((dH * 1000.0 / (dS + Constant.kb * Math.log((stapleConcentration) / 1.0))) - 273.15);
	}

}
