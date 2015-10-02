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
import writers.RCommand;
import writers.Result;
import writers.ResultWriter;

public class Control
{
	String designString, model, hiddenSettings = "";
	int numOfThreads, numOfPaths;
	double rate;
	private String pad;
	private Design design;
	private Result result;
	private String outDir;
	private ResultWriter writer;

	private void run(String[] args)
	{
		Util.print(args);
		designString = args[0];
		model = args[1];
		numOfThreads = Integer.parseInt(args[2]);
		numOfPaths = Integer.parseInt(args[3]);
		rate = Double.parseDouble(args[4]);

		this.detectCustomScriptAndGo();
	}

	private void detectCustomScriptAndGo()
	{
		if (designString.equals("design23eAll")) {

			designString = "design23e";
			this.go();

			designString = "design23eHalfSeam";
			this.go();

			designString = "design23eReinforceSeamV3";
			this.go();

			designString = "design23eAlternativeSeam";
			this.go();

			designString = "design23eLowerRight";
			this.go();

		} else if (designString.equals("design23eAllEnergy")) {

			designString = "design23eEnergy";
			this.go();

			designString = "design23eHalfSeamEnergy";
			this.go();

			designString = "design23eReinforceSeamV3Energy";
			this.go();

			designString = "design23eAlternativeSeamEnergy";
			this.go();

			designString = "design23eLowerRightEnergy";
			this.go();

		} else if (designString.equals("methodsPaperNoCooperativity")) {

			designString = "design23eMonomerEnergyNoCooperativity";
			hiddenSettings = "UnifiedAlsoMelt";

			model = "distance-M1";
			this.go();

			model = "distance-M2";
			this.go();

			model = "distance-M3";
			this.go();

			model = "distance-M1-HelixBonus2";
			this.go();

			model = "distance-M2-HelixBonus2";
			this.go();

			model = "distance-M3-HelixBonus2";
			this.go();

			model = "distance-M1-HelixBonus4";
			this.go();

			model = "distance-M2-HelixBonus4";
			this.go();

			model = "distance-M3-HelixBonus4";
			this.go();

			model = "distance-M1-HelixBonus6";
			this.go();

			model = "distance-M2-HelixBonus6";
			this.go();

			model = "distance-M3-HelixBonus6";
			this.go();

		} else if (designString.equals("methodsPaperAllPlots")) {

			designString = "design23eMonomerEnergy";
			hiddenSettings = "AlsoMelt";

			model = "distance-M1";
			this.go();

			model = "loop-M1";
			this.go();

			model = "distance-M2";
			this.go();

			model = "loop-M2";
			this.go();

			model = "distance-M3";
			this.go();

			model = "loop-M3";
			this.go();

			model = "distance-M1-HelixBonus2";
			this.go();

			model = "loop-M1-HelixBonus2";
			this.go();

			model = "distance-M2-HelixBonus2";
			this.go();

			model = "loop-M2-HelixBonus2";
			this.go();

			model = "distance-M3-HelixBonus2";
			this.go();

			model = "loop-M3-HelixBonus2";
			this.go();

			model = "distance-M1-HelixBonus4";
			this.go();

			model = "loop-M1-HelixBonus4";
			this.go();

			model = "distance-M2-HelixBonus4";
			this.go();

			model = "loop-M2-HelixBonus4";
			this.go();

			model = "distance-M3-HelixBonus4";
			this.go();

			model = "loop-M3-HelixBonus4";
			this.go();

			model = "distance-M1-HelixBonus6";
			this.go();

			model = "loop-M1-HelixBonus6";
			this.go();

			model = "distance-M2-HelixBonus6";
			this.go();

			model = "loop-M2-HelixBonus6";
			this.go();

			model = "distance-M3-HelixBonus6";
			this.go();

			model = "loop-M3-HelixBonus6";
			this.go();

			designString = "design23eMonomer"; // seq-independent
			model = "loop-M1-HelixBonus0";
			this.go();

			designString = "design23eMonomerEnergy"; // 	reduced concentration	
			model = "loop-M2-HelixBonus4-reducedStapleConcentration";
			this.go();

			model = "loop-M2-HelixBonus4-doubleCGamma";
			this.go();

			model = "loop-M2-HelixBonus4-halfCGamma";
			this.go();

			rate = 10; // different rates
			model = "loop-M2-HelixBonus4";
			this.go();

			rate = 0.1;
			model = "loop-M2-HelixBonus4";
			this.go();

			rate = 1.0;
			designString = "mono-remove-X1";
			model = "loop-M2-HelixBonus4";
			this.go();

			designString = "mono-remove-X2";
			model = "loop-M2-HelixBonus4";
			this.go();

		} else {

			this.go();

		}
	}

	private void go()
	{
		this.initialisePath();
		this.initialiseDesign();
		this.initialiseResult();
		this.runEngine();
		this.writeResults();
		this.createGraphs();
		
		System.out.println("Result dir: " + outDir);
		
	}

	private void initialisePath()
	{
		pad = Util.workDirectory() + "/output/";

		if (designString.contains("jon")) {
			pad = pad + "jon/";
		}

		System.out.println(pad);
	}

	public void initialiseDesign()
	{
		outDir = pad + designString + "-" + (numOfThreads * numOfPaths) + "-" + model + "-" + rate + "CpMin";
		design = new Design(designString, rate, outDir, numOfThreads * numOfPaths, model, hiddenSettings);
	}

	private void initialiseResult()
	{
		result = new Result(design);
		writer = new ResultWriter(result, design, (numOfPaths * numOfThreads));
	}

	private void runEngine()
	{
		design.time.startComputation = System.currentTimeMillis();
		this.doSim();
		System.out.println("Time : " + (System.currentTimeMillis() - design.time.startComputation) + " ms");
	}

	// start the threaded simulation
	private void doSim()
	{
		ArrayList<Thread> threads = new ArrayList<Thread>();

		for (int i = 0; i < numOfThreads; i++) { // start a few threads, which will share the ratesCache
			threads.add(new Thread(new Simulator(design, new RatesGenerator(design), result, numOfPaths, i)));
			threads.get(i).start();
		}

		for (int i = 0; i < numOfThreads; i++) { // wait for threads to rest
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted!");
			}

		}
	}

	private void writeResults()
	{
		writer.write();
	}

	// calls R method
	private void createGraphs()
	{
		String command;

		command = RCommand.getCreateGraphCall(design);
		writer.writeRCommand(command);
		RCommand.execute(command);
	}



	public static void main(String[] args)
	{
		new Control().run(args);
	}

}
