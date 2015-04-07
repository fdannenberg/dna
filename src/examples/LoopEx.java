/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package examples;

import folding.Loop;

public class LoopEx
{
	// example for loop classes
	public static void main(String[] args)
	{
		Loop loop = new Loop(new int[][] { { 0, 48 }, { 50, 62 }, { 64, 336 } });
		Loop loop2 = new Loop(new int[][] { { 62, 64 } });

		System.out.println(loop.toString());
		System.out.println(loop2.toString());

		loop.mergeWith(loop2);

		System.out.println(loop.toString());
		System.out.println(loop2.toString());
	}

}
