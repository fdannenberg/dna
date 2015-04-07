/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tests;

import static org.junit.Assert.*;
import folding.*;

public class LoopTest
{
	public static void main(String[] args)
	{
		double rate = 0.4;
		Design design = new Design("design23e", rate, "loop", false, "");

		Loop loop = new Loop(design, new int[][] { { 0, 48 }, { 50, 62 }, { 64, design.scaffoldLength } });
		Loop loop2 = new Loop(design, new int[][] { { 62, 64 } });
		Loop loop3 = new Loop(design, new int[][] { { 48, 50 } });

		assertEquals("Segment test", (int) loop.getNumOfCrossoverSegments(), (int) 2);
		loop.mergeWith(loop2);
		assertEquals("test1", loop.toString(), new Loop(new int[][] { { 0, 48 }, { 50, design.scaffoldLength } }).toString());
		loop.mergeWith(loop3);
		assertEquals("test2", loop.toString(), new Loop(new int[][] { { 0, design.scaffoldLength } }).toString());
		Loop newLoop = new Loop(design);
		loop.split(27, 29, newLoop);
		assertEquals("test3", loop.toString(), new Loop(new int[][] { { 0, 27 }, { 29, design.scaffoldLength } }).toString());
	}

}
