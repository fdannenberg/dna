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
import java.util.Map;
import plotting.PlottingData;
import writers.InfoPrinter;

public class Design // converts the designLoader object into usable data for general, loop-specific and plotting-specific purposes.
{
	public Time time;
	public Model model;
	public Energy energy;
	public Rigidity rigidity;

	private DesignLoader loader;

	// general design data
	private int[][] crossover; // all possible crossover connections, length 4
	public int[][] crossoverToDomain; // takes crossover, says which domains need to be unused
	public ArrayList<ArrayList<Integer>> domainToCrossover; // takes domain, says which crossovers can use it
	private int[][] shortDomain; // all possible single domain staples. 
	public int[] shortToDomain; // takes short staple, says which domain it occupies

	public int numOfCrossover;
	public int numOfStaples;
	public int numOfShortStaples;

	// loop-model specific data
	private int[] doubleCrossover; // denotes with which crossover the crossover forms a seam, if any. (have to check isDouble() if that is so)	
	private BitSet bodyStapleCrossover; // denotes which of the crossover staples are body staples
	private int[][] crossoverToCut; // takes crossover, says which domains it will cut off;
	private BitSet isDouble; //lists if crossover is part of a seam.  
	private BitSet innerCrossover; // lists which crossovers are form interior loops

	// other
	public boolean printing;
	public int[] domainToStapleType; // takes domain, says which staple type can use it (used for plotting functions)

	public BitSet usableDomains; // denotes which domains are part of a 2-domain staple,

	public int scaffoldLength; // actual scaffold length in number of domains
	public int simpleScaffoldLength; // if dimer, this value is half the scaffold length.

	public int numOfPaths;
	public String outDir;

	public final LegalType noType = new LegalType(new String[] { "N.T.", "N.T." });

	// basic constructor defaults options to false;
	public Design()
	{
		this("design23e", 0.4, "distance", false, "");
	}

	public Design(String design2, double rate, String model2)
	{
		this(design2, rate, model2, true, "");
	}

	public Design(String design2, double rate, String outDir2, int numOfPaths2, String model2, String hiddenOptions)
	{
		this(design2, rate, model2, false, hiddenOptions);
		outDir = outDir2;
		numOfPaths = numOfPaths2;
	}

	// basic constructor
	public Design(String design2, double rate, String modelType2, boolean printInfo, String hiddenSettings)
	{
		printing = printInfo;

		if (printing) {
			System.out.println("Design is " + design2);
		}

		usableDomains = new BitSet();

		model = new Model(modelType2);
		model.processHidden(hiddenSettings);
		time = new Time(rate, model.tempStart(), model.alsoMelt(), hiddenSettings);

		loader = new DesignLoader(design2, this);

		this.processDesign();
		this.precomputeMaps();

		energy = new Energy(this, loader);

		InfoPrinter.print(this);
	}

	private void processDesign()
	{
		scaffoldLength = loader.scaffoldLength;
		numOfStaples = loader.staple.length;
		numOfShortStaples = loader.shortStaple.length;

		if (loader.dimer) { // make a mapping of all possible staple connections. 

			this.splitZeroPoint(loader.staple);
			this.splitZeroPointShort(loader.shortStaple);

			simpleScaffoldLength = scaffoldLength;
			scaffoldLength = 2 * scaffoldLength;

		} else { // monomer

			simpleScaffoldLength = scaffoldLength;

		}

		this.genStaple();
		this.genShortStaple();
	}

	public boolean getAlsoMelt()
	{
		return model.alsoMelt();
	}

	private void genInnerCrossover()
	{

		innerCrossover = new BitSet(numOfCrossover);

		if (isDimer()) {

			for (int i = 0; i < numOfCrossover; i++) {

				innerCrossover.set(4 * i + 0, loader.getInnerStaple().get(i));
				innerCrossover.set(4 * i + 1, loader.getInnerStaple().get(i));
				innerCrossover.set(4 * i + 2, loader.getInnerStaple().get(i));
				innerCrossover.set(4 * i + 3, loader.getInnerStaple().get(i));

			}

		} else {

			for (int i = 0; i < numOfCrossover; i++) {

				innerCrossover.set(i, loader.getInnerStaple().get(i));

			}

		}

	}

	private void genShortStaple()
	{
		if (isDimer()) {

			shortDomain = new int[2 * numOfShortStaples][2];

			for (int i = 0; i < numOfShortStaples; i++) {

				shortDomain[2 * i + 0][0] = loader.shortStaple[i][0];
				shortDomain[2 * i + 0][1] = loader.shortStaple[i][1];

				shortDomain[2 * i + 1][0] = this.opposite(loader.shortStaple[i][0]);
				shortDomain[2 * i + 1][1] = this.opposite(loader.shortStaple[i][1]);

			}

		} else {

			shortDomain = loader.shortStaple;

		}

	}

	private void genStaple()
	{
		if (isDimer()) {

			crossover = new int[4 * numOfStaples][4];

			for (int i = 0; i < numOfStaples; i++) {

				crossover[4 * i + 0][0] = loader.staple[i][0];
				crossover[4 * i + 0][1] = loader.staple[i][1];
				crossover[4 * i + 0][2] = loader.staple[i][2];
				crossover[4 * i + 0][3] = loader.staple[i][3];

				crossover[4 * i + 1][0] = loader.staple[i][0];
				crossover[4 * i + 1][1] = loader.staple[i][1];
				crossover[4 * i + 1][2] = this.opposite(loader.staple[i][2]);
				crossover[4 * i + 1][3] = this.opposite(loader.staple[i][3]);

				crossover[4 * i + 2][0] = this.opposite(loader.staple[i][0]);
				crossover[4 * i + 2][1] = this.opposite(loader.staple[i][1]);
				crossover[4 * i + 2][2] = loader.staple[i][2];
				crossover[4 * i + 2][3] = loader.staple[i][3];

				crossover[4 * i + 3][0] = this.opposite(loader.staple[i][0]);
				crossover[4 * i + 3][1] = this.opposite(loader.staple[i][1]);
				crossover[4 * i + 3][2] = this.opposite(loader.staple[i][2]);
				crossover[4 * i + 3][3] = this.opposite(loader.staple[i][3]);

			}

		} else {

			crossover = loader.staple;

		}

		numOfCrossover = crossover.length;

	}

	private int opposite(int pos)
	{
		return (pos + simpleScaffoldLength) % scaffoldLength;
	}

	private void precomputeMaps()
	{
		crossoverToDomain = new int[numOfCrossover][2];
		domainToCrossover = new ArrayList<ArrayList<Integer>>(scaffoldLength);
		shortToDomain = new int[shortDomain.length];
		isDouble = new BitSet(numOfCrossover);
		doubleCrossover = new int[numOfCrossover];

		for (int i = 0; i < scaffoldLength; i++) {

			domainToCrossover.add(new ArrayList<Integer>());

		}

		// Generate crossoverToDomain
		for (int i = 0; i < numOfCrossover; i++) {

			int left = Util.descriptionToDomain(crossover[i][0], crossover[i][1], scaffoldLength);
			int right = Util.descriptionToDomain(crossover[i][2], crossover[i][3], scaffoldLength);

			crossoverToDomain[i][0] = left;
			crossoverToDomain[i][1] = right;

			// also save the values in domainToCrossover
			domainToCrossover.get(left).add(i);
			domainToCrossover.get(right).add(i);

			// also update usableDomains
			usableDomains.set(left);
			usableDomains.set(right);

		}

		// now also make the short staples usable domains
		for (int i = 0; i < shortDomain.length; i++) {

			int domain = Util.descriptionToDomain(shortDomain[i][0], shortDomain[i][1], scaffoldLength);

			usableDomains.set(domain);
			shortToDomain[i] = domain;
		}

		for (int i = 0; i < numOfCrossover; i++) { // now save which staples are part of double-crossovers

			boolean isFound = false; // just stating the obvious
			doubleCrossover[i] = 0;

			for (int j = 0; j < numOfCrossover; j++) {

				if (i != j) {

					if (crossover[i][1] == crossover[j][1] && crossover[i][2] == crossover[j][2] || crossover[i][1] == crossover[j][2]
							&& crossover[i][2] == crossover[j][1]) {// check if crossovers are equal

						isFound = true;
						doubleCrossover[i] = j;

					}

				}

			}

			isDouble.set(i, isFound);

		}

		this.setRigidity();
		this.genInnerCrossover();
		this.genCrossoverToCut();
		this.generateDomainToStapleType();
	}

	private void generateDomainToStapleType()
	{
		domainToStapleType = new int[scaffoldLength];

		for (int i = 0; i < scaffoldLength; i++) {

			domainToStapleType[i] = -1;

		}

		for (int i = 0; i < loader.staple.length; i++) {

			int[] staple = loader.staple[i];

			int leftDomain = Util.descriptionToDomain(staple[0], staple[1], loader.scaffoldLength);
			int rightDomain = Util.descriptionToDomain(staple[2], staple[3], loader.scaffoldLength);

			domainToStapleType[leftDomain] = i;
			domainToStapleType[rightDomain] = i;

			domainToStapleType[(leftDomain + loader.scaffoldLength) % scaffoldLength] = i;
			domainToStapleType[(rightDomain + loader.scaffoldLength) % scaffoldLength] = i;

		}

	}

	private void setRigidity()
	{
		rigidity = new Rigidity(this);
		rigidity.generateCost(loader.domainLength, scaffoldLength, simpleScaffoldLength);
	}

	private void genCrossoverToCut()
	{
		crossoverToCut = new int[crossover.length][2];

		for (int i = 0; i < crossover.length; i++) {

			int[] staple = getStaple(i);

			crossoverToCut[i][0] = Util.getSmall(staple[1], staple[2]);
			crossoverToCut[i][1] = Util.getLarge(staple[1], staple[2]);

		}

	}

	public final int getCrossoverToCut(int a, int b)
	{
		return crossoverToCut[a][b];
	}

	public final int[] getCrossoverToCut(int a)
	{
		return crossoverToCut[a];
	}

	private void splitZeroPoint(int[][] staple)
	{
		// split the zero-noted point on the scaffold.
		for (int i = 0; i < staple.length; i++) {

			this.split(staple, i, 0, 1);
			this.split(staple, i, 1, 0);
			this.split(staple, i, 2, 3);
			this.split(staple, i, 3, 2);

		}
	}

	private void splitZeroPointShort(int[][] staple)
	{
		// split the zero-noted point on the scaffold.
		for (int i = 0; i < staple.length; i++) {

			this.split(staple, i, 0, 1);
			this.split(staple, i, 1, 0);

		}
	}

	private void split(int[][] array, int i, int left, int right)
	{
		if ((array[i][left] == 0) && (array[i][right] == (scaffoldLength - 1))) {
			array[i][left] = scaffoldLength;
			if (printing) {
				System.out.println("SPLITTING ZERO-POINT" + " scaffoldLength is " + scaffoldLength);
			}
		}
	}

	public int getNumberOfSeamStaples()
	{
		int output = 0;

		for (int i = 0; i < loader.seam.size(); i++) {

			ArrayList<Integer> select = loader.seam.get(i);

			for (int j = 0; j < select.size(); j++) {

				if (select.get(j) < 500) {
					output++;
				}

			}

		}

		return output;
	}

	public boolean isSeamStaple(int input)
	{
		for (int i = 0; i < loader.seam.size(); i++) {

			ArrayList<Integer> select = loader.seam.get(i);

			for (int j = 0; j < select.size(); j++) {

				if (select.get(j).equals(input)) {

					return true;

				}

			}

		}

		return false;
	}

	public int[] getDomainFromType(int type)
	{
		int[] output;

		if (type < numOfStaples) { // two domain staple

			if (isDimer()) {

				output = crossoverToDomain[4 * type];

			} else {

				output = crossoverToDomain[type];

			}

		} else { // single domain staple

			type = type - numOfStaples;

			if (isDimer()) {

				output = new int[] { shortToDomain[2 * type] };

			} else {

				output = new int[] { shortToDomain[type] };

			}

		}

		return output;
	}

	public BitSet getBodyStaples()
	{
		if (bodyStapleCrossover == null) {

			bodyStapleCrossover = new BitSet();

			for (int i = 0; i < this.numOfCrossover; i++) {
				bodyStapleCrossover.set(i);
			}

			for (int i = 0; i < 5; i++) {

				bodyStapleCrossover.set(4 * loader.seam.get(i).get(0), false);
				bodyStapleCrossover.set(4 * loader.seam.get(i).get(0) + 1, false);
				bodyStapleCrossover.set(4 * loader.seam.get(i).get(0) + 2, false);
				bodyStapleCrossover.set(4 * loader.seam.get(i).get(0) + 3, false);

				bodyStapleCrossover.set(4 * loader.seam.get(i).get(1), false);
				bodyStapleCrossover.set(4 * loader.seam.get(i).get(1) + 1, false);
				bodyStapleCrossover.set(4 * loader.seam.get(i).get(1) + 2, false);
				bodyStapleCrossover.set(4 * loader.seam.get(i).get(1) + 3, false);

			}

		}

		return (BitSet) bodyStapleCrossover.clone();
	}

	public final String getDesignString()
	{
		return loader.designString;
	}

	public final int getStaple(int a, int b)
	{
		return crossover[a][b];
	}

	public final int[] getStaple(int a)
	{
		return crossover[a];
	}

	public final boolean isDimer()
	{
		return loader.dimer;
	}

	public final boolean isInnerStaple(int pos)
	{
		return innerCrossover.get(pos);
	}

	public final LegalState legalStates(int index)
	{
		return loader.legalStates.get(index);
	}

	public final ArrayList<LegalState> getLegalStates()
	{
		return loader.legalStates;
	}

	public final boolean testDoubleInPlace(int pos1, BitSet crossover)
	{
		return (isDouble.get(pos1) && crossover.get(doubleCrossover[pos1]));
	}

	public final int[] getDomains(int pos)
	{
		return crossoverToDomain[pos];
	}

	public int getDomainRight(int pos)
	{
		return (pos + 1 + scaffoldLength) % scaffoldLength;
	}

	public int getDomainLeft(int pos)
	{
		return (pos - 1 + scaffoldLength) % scaffoldLength;
	}

	public boolean useFullSeq() // backbone seq is specified
	{
		return loader.useFullSeq;
	}

	public String getColour(int i)
	{
		return loader.colour.getColour(i);
	}

	public String getDomainSeq(int i)
	{
		return loader.domainSeq[i];
	}

	public int[] getDomainLength()
	{
		return loader.domainLength;
	}

	public double[] getDsCost()
	{
		return rigidity.dsCost;
	}

	public double[] getSsCost()
	{
		return rigidity.ssCost;
	}

	public double getKPlus()
	{
		return model.kPlus();
	}

	public double[] getInitialConcentrationStaple()
	{
		return loader.initialConcentrationStaple;
	}

	public PlottingData getPlottingData()
	{
		return loader.plottingData;
	}

	public double getExpFactor()
	{
		return model.gamma();
	}

	public int[] getFluorStaples()
	{
		return loader.fluorStaples;
	}

	public int[] getQuenchStaples()
	{
		return loader.quenchStaples;
	}

	public int[] getXStaples()
	{
		return loader.xStaples;
	}

	public Map<Integer, ArrayList<Integer>> seam()
	{
		return loader.seam;
	}

	public int stapleCount()
	{
		return (numOfStaples + numOfShortStaples);
	}

	public int numOfShortDomain()
	{
		return shortDomain.length;
	}

	//print options	
	public void crossoverPrint()
	{
		Util.print(crossover);
	}

	public void shortDomainPrint()
	{
		Util.print(shortDomain);
	}

	public void doubleCrossoverPrint()
	{
		Util.print(doubleCrossover);
	}

	public void legalStatesPrint()
	{
		Util.printLegal(loader.legalStates);
	}

	public void crossoverToCutPrint()
	{
		Util.print(crossoverToCut);
	}

	public String getFullSeq()
	{
		return loader.fullSeq;
	}

}
