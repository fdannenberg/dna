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

import org.jgrapht.Graph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class StateGraph
{
	private Design design;
	private Graph<Integer, DefaultWeightedEdge> g;

	public StateGraph(Design design2)
	{
		design = design2;
		g = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		this.addVertices();
		this.addSSregions();
	}

	public StateGraph(State state)
	{
		design = state.design;
		g = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		this.addVertices();
		this.addSSregions();

		for (int i = 0; i < state.design.scaffoldLength; i++) {

			if (state.domain.get(i)) {

				this.setEdgeSS(i);

			}

		}

		for (int i = 0; i < state.crossover.length(); i++) {

			if (state.crossover.get(i)) {

				this.addCrossover(i);

			}

		}
	}

	private void addVertices()
	{
		// it has scaffoldLength number of vertices
		int numVertices = design.scaffoldLength;

		for (int i = 0; i < numVertices; i++) {
			g.addVertex(i);
		}
	}

	private void addSSregions()
	{
		// scaffold connections
		for (int i = 0; i < design.scaffoldLength; i++) {

			this.addEdgeSS(i, (i + 1) % design.scaffoldLength);
		}
	}

	private void setEdgeDS(int left, int right)
	{
		DefaultWeightedEdge edge = g.getEdge(left, right);
		((AbstractBaseGraph<Integer, DefaultWeightedEdge>) g).setEdgeWeight(edge, design.rigidity.dsCost[left]);
	}

	private void addEdgeSS(int left, int right)
	{
		DefaultWeightedEdge e1 = g.addEdge(left, right);
		((AbstractBaseGraph<Integer, DefaultWeightedEdge>) g).setEdgeWeight(e1, design.rigidity.ssCost[left]);
	}

	private void setEdgeSS(int left, int right)
	{
		DefaultWeightedEdge e1 = g.getEdge(left, right);
		((AbstractBaseGraph<Integer, DefaultWeightedEdge>) g).setEdgeWeight(e1, design.rigidity.ssCost[left]);
	}

	public void setEdgeDS(int pos)
	{
		this.setEdgeDS(pos, (pos + 1) % design.scaffoldLength);
	}

	public void setEdgeSS(int pos)
	{
		this.setEdgeSS(pos, (pos + 1) % design.scaffoldLength);
	}

	public Graph<Integer, DefaultWeightedEdge> get()
	{
		return g;
	}

	private void addCrossover(int crossover)
	{
		int[] positions = design.getStaple(crossover);
		this.addCrossover(new int[] { positions[1], positions[2] });
	}

	private void addCrossover(int[] pos)
	{
		DefaultWeightedEdge edge = g.addEdge(pos[0], pos[1]);

		if (edge != null) { // for seam staples, no duplicate edge will be created since it is already there

			if (design.getDesignString().contains("NoCooperativity")) {

				((AbstractBaseGraph<Integer, DefaultWeightedEdge>) g).setEdgeWeight(edge, 999999.0 * Constant.csCost);

			} else {

				((AbstractBaseGraph<Integer, DefaultWeightedEdge>) g).setEdgeWeight(edge, Constant.csCost);

			}
		}
	}

	public void removeCrossover(int[] pos)
	{
		g.removeEdge(pos[0], pos[1]);
	}

	public void setDomain(int pos, boolean val)
	{
		if (val) {
			this.setEdgeDS(pos);
		} else {
			this.setEdgeSS(pos);
		}
	}

	public void setCrossover(int[] pos, boolean val)
	{
		if (pos[0] != pos[1]) { // skip continuous staples 
			if (val) {
				addCrossover(pos);
			} else {
				removeCrossover(pos);
			}
		}
	}

}
