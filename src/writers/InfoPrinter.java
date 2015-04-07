/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package writers;

import folding.Design;

public final class InfoPrinter
{

	public final static void print(Design design)
	{
		printSelected(design);
	}

	private static void printSelected(Design design)
	{
		//design.time.printStepsInfo();

		//System.out.println("Number of legal states is " + design.getLegalStates().size());

		//		System.out.println("Fluor staples are: ");
		//		Util.print(design.getFluorStaples());
		//		System.out.println("End fluor staples ");
		//
		//		System.out.println("Quench staples are: ");
		//		Util.print(design.getQuenchStaples());
		//		System.out.println("End quench staples ");
		//
		//		System.out.println("Usable Domains start");
		//		Util.print(design.usableDomains);
		//		System.out.println("Usable Domains end");
		//		
		//		System.out.println("domainToCrossover start");
		//		Util.printArrayArray(design.domainToCrossover);
		//		System.out.println("domainToCrossover end");
		//	
		//		System.out.println("staple start");
		//		design.crossoverPrint();
		//		System.out.println("staple end");
		//	
		//		System.out.println("shortDomain start");
		//		design.shortDomainPrint();
		//		System.out.println("shortDomain end");

		//		System.out.println("isLongDomain start");
		//		Util.print(design.isLong);
		//		System.out.println("isLongDomain end");
		//
		//		System.out.println("crossoverToDomain start");
		//		Util.print(design.crossoverToDomain);
		//		System.out.println("crossoverToDomain end");

		//		System.out.println("dsCost start");
		//		Util.print(design.getDsCost());
		//		System.out.println("dsCost end");

		//		System.out.println("shortStapleDomain start");
		//		Util.print(design.shortToDomain);
		//		System.out.println("shortStapleDomain end");

		//		System.out.println("isDouble start");
		//		Util.print(design.isDouble);
		//		System.out.println("isDouble end");
		//
		//		System.out.println("doubleCrossover start");
		//		design.doubleCrossoverPrint();
		//		System.out.println("doubleCrossover end");

		//		System.out.println("domainLength start");
		//		Util.print(design.getDomainLength());
		//		System.out.println("domainLength end");
		//		//
		//		System.out.println("dsCost start");
		//		Util.printByLine(design.rigidity.dsCost);
		//		System.out.println("dsCost end");
		//
		//		System.out.println("ssCost start");
		//		Util.printByLine(design.rigidity.ssCost);
		//		System.out.println("ssCost end");

		//		System.out.println("Printing legal states");
		//		design.legalStatesPrint();
		//		System.out.println("Done printing legal states");

		//		System.out.println("Printing inner crossovers");
		//		Util.print(design.innerCrossover);
		//		System.out.println("Done printing inner crossovers");
		//
		//		System.out.println("Printing crossoverToCut");
		//		design.crossoverToCutPrint();
		//		System.out.println("Done printing crossoverToCut");

		//		System.out.println("Printing inner crossovers");
		//		design.crossoverPrint();
		//		System.out.println("Done printing inner crossovers");
		//
		//		System.out.println("Printing domainToStapleType");
		//		Util.print(design.domainToStapleType);
		//		System.out.println("Done printing domainToStapleType");

		//		System.out.println("Printing sequence");
		//		Util.printByLine(design.energy.getDomainSeq());
		//		System.out.println("Done printing sequence");

		//		for (int i = 0; i < design.scaffoldLength; i++) {
		//
		//			System.out.println("Domain " + i + " is:");
		//			
		//			if (design.useFullSeq()) {
		//				System.out.println(Util.complement((design.energy.getDomainSeq(i))));
		//			}
		//			System.out.println("dH is: " + design.energy.getDH(i));
		//			System.out.println("dS is: " + design.energy.getDS(i));
		//			System.out.println("T_M is:" + design.energy.calc.calcTM(design.energy.getDH(i), design.energy.getDS(i), design.model.getStapleConcentration()));
		//
		//		}

		//			System.out.println("fullSeq length is " + fullSeqLength);

		//System.out.println(" neighbourCorrectionDH - neighbourCorrectionDS " + design.energy.neighbourCorrectionDH + " - " + design.energy.neighbourCorrectionDS);

	}
}
