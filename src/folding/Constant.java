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

public class Constant
{
	public final static double kb = 1.9872; //	Boltzman constant cal /( mol K ) (in fact gas constant)
	public final static double kuhnSS = 1.8; // nm
	public final static double risePerBaseDS = 0.34; // nm
	public final static double risePerBaseSS = 0.6; // nm
	public final static double numSSperDomainSS = 5.333333; // hack  ( 16 * 0.6 / 1.8 ) 
	public final static double ssCostSingleSegment = Math.pow(kuhnSS, 2.0);
	public final static double csCost = ssCostSingleSegment;

	public final static double concentrationTris = 40.0E-3; // Molar (M) (here 40 mM)
	public final static double concentrationMagnesium = 12.5E-3; // Molar (M) (here 12.5 mM)

	// switch strings
	public final static String modelDistance = "distance";
	public final static String modelLoop = "loop";
	public static final String doubleCGamma = "doubleCGamma";
	public static final String halfCGamma = "halfCGamma";

	public static String modelVersionOne = "M1";
	public static String modelVersionTwo = "M2";
	public static String modelVersionThree = "M3";

	public static String helixBonus1 = "HelixBonus1";
	public static String helixBonus2 = "HelixBonus2";
	public static String helixBonus3 = "HelixBonus3";
	public static String helixBonus4 = "HelixBonus4";
	public static String helixBonus5 = "HelixBonus5";
	public static String helixBonus6 = "HelixBonus6";

	// steepness is measured between 20% and 80% domain occupancy 
	public static double width_lower = 0.2;
	public static double width_upper = 0.8;
}
