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

public class RatesCalc
{
	private Design design;
	private double temp;
	private double bindRate;

	public RatesCalc(Design design2)
	{
		design = design2;
		bindRate = design.model.getStapleConcentration() * design.getKPlus();

		this.setUnbindRate(0.0);
	}

	public void setUnbindRate(double time)
	{
		temp = design.time.temp(time);
	}

	public double getBindRate()
	{
		return bindRate;
	}

	private FreeEnergy getDomainEnergy(int pos, State state)
	{
		FreeEnergy output = new FreeEnergy(design.energy.getDH(pos), design.energy.getDS(pos));
		output.add(neighbourCorrection(this.countNumOfNeighbours(state, pos)));

		return output;
	}

	// For half-bound staples unbinding, the unbind rate is modified for the loop model, since we have to compensate for loop entropy  
	public double getUnbindRate(int pos, State state)
	{
		FreeEnergy energy = this.getDomainEnergy(pos, state);

		if (design.model.useLoop()) {
			energy.subtract(state.loop.getUnbindDiff(pos, state, temp));
		}

		return Util.calculateRate(design.getKPlus(), energy.dH(), energy.dS(), temp);
	}

	// Full-bound staple unbinds one domain
	public double getUnbindRateCS(int pos, State state)
	{
		FreeEnergy energy = this.getDomainEnergy(pos, state);
		return Util.calculateRate(design.getKPlus(), energy.dH(), energy.dS(), temp);
	}

	private FreeEnergy neighbourCorrection(int num)
	{
		return new FreeEnergy((double) num * design.energy.neighbourCorrectionDH, (double) num * design.energy.neighbourCorrectionDS);
	}

	private int countNumOfNeighbours(State state, int pos)
	{
		int output = 0;

		if (state.domain.get(design.getDomainLeft(pos))) {

			output++;

		}

		if (state.domain.get(design.getDomainRight(pos))) {

			output++;

		}

		return output;
	}

	public double crossoverRate(State state, int left, int right, int domain, int crossover)
	{
		if (design.model.useDistance()) {

			state.graph.setEdgeDS(domain);
			double output = crossoverRateDistance(state, left, right);
			state.graph.setEdgeSS(domain);

			return output;

		} else if (design.model.useLoop()) {

			return crossoverRateLoop(state, left, right, domain, crossover);

		} else {

			throw new RuntimeException("something went wrong - class RatesCalc");

		}
	}

	private double crossoverRateLoop(State state, int left, int right, int domain, int crossover)
	{
		FreeEnergy energy = state.loop.getBindDiffCS(state, domain, crossover);

		if (design.printing) {
			System.out.println("dH and dS are " + energy.dH() + "  " + energy.dS());
		}

		return Util.calculateRate(design.getKPlus(), -energy.dH(), -energy.dS(), temp);
	}

	public double crossoverRateDistance(State state, int left, int right)
	{
		double output, distance;

		distance = this.calcDistance(state, left, right) * 1E-18;
		output = design.getKPlus() * Math.pow(design.model.preFactor() / distance, design.model.gamma());

		if (design.printing) {
			System.out.println("State " + state);
			System.out.println("Distance and rate " + distance + "  " + output + "  left,right " + left + "," + right);
		}

		return output;
	}

	private double calcDistance(State state, int left, int right)
	{
		return Util.getDistance(state, left, right);
	}

	public double getTemp()
	{
		return temp;
	}

	public FreeEnergy nillLoops(int count)
	{
		// Returns the free energy cost of count-many nill loops in the loop model.
		double dH, dS;

		dH = 0.0;
		dS = count * design.model.seamEnergy();

		return new FreeEnergy(dH, dS);
	}

}
