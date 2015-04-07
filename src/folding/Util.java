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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import plotting.SvgObject;

public final class Util
{
	public static final void print(int[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++) {

			String output = "[ ";

			for (int j = 0; j < matrix[i].length; j++) {

				output = output + matrix[i][j] + ",\t";

			}

			System.out.println(output + "]     " + i);

		}
	}

	public static final void print(int[] matrix)
	{
		System.out.println("[");

		for (int i = 0; i < matrix.length; i++) {

			System.out.println(i + ": " + matrix[i]);

		}

		System.out.println("]");
	}

	public static final void printArrayArray(ArrayList<ArrayList<Integer>> matrix)
	{
		for (int pos = 0; pos < matrix.size(); pos++) {

			ArrayList<Integer> list = matrix.get(pos);

			if (!list.isEmpty()) {

				String output = "pos = " + pos + ": (";

				for (Integer i : list) {

					output = output + i + ",";

				}

				System.out.println(output + ")");

			}
		}

	}

	public static final int[] appendIntArray(int[] left, int[] right)
	{
		int[] output = new int[left.length + right.length];
		System.arraycopy(left, 0, output, 0, left.length);
		System.arraycopy(right, 0, output, left.length, right.length);

		return output;
	}

	public String makeString(int[] input)
	{

		String output2;
		int length = input.length;
		StringBuffer stringBuffer = new StringBuffer(length);
		int i;

		for (i = 0; i < length; i++) {
			stringBuffer.append(input[i]);
		}

		output2 = stringBuffer.toString();

		return output2;
	}

	public synchronized static State generateInitialState(Design design)
	{
		State newState = new State(design);
		return newState;
	}

	public static void print(ArrayList<Transition> rates)
	{
		System.out.println("Printing transitions");

		for (Transition transition : rates) {

			System.out.println(transition.toString());

		}

		System.out.println("Done printing transitions");
	}

	public static double getDistance(State state, int left, int right)
	{
		DijkstraShortestPath<Integer, DefaultWeightedEdge> path = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(
				(Graph<Integer, DefaultWeightedEdge>) state.graph.get(), left, right);
		double distance = path.getPathLength();

		return distance + Constant.csCost; // include the crossover link in the distance calc too.. model expects the R^2 of the loop, not the distance between ends.
	}

	public static void print(BitSet set)
	{
		System.out.println(set);
	}

	public static void printLegal(ArrayList<LegalState> legalStates)
	{
		for (LegalState state : legalStates) {

			System.out.println(state.toString());
		}
	}

	public static String reverse(String input)
	{
		return new StringBuffer(input).reverse().toString();
	}

	// takes the first i charactars and appends them in the back.
	public static String rotate(String string, int i)
	{
		String front = string.substring(0, i);
		String back = string.substring(i);
		String rotate = back + front;

		return rotate;
	}

	public static void add(double[] array1, double[] array2)
	{
		int length = array1.length;

		for (int i = 0; i < length; i++) {

			array1[i] = array1[i] + array2[i];

		}
	}

	public static void print(double[] input)
	{
		String output = "";

		for (int i = 0; i < input.length; i++) {

			output += input[i] + ", ";

		}

		System.out.println(output);
	}

	public static void print(String[] input)
	{
		for (int i = 0; i < input.length; i++) {

			System.out.println(input[i]);
		}
	}

	public static boolean matchTwo(int[] array, int left, int right)
	{
		return ((array[0] == left && array[1] == right) || (array[1] == left && array[0] == right));
	}

	public static int[][] removeShortStaple(int[][] shortStaple, int left, int right) // will crash if nothing can be removed.
	{
		int size = shortStaple.length;
		int[][] output = new int[size - 1][];
		int found = 0;

		if (left == right) {

			return shortStaple;

		}

		for (int i = 0; i < size; i++) {

			if (matchTwo(shortStaple[i], left, right)) {

				found++;

			} else {

				output[i - found] = shortStaple[i];

			}

		}

		return output;
	}

	public static int findMatch(int[][] shortStaple, int left, int right)
	{
		int size = shortStaple.length;
		if (left == right) {

			return -1;

		}

		for (int i = 0; i < size; i++) {

			if (matchTwo(shortStaple[i], left, right)) {

				return i;

			}

		}

		return -1;
	}

	public static int[][] appendArray(int[][] array, int[] staple)
	{
		int[][] output = new int[array.length + 1][];

		System.arraycopy(array, 0, output, 0, array.length);
		output[array.length] = staple;

		return output;
	}

	public static SvgObject[] appendArraySvgObject(SvgObject[] array, SvgObject staple)
	{
		SvgObject[] output = new SvgObject[array.length + 1];

		System.arraycopy(array, 0, output, 0, array.length);
		output[array.length] = staple;

		return output;
	}

	public static int getSmall(int x, int y, int z)
	{
		return getSmall(getSmall(x, y), z);
	}

	public static int getMedium(int a, int b, int c)
	{
		if ((a - b) * (c - a) >= 0) // a >= b and a <= c OR a <= b and a >= c
			return a;
		else if ((b - a) * (c - b) >= 0) // b >= a and b <= c OR b <= a and b >= c
			return b;
		else
			return c;
	}

	public static int getLarge(int x, int y, int z)
	{
		return getLarge(getLarge(x, y), z);
	}

	public static int getLarge(int x, int y)
	{
		if (x > y) {
			return x;
		} else {
			return y;
		}
	}

	public static int getSmall(int x, int y)
	{
		if (x > y) {
			return y;
		} else {
			return x;
		}
	}

	public static void printArrayList(ArrayList<?> set)
	{
		for (Object thing : set) {
			System.out.println(thing.toString());
		}
	}

	public static double calculateRate(double preFactor, double dH, double dS, double temp)
	{
		double exp = (((1000.0 * dH) - (temp * dS)) / (Constant.kb * temp));
		return preFactor * Math.exp(exp);
	}

	public static String complement(String input)
	{
		String output = new StringBuilder(input.replace('A', 'B').replace('T', 'A').replace('B', 'T').replace('C', 'D').replace('G', 'C').replace('D', 'G'))
				.toString();

		return output;
	}

	public static int descriptionToDomain(int left, int right, int scaffoldLength)
	{
		int output = 0;

		if ((left < right)) {
			output = left;
		} else {
			output = right;
		}

		// special situation, when we specify (0, scaffoldLength -1)
		if ((left == 0) && (right == (scaffoldLength - 1))) {
			output = right;
		}

		if ((left == (scaffoldLength - 1)) && (right == 0)) {
			output = left;
		}

		return output;
	}

	public static void removeDomain(int[][] array, int pos)
	{
		for (int i = 0; i < array.length; i++) {

			Util.decreaseValuesAbove(array[i], pos);
		}
	}

	public static void decreaseValuesAbove(int[] array, int pos)
	{
		for (int j = 0; j < array.length; j++) {

			if (array[j] > pos) {

				array[j] = array[j] - 1;

			}
		}
	}

	public static int[] mergeDomainDown(int[] array, int pos)
	{
		// create array of size n-1. Merge domainlengths of pos onto pos-1;
		int[] newArray = Util.removeDomain(array, pos);

		if (pos == 0) {
			return null; // removing zero not supported
		}

		newArray[pos - 1] = array[pos - 1] + array[pos];

		return newArray;
	}

	public static int[] mergeDomainUp(int[] array, int pos)
	{
		// create array of size n-1. Merge domainlengths of pos onto pos+1;
		int[] newArray = Util.removeDomain(array, pos);

		if (pos == 0) {

			return null; // removing zero not supported

		}

		newArray[pos] = array[pos] + array[pos + 1];

		return newArray;
	}

	public static int[] removeDomain(int[] array, int pos)
	{
		// create array of size n-1. Merge domainlengths of pos onto pos+1;
		int[] newArray = new int[array.length - 1];

		System.arraycopy(array, 0, newArray, 0, (pos));
		System.arraycopy(array, pos + 1, newArray, pos, array.length - 1 - pos);

		return newArray;
	}

	public static void addDomain(int[][] array, int pos)
	{
		for (int i = 0; i < array.length; i++) {

			for (int j = 0; j < array[i].length; j++) {

				if (array[i][j] > pos) {

					array[i][j] = array[i][j] + 1;
				}
			}
		}
	}

	public static String workDirectory()
	{
		String workspace = null;

		try {
			workspace = new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return workspace;
	}

	public static final String[] appendStringArray(String[] array, String end)
	{
		String[] output = new String[array.length + 1];
		System.arraycopy(array, 0, output, 0, array.length);
		output[array.length] = end;

		return output;
	}

	public static void testIfNull(Object object)
	{
		if (object == null) {

			System.out.println("Object is null.");
		}
	}

	public static String readFile(String file)
	{
		BufferedReader reader;
		StringBuilder stringBuilder = new StringBuilder();
		boolean toggle = false;

		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			String ls = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				if (toggle) {
					stringBuilder.append(ls);
				}
				stringBuilder.append(line);
				if (!toggle) {
					toggle = true;
				}
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	public static String interpolate(double factor, Color bottomCol, Color topCol)
	{
		int red, blue, green;

		red = (int) Math.round((1.0 - factor) * bottomCol.getRed() + factor * topCol.getRed());
		blue = (int) Math.round((1.0 - factor) * bottomCol.getBlue() + factor * topCol.getBlue());
		green = (int) Math.round((1.0 - factor) * bottomCol.getGreen() + factor * topCol.getGreen());

		Color color = new Color(Math.min(red, 255), Math.min(green, 255), Math.min(blue, 255));

		return Integer.toHexString(color.getRGB()).substring(2, 8);
	}

	public static SvgObject[] remove(SvgObject[] array, int index)
	{
		SvgObject[] newArray = new SvgObject[array.length - 1];

		System.arraycopy(array, 0, newArray, 0, (index));
		System.arraycopy(array, index + 1, newArray, index, array.length - 1 - index);

		return newArray;
	}

	public static SvgObject[] expandStaples(SvgObject[] staples)
	{
		SvgObject[] newArray = new SvgObject[staples.length + 1];

		for (int i = 0; i < staples.length; i++) {

			newArray[i] = staples[i];

		}

		return newArray;
	}

	public static double getMin(double[] array) // returns smallestValue
	{
		double output = array[0];

		for (int i = 0; i < array.length; i++) {

			if (output > array[i]) {

				output = (double) array[i];

			}

		}

		return output;
	}

	public static double getMax(double[] array)
	{
		double output = array[0];

		for (int i = 0; i < array.length; i++) {

			if (output < array[i]) {

				output = array[i];

			}

		}

		return output;
	}

	public static Double limit(Double a, double x)
	{
		return Math.round(a * x) / x;
	}

	public static double[] castAsDoubleArray(int[] input)
	{
		double[] output;

		output = new double[input.length];

		for (int i = 0; i < input.length; i++) {

			output[i] = (double) input[i];

		}

		return output;
	}

}
