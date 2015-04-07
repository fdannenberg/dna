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

public class LegalType
{
	public static final String all = "all";
	public static final String fiveZero = "5:0";
	public static final String fourOne = "4:1";
	public static final String threeTwo = "3:2";
	public static final String MCType = "MC";
	public static final String noType = "N.T.";

	public static final String[] legalTypes = { all, fiveZero, fourOne, threeTwo, MCType, noType };

	public static final String descA = "A";
	public static final String descB = "B";
	public static final String descC = "C";
	public static final String descD = "D";
	public static final String descE = "E";
	public static final String descF = "F";
	public static final String descG = "G";

	public String[] type;

	public LegalType(String[] type2)
	{
		type = type2;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof LegalType) {

			LegalType that = (LegalType) other;
			if (that.type.length == type.length) {

				for (int i = 0; i < type.length; i++) {

					if (!that.type[i].equals(type[i])) {

						return false;

					}

				}

				return true;
			}

		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int output = 0;
		int length = type.length;

		for (int i = 0; i < length; i++) {

			output = output + type[i].hashCode();

		}

		return output;
	}

	@Override
	public String toString()
	{
		String output = "";
		int length = type.length;

		for (int i = 0; i < length; i++) {

			output = output + type[i] + " ";

		}

		return output;
	}

}
