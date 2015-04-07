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
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import writers.Colour;

public class DesignLoader
{
	Colour colour;
	plotting.PlottingData plottingData;

	Design design;
	String designString;

	int scaffoldLength, numOfCrossover;
	int[][] staple, shortStaple;
	private BitSet innerStaple;
	boolean dimer;
	public double[] initialConcentrationStaple;
	Map<Integer, ArrayList<Integer>> seam;
	public ArrayList<LegalState> legalStates;

	// nucleotide-sequence related objects
	public boolean useFullSeq;
	public int[] domainLength;
	public String fullSeq;
	public String[] domainSeq;

	// printing objects
	public int[] fluorStaples = {};
	public int[] quenchStaples = {};
	public int[] xStaples = {};

	public DesignLoader(String designString2, Design design2)
	{
		design = design2;
		designString = designString2;
		plottingData = new plotting.PlottingData(design);

		init();
		loadDesign(designString);
		colour.lazy(staple.length, shortStaple.length);

		this.lazyFluor();
		this.lazyDomainLength();
		plottingData.finalize();
	}

	private void lazyFluor()
	{
		if (fluorStaples.length == 0) {

			int[] fluorStaplesCopy = { 0 };
			fluorStaples = fluorStaplesCopy;

		}
	}

	private void init()
	{
		staple = new int[][] {};
		shortStaple = new int[][] {};
		innerStaple = new BitSet();
		legalStates = new ArrayList<LegalState>();
		colour = new Colour(design);
	}

	private void loadDesign(String design)
	{

		// DESIGN 23e -- full size DIMER /w short and long staples
		if (design.equals("design23e")) {
			this.setDesign23e();
		}

		// -- half of each seam removed
		if (design.equals("design23eHalfSeam")) {
			this.setDesign23eHalfSeam();
		}

		if (design.equals("design23eReinforceSeamV3")) {
			this.loadDesign23eReinforceSeamV3();
		}

		if (design.equals("design23eAlternativeSeam")) {
			this.loadDesign23eAlternativeSeam();
		}

		if (design.equals("design23eLowerRight")) {

			this.setDesign23e();
			this.removeStapleNoSingleDomainStaple(75);
			this.removeStapleNoSingleDomainStaple(68);
			this.removeShortStaple(160, 161);
			this.addShortStaple(146, 147);
			this.add64Staple(157, 158, 159, 160);

			legalStates.get(6).setType("D", "5:0");

			plottingData.setDesign23eLowerRight();
		}

		// energy toggle 
		if (design.equals("design23eEnergy")) {
			useFullSeq = true;
			this.setDesign23e();
		}

		if (design.equals("design23eHalfSeamEnergy")) {
			useFullSeq = true;
			this.setDesign23eHalfSeam();
		}

		if (design.equals("design23eReinforceSeamV3Energy")) {
			useFullSeq = true;
			this.loadDesign23eReinforceSeamV3();

		}

		if (design.equals("design23eAlternativeSeamEnergy")) {
			useFullSeq = true;
			this.loadDesign23eAlternativeSeam();
		}

		if (design.equals("design23eLowerRightEnergy")) {
			useFullSeq = true;

			this.setDesign23e();
			this.removeStapleNoSingleDomainStaple(75);
			this.removeStapleNoSingleDomainStaple(68);
			this.removeShortStaple(160, 161);
			this.addShortStaple(146, 147);
			this.add64Staple(157, 158, 159, 160);

			legalStates.get(6).setType("D", "5:0");

			plottingData.setDesign23eLowerRight();
		}

		//MONOMERS
		if (design.equals("design23eMonomer")) {
			this.setDesign23e();
			dimer = false;
		}

		if (design.equals("design23eMonomerEnergy")) {
			useFullSeq = true;
			this.setDesign23e();
			dimer = false;
		}

		if (design.equals("design23eMonomerEnergyNoCooperativity")) {
			useFullSeq = true;
			this.setDesign23e();
			dimer = false;
		}

		if (design.equals("mono-remove-X1")) {
			useFullSeq = true;
			this.setDesign23e();
			dimer = false;
			this.removeStapleNoSingleDomainStaple(new int[] { 60, 54, 53, 40, 39, 32 });
		}

		if (design.equals("mono-remove-X2")) {
			useFullSeq = true;
			this.setDesign23e();
			dimer = false;
			this.removeStapleNoSingleDomainStaple(new int[] { 56, 49, 48, 35, 34, 28 });
		}

		// mock designs
		if (design.equals("mock0")) {
			this.setMock0();
		}

		if (design.equals("mock1")) {
			this.setMock1();
		}

		if (design.equals("mock2")) {
			scaffoldLength = 32;
			dimer = false;

			int[][] stapleCopy = { { 7, 6, 10, 9 }, { 4, 3, 13, 12 } };

			staple = stapleCopy;
		}

		if (design.equals("mock3")) {
			scaffoldLength = 10;
			dimer = false;

			int[][] stapleCopy = { { 1, 2, 8, 9 }, { 3, 4, 6, 7 } };

			staple = stapleCopy;
		}

		// mock4 -- mock design with regular, seam, long and short staples
		if (design.equals("mock4")) {
			this.setMock4();
		}

		// mock5 -- mock4 as dimer
		if (design.equals("mock5")) {
			this.setMock4();
			dimer = true;
		}

		if (design.equals("mock5e")) {
			dimer = false;
			scaffoldLength = 24;

			int[][] stapleCopy = { { 7, 6, 18, 17 }, // S0
					{ 5, 6, 18, 19 }, // S1
					// { 1, 0, 0, 23 }, //S2
					{ 9, 10, 8, 9 }, // S3
			};

			staple = stapleCopy;

			int[][] shortStapleCopy = { { 1, 2 }, // Short1
					{ 22, 23 }, // Short2
			};

			shortStaple = shortStapleCopy;
		}

		// mock11 -- mock dimer design
		// standard origami but with only seam staples.
		if (design.equals("mock11")) {
			this.setMock11();
		}

		if (shortStaple == null) {
			int[][] shortStaplesCopy = {};
			shortStaple = shortStaplesCopy;
		}

	}

	private void loadDesign23eReinforceSeamV3()
	{
		this.setDesign23e();

		// modify the plotting layout first..
		this.removeStapleNoSingleDomainStaple(new int[] { 67, 63, 53, 49, 25, 21, 11, 7 });
		this.removeStaple(44 - 4);

		this.addShortStaple(52 - 1, 53 - 1);
		this.addShortStaple(115 - 2, 116 - 2);
		this.addShortStaple(31 - 1, 32 - 1);
		this.addShortStaple(136 - 2, 137 - 2);

		this.removeShortStaple(80 - 1, 81 - 1);
		this.removeShortStaple(87 - 2, 88 - 2);
		this.removeShortStaple(165 - 2, 164 - 2);
		this.removeShortStaple(3 - 1, 4 - 1);

		this.mergeDomainUp(162);
		this.mergeDomainDown(155);
		this.mergeDomainUp(148);
		this.mergeDomainDown(141);
		this.mergeDomainUp(106);
		this.mergeDomainDown(99);
		this.mergeDomainUp(92);

		this.mergeDomainDown(85);
		this.mergeDomainUp(79);

		this.mergeDomainDown(72);
		this.mergeDomainUp(65);
		this.mergeDomainDown(58);
		this.mergeDomainUp(23);
		this.mergeDomainDown(16);
		this.mergeDomainUp(9);

		this.mergeDomainDown(2);

		plottingData.setDesign23ReinforceSeamV3Domains();

	}

	private void removeShortStaple(int left, int right)
	{
		int removeIndex = Util.findMatch(shortStaple, left, right);
		plottingData.shortStaples = Util.remove(plottingData.shortStaples, removeIndex);

		shortStaple = Util.removeShortStaple(shortStaple, left, right);
	}

	private void add32Staple(int i, int j)
	{
		// add short staple at location i;
		// merge domain j onto domain i.

		this.addShortStaple(i, i + 1);
		this.mergeDomainDown(j);
	}

	private void add64Staple(int i, int j, int k, int l)
	{
		// add short staple at location i;

		// merge domain l onto domain k,
		// merge domain k onto domain j,
		// merge domain j onto domain i.

		this.addShortStaple(i, i + 1);

		this.mergeDomainDown(l);
		this.mergeDomainDown(k);
		this.mergeDomainDown(j);
	}

	private void mergeDomainDown(int m) // merge domain m onto m-1
	{
		this.removeDomain(m);

		domainLength = Util.mergeDomainDown(domainLength, m);
		scaffoldLength = scaffoldLength - 1;
	}

	private void mergeDomainUp(int m) // merge domain m onto m+1
	{
		this.removeDomain(m);

		domainLength = Util.mergeDomainUp(domainLength, m);
		scaffoldLength = scaffoldLength - 1;
	}

	private void loadDesign23eAlternativeSeam()
	{
		this.setDesign23e();

		this.addStaple(146 - 2, 147 - 2, 119 - 2, 118 - 2); // adding staple s76
		plottingData.addStaple("507.5713,316.6768", "M 0,0 66.141,0");
		this.addStaple(148 - 2, 147 - 2, 119 - 2, 120 - 2); // adding staple s77
		plottingData.addStaple("507.5713,330.0000", "M 0,0 66.141,0");

		this.restoreToLegalStates(18, 76);
		this.restoreToLegalStates(18, 77);

		numOfCrossover = 4 * staple.length;

		this.removeStaple(72);
		this.removeStaple(58);
		this.removeStaple(44);
		this.removeStaple(30);
		this.removeStaple(16);

		plottingData.addShortStapleInSeam();

		this.removeStapleNoSingleDomainStaple(57);
		this.removeStapleNoSingleDomainStaple(44);
		this.removeStapleNoSingleDomainStaple(31);

		this.add32Staple(130, 131);
		plottingData.addShortStaple(plottingData.shortStaples.length - 1, "507.5713,280.0000", "M 0,0 66.141,0");

		plottingData.setDesign23eAlternativeSeam();
	}

	private void addStaple(int i, int j, int k, int l)
	{
		this.addStaple(i, j, k, l, Colour.standard);
	}

	private void addStaple(int i, int j, int k, int l, String col)
	{
		staple = Util.appendArray(staple, new int[] { i, j, k, l });
		colour.addStaple(col);
	}

	private void addShortStaple(int left, int right)
	{
		shortStaple = Util.appendArray(shortStaple, new int[] { left, right });
		colour.addShortStaple();
		plottingData.addShortStaple();
	}

	private void removeStapleNoSingleDomainStaple(int[] remove)
	{
		for (int i = 0; i < remove.length; i++) {

			this.removeStapleNoSingleDomainStaple(remove[i]);

		}
	}

	private void removeStapleNoSingleDomainStaple(int remove)
	{
		// replaces the staple with two half-staples.
		// modify stapleDomainMap, shortStaples, legalStates.
		int[][] stapleNew = new int[staple.length - 1][];

		System.arraycopy(staple, 0, stapleNew, 0, remove);
		System.arraycopy(staple, remove + 1, stapleNew, remove, staple.length - remove - 1);

		for (int i = 0; i < legalStates.size(); i++) {

			this.remove4(i, remove);

		}

		staple = stapleNew;
		colour.removeStaple(remove);

		this.removeStapleFromPrintSets(remove);
		plottingData.remove(remove);

		// also fix the innerStaple toggle 
		BitSet set = new BitSet();

		for (int i = 0; i < staple.length + 1; i++) {

			if (i < remove && innerStaple.get(i)) {

				set.set(i);

			}

			if (i > remove && innerStaple.get(i)) {

				set.set(i - 1);

			}

		}

		innerStaple = set;
		removeFromSeam(remove);
	}

	private void removeStapleFromPrintSets(int remove)
	{
		Util.decreaseValuesAbove(fluorStaples, remove);
		Util.decreaseValuesAbove(quenchStaples, remove);
		Util.decreaseValuesAbove(xStaples, remove);
	}

	private void removeStaple(int remove)
	{
		int[][] stapleNew = new int[staple.length - 1][];
		int[][] shortStapleNew = new int[shortStaple.length + 2][];
		shortStapleNew[shortStaple.length] = new int[2];
		shortStapleNew[shortStaple.length + 1] = new int[2];

		System.arraycopy(staple, 0, stapleNew, 0, remove);
		System.arraycopy(staple, remove + 1, stapleNew, remove, staple.length - remove - 1);

		System.arraycopy(shortStaple, 0, shortStapleNew, 0, shortStaple.length);
		System.arraycopy(staple[remove], 0, shortStapleNew[shortStaple.length], 0, 2);
		System.arraycopy(staple[remove], 2, shortStapleNew[shortStaple.length + 1], 0, 2);

		for (int i = 0; i < legalStates.size(); i++) {
			this.remove4(i, remove);
		}

		staple = stapleNew;
		shortStaple = shortStapleNew;

		colour.removeStaple(remove);

		colour.addShortStaple();
		colour.addShortStaple();

		this.removeStapleFromPrintSets(remove);

		plottingData.remove(remove);
		plottingData.addShortStaple();
		plottingData.addShortStaple();

		BitSet set = new BitSet();

		for (int i = 0; i < staple.length + 1; i++) {

			if (i < remove && innerStaple.get(i)) {

				set.set(i);

			}

			if (i > remove && innerStaple.get(i)) {

				set.set(i - 1);

			}

		}

		innerStaple = set;
		removeFromSeam(remove);
	}

	private void removeFromSeam(int remove)
	{
		removeFromSeam(remove, seam.get(0));
		removeFromSeam(remove, seam.get(1));
		removeFromSeam(remove, seam.get(2));
		removeFromSeam(remove, seam.get(3));
		removeFromSeam(remove, seam.get(4));
	}

	private void removeFromSeam(int remove, ArrayList<Integer> seam)
	{
		for (int i = seam.size(); i > 0; i--) {

			if (seam.get(i - 1) == remove) {

				seam.set(i - 1, 9999);

			} else if (seam.get(i - 1) > remove) {

				seam.set(i - 1, seam.get(i - 1) - 1);

			}

		}

	}

	private void restoreToLegalStates(int pos, int staple)
	{
		BitSet crossover = legalStates.get(pos).state.crossover;

		crossover.set(4 * staple);
		crossover.clear(4 * staple + 1);
		crossover.clear(4 * staple + 2);
		crossover.set(4 * staple + 3);

	}

	private void setDesign23eHalfSeam()
	{
		this.setDesign23e();

		this.removeStaple(72);
		this.removeStaple(58);
		this.removeStaple(44);
		this.removeStaple(30);
		this.removeStaple(16);

		plottingData.addShortStapleInSeam();
	}

	private void remove4(int index, int remove)
	{
		State state = legalStates.get(index).state;
		BitSet crossoverNew = new BitSet(numOfCrossover - 4);

		for (int i = 0; i < (remove * 4); i = i + 4) {

			crossoverNew.set(i, state.crossover.get(i));
			crossoverNew.set(i + 1, state.crossover.get(i + 1));
			crossoverNew.set(i + 2, state.crossover.get(i + 2));
			crossoverNew.set(i + 3, state.crossover.get(i + 3));

		}

		for (int i = (remove * 4); i < (numOfCrossover - 4); i = i + 4) {

			crossoverNew.set(i, state.crossover.get(i + 4));
			crossoverNew.set(i + 1, state.crossover.get(i + 5));
			crossoverNew.set(i + 2, state.crossover.get(i + 6));
			crossoverNew.set(i + 3, state.crossover.get(i + 7));

		}

		state.crossover = crossoverNew;
	}

	private void setMock4()
	{
		dimer = false;
		scaffoldLength = 23;

		int[][] stapleCopy = { { 6, 5, 17, 16 }, //S0
				{ 4, 5, 17, 18 }, //S1
				{ 8, 9, 7, 8 }, //S3
		};

		staple = stapleCopy;

		int[][] shortStapleCopy = { { 0, 1 }, //Short1 
				{ 21, 22 }, //Short2
				{ 0, 22 } // long
		};

		shortStaple = shortStapleCopy;

		this.lazyDomainLength();
		domainLength[22] = 32;
	}

	private void lazyDomainLength()
	{
		if (domainLength == null) {

			domainLength = new int[scaffoldLength];

			for (int i = 0; i < scaffoldLength; i++) {

				domainLength[i] = 16;

			}

		}

	}

	private void removeDomain(int pos)
	{
		Util.removeDomain(staple, pos);
		Util.removeDomain(shortStaple, pos);
	}

	private void setMock1()
	{
		dimer = false;
		scaffoldLength = 36;

		int[][] stapleCopy = { { 2, 3, 33, 34 }, //S0
				{ 7, 8, 28, 29 }, //S1
				{ 9, 8, 28, 27 } //S2
		};

		staple = stapleCopy;
		int[][] shortStaplesCopy = {};
		shortStaple = shortStaplesCopy;
	}

	private void setMock0()
	{
		dimer = false;
		scaffoldLength = 36;

		int[][] stapleCopy = { { 18, 17, 19, 18 }, //S0
		};

		staple = stapleCopy;

		int[][] shortStaplesCopy = {};
		shortStaple = shortStaplesCopy;
	}

	private void setMock11()
	{
		dimer = true;
		scaffoldLength = 168;

		int[][] stapleCopy = { { 97, 98, 70, 71 }, // S9 -- now 0
				{ 69, 70, 98, 99 }, // S16 -- now 1
				{ 111, 112, 56, 57 }, // 23 -- now 2
				{ 55, 56, 112, 113 }, // S30 -- now 3
				{ 125, 126, 42, 43 }, // S37 -- now 4
				{ 41, 42, 126, 127 }, // S44 -- now 5
				{ 139, 140, 28, 29 }, // S51 -- now 6
				{ 27, 28, 140, 141 }, // S58 -- now 7
				{ 153, 154, 14, 15 }, // S65 -- now 8
				{ 13, 14, 154, 155 }, // S72 -- now 9
				{ 83, 84, 84, 85 }, // S76 -- now 10
				{ 1, 0, 0, 167 }, // S77 -- now 11
		};

		staple = stapleCopy;

		int[][] shortStapleCopy = {};
		shortStaple = shortStapleCopy;

		this.makeLegalStates();

		this.addLegalState(new int[] { 11 }, "A");
		this.addLegalState(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11 }, "A");
		this.addLegalState(new int[] { 8, 9, 11 }, "E");
		this.addLegalState(new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 11 }, "E");
		this.addLegalState(new int[] { 6, 7, 8, 9, 11 }, "F");
		this.addLegalState(new int[] { 4, 5, 6, 7, 8, 9, 11 }, "F");
	}

	private void setDesign23e()
	{
		this.setDesign23();
		this.makeLegalStates();

		this.addLegalState(new int[] {}, "A", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72 }, "A", "5:0");
		this.addLegalState(new int[] { 73 }, "B", "5:0");
		this.addLegalState(new int[] { 71 }, "B", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72, 3 }, "B", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72, 2 }, "B", "5:0");
		this.addLegalState(new int[] { 73, 74 }, "C", "5:0");
		this.addLegalState(new int[] { 70, 71 }, "C", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72, 3, 4 }, "C", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72, 2, 1 }, "C", "5:0");
		this.addLegalState(new int[] { 73, 74, 75 }, "D", "5:0");
		this.addLegalState(new int[] { 69, 70, 71 }, "D", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72, 3, 4, 5 }, "D", "5:0");
		this.addLegalState(new int[] { 9, 16, 23, 30, 37, 44, 51, 58, 65, 72, 0, 1, 2 }, "D", "5:0");
		this.addLegalState(new int[] { 65, 72, 59, 60, 61 }, "E", "4:1");
		this.addLegalState(new int[] { 65, 72, 55, 56, 57 }, "E", "4:1");
		this.addLegalState(new int[] { 23, 30, 37, 44, 51, 58, 65, 72, 13, 14, 15 }, "E", "4:1");
		this.addLegalState(new int[] { 23, 30, 37, 44, 51, 58, 65, 72, 17, 18, 19 }, "E", "4:1");
		this.addLegalState(new int[] { 51, 58, 65, 72, 45, 46, 47 }, "F", "3:2");
		this.addLegalState(new int[] { 51, 58, 65, 72, 41, 42, 43 }, "F", "3:2");
		this.addLegalState(new int[] { 37, 44, 51, 58, 65, 72, 27, 28, 29 }, "F", "3:2");
		this.addLegalState(new int[] { 37, 44, 51, 58, 65, 72, 31, 32, 33 }, "F", "3:2");

		this.genDesign23EndToEnd(new int[] { 1, 0, 0, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 0, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 0, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 0, 1, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 1, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 1, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 1, 0, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 0, 0, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 0, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 0, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 0, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 0, 1, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 1, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 1, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 1, 1, 0 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 0, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 0, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 0, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 0, 0, 1, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 1, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 1, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 1, 0, 1 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 0, 1, 1 });
		this.genDesign23EndToEnd(new int[] { 0, 1, 0, 1, 1 });
		this.genDesign23EndToEnd(new int[] { 1, 1, 0, 1, 1 });
		this.genDesign23EndToEnd(new int[] { 1, 0, 1, 1, 1 });
	}

	private void setDesign23()
	{
		scaffoldLength = 166;
		dimer = true;
		colour.setDesign23(76, 14);
		plottingData.setDesign23(76, 14, scaffoldLength);

		int[][] stapleCopy = { { 77, 78, 76, 77 }, // S0
				{ 79, 80, 74, 75 }, // S1
				{ 81, 82, 72, 73 }, // S2
				{ 95, 96, 86, 87 }, // S3
				{ 93, 94, 88, 89 }, // S4
				{ 91, 92, 90, 91 }, // S5
				{ 64, 65, 75, 76 }, // S6
				{ 66, 67, 73, 74 }, // S7
				{ 68, 69, 71, 72 }, // S8
				{ 97, 98, 70, 71 }, // S9
				{ 96, 97, 99, 100 }, // S10
				{ 94, 95, 101, 102 }, // S11
				{ 92, 93, 103, 104 }, // S12
				{ 63, 64, 62, 63 }, // S13
				{ 65, 66, 60, 61 }, // S14
				{ 67, 68, 58, 59 }, // S15
				{ 69, 70, 98, 99 }, // S16
				{ 109, 110, 100, 101 }, // S17
				{ 107, 108, 102, 103 }, // S18
				{ 105, 106, 104, 105 }, // S19
				{ 50, 51, 61, 62 }, // S20
				{ 52, 53, 59, 60 }, // S21
				{ 54, 55, 57, 58 }, // S22
				{ 111, 112, 56, 57 }, // 23
				{ 110, 111, 113, 114 }, // S24
				{ 108, 109, 115, 116 }, // S25
				{ 106, 107, 117, 118 }, // S26
				{ 49, 50, 48, 49 }, // S27
				{ 51, 52, 46, 47 }, // S28
				{ 53, 54, 44, 45 }, // S29
				{ 55, 56, 112, 113 }, // S30
				{ 123, 124, 114, 115 }, // S31
				{ 121, 122, 116, 117 }, // S32
				{ 119, 120, 118, 119 }, // S33
				{ 36, 37, 47, 48 }, // S34
				{ 38, 39, 45, 46 }, // S35
				{ 40, 41, 43, 44 }, // S36
				{ 125, 126, 42, 43 }, // S37
				{ 124, 125, 127, 128 }, // S38
				{ 122, 123, 129, 130 }, // S39
				{ 120, 121, 131, 132 }, // S40
				{ 35, 36, 34, 35 }, // S41
				{ 37, 38, 32, 33 }, // S42
				{ 39, 40, 30, 31 }, // S43
				{ 41, 42, 126, 127 }, // S44
				{ 137, 138, 128, 129 }, // S45
				{ 135, 136, 130, 131 }, // S46
				{ 133, 134, 132, 133 }, // S47
				{ 22, 23, 33, 34 }, // S48
				{ 24, 25, 31, 32 }, // S49
				{ 26, 27, 29, 30 }, // S50
				{ 139, 140, 28, 29 }, // S51
				{ 138, 139, 141, 142 }, // S52
				{ 136, 137, 143, 144 }, // S53
				{ 134, 135, 145, 146 }, // S54
				{ 21, 22, 20, 21 }, // S55
				{ 23, 24, 18, 19 }, // S56
				{ 25, 26, 16, 17 }, // S57
				{ 27, 28, 140, 141 }, // S58
				{ 151, 152, 142, 143 }, // S59
				{ 149, 150, 144, 145 }, // S60
				{ 147, 148, 146, 147 }, // S61
				{ 8, 9, 19, 20 }, // S62
				{ 10, 11, 17, 18 }, // S63
				{ 12, 13, 15, 16 }, // S64
				{ 153, 154, 14, 15 }, // S65
				{ 152, 153, 155, 156 }, // S66
				{ 150, 151, 157, 158 }, // S67
				{ 148, 149, 159, 160 }, // S68
				{ 7, 8, 6, 7 }, // S69
				{ 9, 10, 4, 5 }, // S70
				{ 11, 12, 2, 3 }, // S71
				{ 13, 14, 154, 155 }, // S72
				{ 165, 166, 156, 157 }, // S73
				{ 163, 164, 158, 159 }, // S74
				{ 161, 162, 160, 161 }, // S75
		};
		staple = stapleCopy;

		int[][] shortStaplesCopy = { { 78, 79 }, // Short1 - has to be from low to high
				{ 80, 81 }, // Short2
				{ 82, 83 }, // Short3
				{ 85, 86 }, // Short4
				{ 87, 88 }, // Short5
				{ 89, 90 }, // Short6
				{ 5, 6 }, // Short7
				{ 3, 4 }, // Short8
				{ 1, 2 }, // Short9
				{ 166, 167 }, // Short10
				{ 164, 165 }, // Short11
				{ 162, 163 }, // Short12
				{ 0, 167 }, { 83, 84 } };

		shortStaple = shortStaplesCopy;

		this.removeDomain(84);
		this.removeDomain(0);

		if (useFullSeq) {

			this.loadDomainLengthDesign23();
			this.loadSequenceDesign23();

		} else {

			this.generateDomainLength(scaffoldLength);
			domainLength[82] = 32;
			domainLength[165] = 32;

		}

		innerStaple.set(0);
		innerStaple.set(1);
		innerStaple.set(2);
		innerStaple.set(3);
		innerStaple.set(4);
		innerStaple.set(5);
		innerStaple.set(13);
		innerStaple.set(14);
		innerStaple.set(15);
		innerStaple.set(17);
		innerStaple.set(18);
		innerStaple.set(19);
		innerStaple.set(27);
		innerStaple.set(28);
		innerStaple.set(29);
		innerStaple.set(31);
		innerStaple.set(32);
		innerStaple.set(33);
		innerStaple.set(41);
		innerStaple.set(42);
		innerStaple.set(43);
		innerStaple.set(45);
		innerStaple.set(46);
		innerStaple.set(47);
		innerStaple.set(55);
		innerStaple.set(56);
		innerStaple.set(57);
		innerStaple.set(59);
		innerStaple.set(60);
		innerStaple.set(61);
		innerStaple.set(69);
		innerStaple.set(70);
		innerStaple.set(71);
		innerStaple.set(73);
		innerStaple.set(74);
		innerStaple.set(75);
		innerStaple.set(9);
		innerStaple.set(16);
		innerStaple.set(23);
		innerStaple.set(30);
		innerStaple.set(37);
		innerStaple.set(44);
		innerStaple.set(51);
		innerStaple.set(58);
		innerStaple.set(65);
		innerStaple.set(72);

		int[] fluorStaplesCopy = { 35, 37 };
		fluorStaples = fluorStaplesCopy;

		int[] xStaplesCopy = { 46 };
		xStaples = xStaplesCopy;

		int[] quenchStaplesCopy = { 32, 46 };
		quenchStaples = quenchStaplesCopy;

		seam = new HashMap<Integer, ArrayList<Integer>>();

		seam.put(0, new ArrayList<Integer>());
		seam.put(1, new ArrayList<Integer>());
		seam.put(2, new ArrayList<Integer>());
		seam.put(3, new ArrayList<Integer>());
		seam.put(4, new ArrayList<Integer>());

		seam.get(0).add(9);
		seam.get(0).add(16);
		seam.get(1).add(23);
		seam.get(1).add(30);
		seam.get(2).add(37);
		seam.get(2).add(44);
		seam.get(3).add(51);
		seam.get(3).add(58);
		seam.get(4).add(65);
		seam.get(4).add(72);
	}

	private void generateDomainLength(int length)
	{
		int[] domainLengthCopy = new int[length];

		for (int i = 0; i < length; i++) {

			domainLengthCopy[i] = 16;

		}

		domainLength = domainLengthCopy;
	}

	private void loadSequenceDesign23()
	{
		fullSeq = "GACGAAAGGGCCTCGTGATACGCCTATTTTTATAGGTTAATGTCATGATAATAATGGTTTCTTAGACGTCAGGTGGCACTTTTCGGGGAAATGTGCGCGGAACCCCTATTTGTTTATTTTTCTAAATACATTCAAATATGTATCCGCTCATGAGACAATAACCCTGATAAATGCTTCAATAATATTGAAAAAGGAAGAGTATGAGTATTCAACATTTCCGTGTCGCCCTTATTCCCTTTTTTGCGGCATTTTGCCTTCCTGTTTTTGCTCACCCAGAAACGCTGGTGAAAGTAAAAGATGCTGAAGATCAGTTGGGTGCACGAGTGGGTTACATCGAACTGGATCTCAACAGCGGTAAGATCCTTGAGAGTTTTCGCCCCGAAGAACGTTTTCCAATGATGAGCACTTTTAAAGTTCTGCTATGTGGCGCGGTATTATCCCGTATTGACGCCGGGCAAGAGCAACTCGGTCGCCGCATACACTATTCTCAGAATGACTTGGTTGAGTACTCACCAGTCACAGAAAAGCATCTTACGGATGGCATGACAGTAAGAGAATTATGCAGTGCTGCCATAACCATGAGTGATAACACTGCGGCCAACTTACTTCTGACAACGATCGGAGGACCGAAGGAGCTAACCGCTTTTTTGCACAACATGGGGGATCATGTAACTCGCCTTGATCGTTGGGAACCGGAGCTGAATGAAGCCATACCAAACGACGAGCGTGACACCACGATGCCTGTAGCAATGGCAACAACGTTGCGCAAACTATTAACTGGCGAACTACTTACTCTAGCTTCCCGGCAACAATTAATAGACTGGATGGAGGCGGATAAAGTTGCAGGACCACTTCTGCGCTCGGCCCTTCCGGCTGGCTGGTTTATTGCTGATAAATCTGGAGCCGGTGAGCGTGGGTCTCGCGGTATCATTGCAGCACTGGGGCCAGATGGTAAGCCCTCCCGTATCGTAGTTATCTACACGACGGGGAGTCAGGCAACTATGGATGAACGAAATAGACAGATCGCTGAGATAGGTGCCTCACTGATTAAGCATTGGTAACTGTCAGACCAAGTTTACTCATATATACTTTAGATTGATTTAAAACTTCATTTTTAATTTAAAAGGATCTAGGTGAAGATCCTTTTTGATAATCTCATGACCAAAATCCCTTAACGTGAGTTTTCGTTCCACTGAGCGTCAGACCCCGTAGAAAAGATCAAAGGATCTTCTTGAGATCCTTTTTTTCTGCGCGTAATCTGCTGCTTGCAAACAAAAAAACCACCGCTACCAGCGGTGGTTTGTTTGCCGGATCAAGAGCTACCAACTCTTTTTCCGAAGGTAACTGGCTTCAGCAGAGCGCAGATACCAAATACTGTTCTTCTAGTGTAGCCGTAGTTAGGCCACCACTTCAAGAACTCTGTAGCACCGCCTACATACCTCGCTCTGCTAATCCTGTTACCAGTGGCTGCTGCCAGTGGCGATAAGTCGTGTCTTACCGGGTTGGACTCAAGACGATAGTTACCGGATAAGGCGCAGCGGTCGGGCTGAACGGGGGGTTCGTGCACACAGCCCAGCTTGGAGCGAACGACCTACACCGAACTGAGATACCTACAGCGTGAGCTATGAGAAAGCGCCACGCTTCCCGAAGGGAGAAAGGCGGACAGGTATCCGGTAAGCGGCAGGGTCGGAACAGGAGAGCGCACGAGGGAGCTTCCAGGGGGAAACGCCTGGTATCTTTATAGTCCTGTCGGGTTTCGCCACCTCTGACTTGAGCGTCGATTTTTGTGATGCTCGTCAGGGGGGCGGAGCCTATGGAAAAACGCCAGCAACGCGGCCTTTTTACGGTTCCTGGCCTTTTGCTGGCCTTTTGCTCACATGTTCTTTCCTGCGTTATCCCCTGATTCTGTGGATAACCGTATTACCGCCTTTGAGTGAGCTGATACCGCTCGCCGCAGCCGAACGACCGAGCGCAGCGAGTCAGTGAGCGAGGAAGCGGAAGAGCGCCCAATACGCAAACCGCCTCTCCCCGCGCGTTGGCCGATTCATTAATGCAGCTGGCACGACAGGTTTCCCGACTGGAAAGCGGGCAGTGAGCGCAACGCAATTAATGTGAGTTAGCTCACTCATTAGGCACCCCAGGCTTTACACTTTATGCTTCCGGCTCGTATGTTGTGTGGAATTGTGAGCGGATAACAATTTCACACAGGAAACAGCTATGACCATGATTACGCCAAGCTCCTCAGCAATTCACTGGCCGTCGTTTTACAACGTCGTGACTGGGAAAACCCTGGCGTTACCCAACTTAATCGCCTTGCAGCACATCCCCCTTTCGCCAGCTGGCGTAATAGCGAAGAGGCCCGCACCGATCGCCCTTCCCAACAGTTGCGCAGCCTGAATGGCGAATGGCGCCTGATGCGGTATTTTCTCCTTACGCATCTGTGCGGTATTTCACACCGCATATGGTGCACTCTCAGTACAATCTGCTCTGATGCCGCATAGTTAAGCCAGCCCCGACACCCGCCAACACCCGCTGACGCGCCCTGACGGGCTTGTCTGCTCCCGGCATCCGCTTACAGACAAGCTGTGACCGTCTCCGGGAGCTGCATGTGTCAGAGGTTTTCACCGTCATCACCGAAACGCGCGA";

		int offset = 1408;

		fullSeq = Util.rotate(fullSeq, offset);
		fullSeq = Util.reverse(fullSeq); // fullseq is in 5'-3' notation, this is reversed as the domain description is counter-orientation. When computing free-energy, the sequences are reversed. IMPORTANT: The printing of domains is thus done 3'-5' (anti-convention)
	}

	private void loadDomainLengthDesign23()
	{
		int[] domainLengthCopy = { 16, 15, 16, 16, 16, 15, // row 12 
				16, 16, 16, 15, 16, 16, 16, // row 11	
				15, 16, 16, 16, 15, 16, 16, // row 10
				16, 15, 16, 16, 16, 15, 16, // row 9
				16, 16, 15, 16, 16, 16, 15, // row 8
				16, 16, 16, 15, 16, 16, 16, // row 7
				15, 16, 16, 16, 15, 16, 16, // row 6
				16, 15, 16, 16, 16, 15, 16, // row 5
				16, 16, 15, 16, 16, 16, 15, // row 4
				16, 16, 16, 15, 16, 16, 16, // row 3
				15, 16, 16, 16, 15, 16, 16, // row 2
				16, 15, 16, 16, 16, 15, 32, 16, 15, 16, 16, 16, 15, // row 1
				16, 16, 16, 15, 16, 16, 16, // row 2
				15, 16, 16, 16, 15, 16, 16, // row 3
				16, 15, 16, 16, 16, 15, 16, // row 4
				16, 16, 15, 16, 16, 16, 15, // row 5
				16, 16, 16, 15, 16, 16, 16, // row 6
				15, 16, 16, 16, 15, 16, 16, // row 7
				16, 15, 16, 16, 16, 15, 16, // row 8
				16, 16, 15, 16, 16, 16, 15, // row 9
				16, 16, 16, 15, 16, 16, 16, // row 10
				15, 16, 16, 16, 15, 16, 16, // row 11
				16, 15, 16, 16, 16, 15, 32 // row 12					
		};

		domainLength = domainLengthCopy;
	}

	private void addLegalState(int[] js, String[] type)
	{
		BitSet newLegalState = new BitSet(numOfCrossover);

		for (int i = 0; i < numOfCrossover; i = i + 4) {

			newLegalState.set(i, true);
			newLegalState.set(i + 3, true);

		}

		for (int i = 0; i < js.length; i++) {

			newLegalState.clear(4 * js[i]);
			newLegalState.set(4 * js[i] + 1);
			newLegalState.set(4 * js[i] + 2);
			newLegalState.clear(4 * js[i] + 3);

		}

		legalStates.add(new LegalState(design, newLegalState, type, 2 * scaffoldLength));
	}

	private void addLegalState(int[] js, String type)
	{
		this.addLegalState(js, new String[] { type, "" });
	}

	private void addLegalState(int[] js, String type1, String type2)
	{
		this.addLegalState(js, new String[] { type1, type2 });
	}

	private void genDesign23EndToEnd(int[] cross)
	{
		int[] crossoverStaplesLeft = new int[] {};
		int[] crossoverStaplesRight = new int[] {};

		int[] left0 = new int[] { 0, 1, 2 };
		int[] left1 = new int[] { 13, 14, 15 };
		int[] left2 = new int[] { 27, 28, 29 };
		int[] left3 = new int[] { 41, 42, 43 };
		int[] left4 = new int[] { 55, 56, 57 };
		int[] left5 = new int[] { 69, 70, 71 };

		int[] right0 = new int[] { 3, 4, 5 };
		int[] right1 = new int[] { 17, 18, 19 };
		int[] right2 = new int[] { 31, 32, 33 };
		int[] right3 = new int[] { 45, 46, 47 };
		int[] right4 = new int[] { 59, 60, 61 };
		int[] right5 = new int[] { 73, 74, 75 };

		int[] seam0 = new int[] { 9, 16 };
		int[] seam1 = new int[] { 23, 30 };
		int[] seam2 = new int[] { 37, 44 };
		int[] seam3 = new int[] { 51, 58 };
		int[] seam4 = new int[] { 65, 72 };

		if (cross[0] > 0) {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, left0);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, right0);

		} else { // nothing

		}

		if (cross[0] == cross[1]) { // nothing

		} else {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, left1);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, right1);

		}

		if (cross[1] == cross[2]) { // nothing

		} else {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, left2);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, right2);

		}

		if (cross[2] == cross[3]) { // nothing

		} else {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, left3);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, right3);

		}

		if (cross[3] == cross[4]) { // nothing

		} else {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, left4);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, right4);

		}

		if (cross[4] == 0) { // nothing

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, left5);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, right5);

		}

		// now do seam staples
		if (cross[0] > 0) {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, seam0);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, seam0);

		}
		if (cross[1] > 0) {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, seam1);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, seam1);

		}
		if (cross[2] > 0) {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, seam2);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, seam2);

		}
		if (cross[3] > 0) {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, seam3);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, seam3);

		}
		if (cross[4] > 0) {

			crossoverStaplesLeft = Util.appendIntArray(crossoverStaplesLeft, seam4);
			crossoverStaplesRight = Util.appendIntArray(crossoverStaplesRight, seam4);

		}

		this.addLegalState(crossoverStaplesLeft, "G", "MC");
		this.addLegalState(crossoverStaplesRight, "G", "MC");
	}

	private void makeLegalStates()
	{
		numOfCrossover = staple.length * 4;
		legalStates = new ArrayList<LegalState>();
	}

	public BitSet getInnerStaple()
	{
		return innerStaple;
	}

}
