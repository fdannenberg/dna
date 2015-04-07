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

// Collects all model-specific settings, variables and methods that are not part of design class.
public class Model
{
	// model booleans
	private boolean useLoop, useDistance;

	// model constants
	private double gamma;
	private double preFactor;
	private double seamEnergy;
	private double kPlus;
	private double stapleConcentration;
	private double tempStart;
	private boolean alsoMelt;
	private double helixBonus = 0.0; //  bonus in basepair  

	private String modelType;

	public Model()
	{
		// Constructor that uses default options
		this("distance");
	}

	public Model(String modelType2)
	{
		modelType = modelType2;

		this.initCalculationModel(modelType);
		this.setExponent(modelType);
		this.setkPlus(modelType);
		this.setTempStart();
		this.setAlsoMelt(modelType);
		this.setHelixBonus(modelType);
		this.setStapleConcentration(modelType);
		this.setCustomCGamma(modelType);

		if (useLoop) {
			System.out.println("The model used here is loops model");
		}
		if (useDistance) {
			System.out.println("The model used here is distance model");
		}

		System.out.println("Gamma is " + gamma);
		System.out.println("Prefactor is " + preFactor);
	}

	private void setStapleConcentration(String options)
	{
		if (options.contains("reducedStapleConcentration")) {

			stapleConcentration = 20.0E-10;

		} else {

			stapleConcentration = 20.0E-9;

		}
	}

	public void processHidden(String option)
	{
		this.setAlsoMelt(option);
	}

	private void setCustomCGamma(String option)
	{
		if (option.contains(Constant.doubleCGamma)) {

			preFactor = 2.0 * preFactor;

		}

		if (option.contains(Constant.halfCGamma)) {

			preFactor = 0.5 * preFactor;

		}
	}

	private void setHelixBonus(String options)
	{
		if (options.contains(Constant.helixBonus1)) {

			helixBonus = 0.5;

		}

		if (options.contains(Constant.helixBonus2)) {

			helixBonus = 1.0;

		}

		if (options.contains(Constant.helixBonus3)) {

			helixBonus = 1.5;

		}

		if (options.contains(Constant.helixBonus4)) {

			helixBonus = 2.0;

		}

		if (options.contains(Constant.helixBonus5)) {

			helixBonus = 2.5;

		}

		if (options.contains(Constant.helixBonus6)) {

			helixBonus = 3.0;

		}
	}

	private void setAlsoMelt(String option)
	{
		if (option.contains("AlsoMelt")) {
			alsoMelt = true;
		}
	}

	private void setTempStart()
	{
		tempStart = 273.15 + 85.0;
	}

	private void setkPlus(String modelType2)
	{
		kPlus = 1.0E6; //	Standard model	
	}

	private void setExponent(String modelType) // DO NOT CALL FROM PROCESSHIDDEN
	{
		if (modelType.contains(Constant.modelVersionTwo)) {

			gamma = 2.5;
			preFactor = 2.80E-18; // m^2			// Alternative2 model

		} else if (modelType.contains(Constant.modelVersionThree)) {

			gamma = 3.5;
			preFactor = 5.2E-18; // m^2				// Alternative model

		} else { // default to ``regular'' version;

			gamma = 1.5;
			preFactor = 6.69546718045E-19; // m^2		// Standard model

		}

		seamEnergy = LoopCalc.seamEnergy(this);
	}

	private void initCalculationModel(String modelType)
	{
		if (modelType.contains(Constant.modelDistance)) {

			useDistance = true;

		} else if (modelType.contains(Constant.modelLoop)) {

			useLoop = true;

		} else {

			System.out.println("Model of computation not specified");

		}
	}

	public boolean useLoop()
	{
		return useLoop;
	}

	public boolean useDistance()
	{
		return useDistance;
	}

	public double preFactor()
	{
		return preFactor;
	}

	public double gamma()
	{
		return gamma;
	}

	public double seamEnergy()
	{
		return seamEnergy;
	}

	public double kPlus()
	{
		return kPlus;
	}

	public double tempStart()
	{
		return tempStart;
	}

	public boolean alsoMelt()
	{
		return alsoMelt;
	}

	public String getType()
	{
		// returns the model options that were put into the program. 
		return modelType;
	}

	public double helixBonus()
	{
		return helixBonus;
	}

	public double getStapleConcentration()
	{
		return stapleConcentration;
	}

}
