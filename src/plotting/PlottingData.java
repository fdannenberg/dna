/**
 * Copyright (c) 2015 FRITS DANNENBERG 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package plotting;

import java.util.ArrayList;
import java.util.Collections;

import folding.Design;
import folding.Util;

public class PlottingData
{
	private final static String shortStapleDesc = "M 0,0 -29.292,0";
	private final static String height5 = "193.4346";
	private final static String height4 = "242.7305";
	private final static String height3 = "292.0283";
	private final static String height2 = "341.3252";
	private final static String height1 = "390.6235";

	Design design;
	public SvgObject[] staples;
	public SvgObject[] shortStaples;
	public SvgObject[] domains;
	public SvgObject[] seamCorrelation;

	private SvgObject[] customData;
	private ArrayList<Integer> toRemoveDomains;

	public PlottingData(Design design2)
	{
		design = design2;

		toRemoveDomains = new ArrayList<Integer>();
	}

	public void init(int size)
	{
		customData = new SvgObject[size];
	}

	public void set(int pos, String first, String second)
	{
		SvgObject obj = new SvgObject(Integer.toString(pos), first, second);
		customData[pos] = obj;
	}

	public void setDesign23(int numOfStaples, int numOfShortStaples, int simpleScaffoldLength)
	{
		staples = new SvgObject[numOfStaples];
		shortStaples = new SvgObject[numOfShortStaples];
		domains = new SvgObject[simpleScaffoldLength];
		seamCorrelation = new SvgObject[81];

		this.addStaple(0, "55.2832,415.269", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(1, "124.6465,415.269", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(2, "194.0151,415.269", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(3, "398.2051,415.269", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(4, "467.5713,415.269", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(5, "536.9355,415.269", "m 0,0 -29.292,0 0,13.732 29.292,0");

		this.addStaple(6, "89.9663,390.6235", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(7, "159.332,390.6235", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(8, "228.6948,390.6235", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(9, "263.3794,404.355", "M 0,0 66.141,0");
		this.addStaple(10, "363.5254,390.6235", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(11, "432.8906,390.6235", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(12, "502.2559,390.6235", "m 0,0 -29.292,0 0,13.732 29.292,0");

		this.addStaple(13, "55.2832,365.9731", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(14, "124.6465,365.9731", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(15, "194.0151,365.9731", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(16, "263.3794,390.6245", "M 0,0 66.141,0");
		this.addStaple(17, "398.2051,365.9731", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(18, "467.5713,365.9731", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(19, "536.9355,365.9731", "m 0,0 -29.292,0 0,13.732 29.292,0");

		this.addStaple(20, "89.9663,341.3252", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(21, "159.332,341.3252", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(22, "228.6948,341.3252", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(23, "263.3794,355.0566", "M 0,0 66.141,0");
		this.addStaple(24, "363.5254,341.3252", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(25, "432.8906,341.3252", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(26, "502.2559,341.3252", "m 0,0 -29.292,0 0,13.732 29.292,0");

		this.addStaple(27, "55.2832,316.6768", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(28, "124.6465,316.6768", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(29, "194.0151,316.6768", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(30, "263.3794,341.3252", "M 0,0 66.141,0");
		this.addStaple(31, "398.2051,316.6768", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(32, "467.5713,316.6768", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(33, "536.9355,316.6768", "m 0,0 -29.292,0 0,13.732 29.292,0");

		this.addStaple(34, "89.9663,292.0283", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(35, "159.332,292.0283", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(36, "228.6948,292.0283", "m 0,0 29.292,0 0,13.732 -29.292,0");
		this.addStaple(37, "263.3794,305.7603", "M 0,0 66.141,0");
		this.addStaple(38, "363.5254,292.0283", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(39, "432.8906,292.0283", "m 0,0 -29.292,0 0,13.732 29.292,0");
		this.addStaple(40, "502.2559,292.0283", "m 0,0 -29.292,0 0,13.732 29.292,0");

		this.addStaple(41, "55.2832,267.3779", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(42, "124.6465,267.3779", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(43, "194.0151,267.3779", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(44, "263.3794,292.0273", "M 0,0 66.141,0");
		this.addStaple(45, "398.2051,267.3779", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(46, "467.5713,267.3779", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(47, "536.9355,267.3779", "m 0,0 -29.292,0 0,13.731 29.292,0");

		this.addStaple(48, "89.9663,242.7305", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(49, "159.332,242.7305", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(50, "228.6948,242.7305", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(51, "263.3794,257.8115", "M 0,0 66.141,0");
		this.addStaple(52, "363.5254,242.7305", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(53, "432.8906,242.7305", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(54, "502.2559,242.7305", "m 0,0 -29.292,0 0,13.731 29.292,0");

		this.addStaple(55, "55.2832,218.085", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(56, "124.6465,218.085", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(57, "194.0151,218.085", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(58, "263.3794,242.7314", "M 0,0 66.141,0");
		this.addStaple(59, "398.2051,218.085", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(60, "467.5713,218.085", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(61, "536.9355,218.085", "m 0,0 -29.292,0 0,13.731 29.292,0");

		this.addStaple(62, "89.9663,193.4316", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(63, "159.332,193.4316", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(64, "228.6948,193.4316", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(65, "263.3794,207.165", "M 0,0 66.141,0");
		this.addStaple(66, "363.5254,193.4316", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(67, "432.8906,193.4316", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(68, "502.2559,193.4316", "m 0,0 -29.292,0 0,13.731 29.292,0");

		this.addStaple(69, "54.8765,168.7861", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(70, "124.6465,168.7861", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(71, "194.0151,168.7861", "m 0,0 29.292,0 0,13.731 -29.292,0");
		this.addStaple(72, "263.3794,193.4346", "M 0,0 66.141,0");
		this.addStaple(73, "398.2051,168.7861", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(74, "467.5713,168.7861", "m 0,0 -29.292,0 0,13.731 29.292,0");
		this.addStaple(75, "537.3428,168.7861", "m 0,0 -29.292,0 0,13.731 29.292,0");

		this.addShortStaple(0, "119.2593,429.001", "M 0,0 -29.292,0");
		this.addShortStaple(1, "188.6216,429.001", "M 0,0 -29.292,0");
		this.addShortStaple(2, "258.8003,429.001", "M 0,0 -29.292,0");
		this.addShortStaple(3, "362.7129,429.001", "M 0,0 -29.292,0");
		this.addShortStaple(4, "432.0771,429.001", "M 0,0 -29.292,0");
		this.addShortStaple(5, "501.4414,429.001", "M 0,0 -29.292,0");

		this.addShortStaple(6, "89.9692,168.7852", "M 0,0 29.292,0");
		this.addShortStaple(7, "159.335,168.7852", "M 0,0 29.292,0");
		this.addShortStaple(8, "228.6948,168.7852", "M 0,0 29.292,0");
		this.addShortStaple(9, "334.2354,168.7852", "M 0,0 29.292,0");
		this.addShortStaple(10, "403.5977,168.7852", "M 0,0 29.292,0");
		this.addShortStaple(11, "472.9639,168.7852", "M 0,0 29.292,0");

		this.addShortStaple(12, "263.3794,429.0005", "M 0,0 66.141,0");
		this.addShortStaple(13, "264.0225,168.7861", "M 0,0 66.141,0");

		this.addDomain(0, "", "m 293.112,540.20929 40.2765,0");
		this.addDomain(1, "", "m 248.71201,540.20929 40.2765,0");
		this.addDomain(2, "", "m 204.312,540.20929 40.2765,0");
		this.addDomain(3, "", "m 159.91201,540.20929 40.2765,0");
		this.addDomain(4, "", "m 115.51201,540.20929 40.2765,0");
		this.addDomain(5, "", "m 71.112005,540.20929 40.276495,0");

		this.addDomain(6, "", "m 111.3885,509.32779 -40.276495,0");
		this.addDomain(7, "", "m 155.7885,509.32779 -40.2765,0");
		this.addDomain(8, "", "m 200.18851,509.32779 -40.27651,0");
		this.addDomain(9, "", "m 244.5885,509.32779 -40.2765,0");
		this.addDomain(10, "", "m 288.98851,509.32779 -40.2765,0");
		this.addDomain(11, "", "m 333.3885,509.32779 -40.2765,0");
		this.addDomain(12, "", "m 377.7885,509.32779 -40.2765,0");

		this.addDomain(13, "", "m 337.512,478.10929 40.2765,0");
		this.addDomain(14, "", "m 293.112,478.10929 40.2765,0");
		this.addDomain(15, "", "m 248.71201,478.10929 40.2765,0");
		this.addDomain(16, "", "m 204.312,478.10929 40.2765,0");
		this.addDomain(17, "", "m 159.91201,478.10929 40.2765,0");
		this.addDomain(18, "", "m 115.51201,478.10929 40.2765,0");
		this.addDomain(19, "", "m 71.112005,478.10929 40.276505,0");

		this.addDomain(20, "", "m 111.38851,447.22779 -40.276505,0");
		this.addDomain(21, "", "m 155.7885,447.22779 -40.2765,0");
		this.addDomain(22, "", "m 200.18851,447.22779 -40.27651,0");
		this.addDomain(23, "", "m 244.5885,447.22779 -40.2765,0");
		this.addDomain(24, "", "m 288.98851,447.22779 -40.2765,0");
		this.addDomain(25, "", "m 333.3885,447.22779 -40.2765,0");
		this.addDomain(26, "", "m 377.7885,447.22779 -40.2765,0");

		this.addDomain(27, "", "m 337.512,416.70229 40.2765,0");
		this.addDomain(28, "", "m 293.112,416.70229 40.2765,0");
		this.addDomain(29, "", "m 248.71201,416.70229 40.2765,0");
		this.addDomain(30, "", "m 204.312,416.70229 40.2765,0");
		this.addDomain(31, "", "m 159.91201,416.70229 40.2765,0");
		this.addDomain(32, "", "m 115.51201,416.70229 40.2765,0");
		this.addDomain(33, "", "m 71.112005,416.70229 40.276505,0");

		this.addDomain(34, "", "m 111.38851,385.82079 -40.276505,0");
		this.addDomain(35, "", "m 155.7885,385.82079 -40.2765,0");
		this.addDomain(36, "", "m 200.18851,385.82079 -40.27651,0");
		this.addDomain(37, "", "m 244.5885,385.82079 -40.2765,0");
		this.addDomain(38, "", "m 288.98851,385.82079 -40.2765,0");
		this.addDomain(39, "", "m 333.3885,385.82079 -40.2765,0");
		this.addDomain(40, "", "m 377.7885,385.82079 -40.2765,0");

		this.addDomain(41, "", "m 337.512,355.16429 40.2765,0");
		this.addDomain(42, "", "m 293.112,355.16429 40.2765,0");
		this.addDomain(43, "", "m 248.71201,355.16429 40.2765,0");
		this.addDomain(44, "", "m 204.312,355.16429 40.2765,0");
		this.addDomain(45, "", "m 159.91201,355.16429 40.2765,0");
		this.addDomain(46, "", "m 115.51201,355.16429 40.2765,0");
		this.addDomain(47, "", "m 71.112005,355.16429 40.276495,0");

		this.addDomain(48, "", "m 111.3885,324.28279 -40.276495,0");
		this.addDomain(49, "", "m 155.7885,324.28279 -40.2765,0");
		this.addDomain(50, "", "m 200.18851,324.28279 -40.27651,0");
		this.addDomain(51, "", "m 244.5885,324.28279 -40.2765,0");
		this.addDomain(52, "", "m 288.98851,324.28279 -40.2765,0");
		this.addDomain(53, "", "m 333.3885,324.28279 -40.2765,0");
		this.addDomain(54, "", "m 377.7885,324.28279 -40.2765,0");

		this.addDomain(55, "", "m 337.512,293.06429 40.2765,0");
		this.addDomain(56, "", "m 293.112,293.06429 40.2765,0");
		this.addDomain(57, "", "m 248.71201,293.06429 40.2765,0");
		this.addDomain(58, "", "m 204.312,293.06429 40.2765,0");
		this.addDomain(59, "", "m 159.91201,293.06429 40.2765,0");
		this.addDomain(60, "", "m 115.51201,293.06429 40.2765,0");
		this.addDomain(61, "", "m 71.112005,293.06429 40.276495,0");

		this.addDomain(62, "", "m 111.3885,262.18279 -40.276495,0");
		this.addDomain(63, "", "m 155.7885,262.18279 -40.2765,0");
		this.addDomain(64, "", "m 200.18851,262.18279 -40.27651,0");
		this.addDomain(65, "", "m 244.5885,262.18279 -40.2765,0");
		this.addDomain(66, "", "m 288.98851,262.18279 -40.2765,0");
		this.addDomain(67, "", "m 333.3885,262.18279 -40.2765,0");
		this.addDomain(68, "", "m 377.7885,262.18279 -40.2765,0");

		this.addDomain(69, "", "m 337.512,231.65729 40.2765,0");
		this.addDomain(70, "", "m 293.112,231.65729 40.2765,0");
		this.addDomain(71, "", "m 248.71201,231.65729 40.2765,0");
		this.addDomain(72, "", "m 204.312,231.65729 40.2765,0");
		this.addDomain(73, "", "m 159.91201,231.65729 40.2765,0");
		this.addDomain(74, "", "m 115.51201,231.65729 40.2765,0");
		this.addDomain(75, "", "m 71.112005,231.65729 40.276495,0");

		this.addDomain(76, "", "m 111.3885,200.77579 -40.276495,0");
		this.addDomain(77, "", "m 155.7885,200.77579 -40.2765,0");
		this.addDomain(78, "", "m 200.18851,200.77579 -40.27651,0");
		this.addDomain(79, "", "m 244.5885,200.77579 -40.2765,0");
		this.addDomain(80, "", "m 288.98851,200.77579 -40.2765,0");
		this.addDomain(81, "", "m 333.3885,200.77579 -40.2765,0");
		this.addDomain(82, "", "m 423.61953,200.77579 -86.1072,0");
		this.addDomain(83, "", "m 467.57553,200.77579 -39.87373,0");
		this.addDomain(84, "", "m 511.53153,200.77579 -39.87373,0");
		this.addDomain(85, "", "m 555.48753,200.77579 -39.87373,0");
		this.addDomain(86, "", "m 599.44353,200.77579 -39.87373,0");
		this.addDomain(87, "", "m 643.39953,200.77579 -39.87373,0");
		this.addDomain(88, "", "m 687.35553,200.77579 -39.87373,0");

		this.addDomain(89, "", "m 647.4818,231.65729 39.87373,0");
		this.addDomain(90, "", "m 603.5258,231.65729 39.87373,0");
		this.addDomain(91, "", "m 559.5698,231.65729 39.87373,0");
		this.addDomain(92, "", "m 515.6138,231.65729 39.87373,0");
		this.addDomain(93, "", "m 471.6578,231.65729 39.87373,0");
		this.addDomain(94, "", "m 427.7018,231.65729 39.87373,0");
		this.addDomain(95, "", "m 383.74579,231.65729 39.87374,0");

		this.addDomain(96, "", "m 423.61953,262.18279 -39.87374,0");
		this.addDomain(97, "", "m 467.57553,262.18279 -39.87373,0");
		this.addDomain(98, "", "m 511.53153,262.18279 -39.87373,0");
		this.addDomain(99, "", "m 555.48753,262.18279 -39.87373,0");
		this.addDomain(100, "", "m 599.44353,262.18279 -39.87373,0");
		this.addDomain(101, "", "m 643.39953,262.18279 -39.87373,0");
		this.addDomain(102, "", "m 687.35553,262.18279 -39.87373,0");

		this.addDomain(103, "", "m 647.4818,293.06429 39.87373,0");
		this.addDomain(104, "", "m 603.5258,293.06429 39.87373,0");
		this.addDomain(105, "", "m 559.5698,293.06429 39.87373,0");
		this.addDomain(106, "", "m 515.6138,293.06429 39.87373,0");
		this.addDomain(107, "", "m 471.6578,293.06429 39.87373,0");
		this.addDomain(108, "", "m 427.7018,293.06429 39.87373,0");
		this.addDomain(109, "", "m 383.74579,293.06429 39.87374,0");

		this.addDomain(110, "", "m 423.61953,324.28279 -39.87374,0");
		this.addDomain(111, "", "m 467.57553,324.28279 -39.87373,0");
		this.addDomain(112, "", "m 511.53153,324.28279 -39.87373,0");
		this.addDomain(113, "", "m 555.48753,324.28279 -39.87373,0");
		this.addDomain(114, "", "m 599.44353,324.28279 -39.87373,0");
		this.addDomain(115, "", "m 643.39953,324.28279 -39.87373,0");
		this.addDomain(116, "", "m 687.35553,324.28279 -39.87373,0");

		this.addDomain(117, "", "m 647.4818,355.16429 39.87373,0");
		this.addDomain(118, "", "m 603.5258,355.16429 39.87373,0");
		this.addDomain(119, "", "m 559.5698,355.16429 39.87373,0");
		this.addDomain(120, "", "m 515.6138,355.16429 39.87373,0");
		this.addDomain(121, "", "m 471.6578,355.16429 39.87373,0");
		this.addDomain(122, "", "m 427.7018,355.16429 39.87373,0");
		this.addDomain(123, "", "m 383.74579,355.16429 39.87374,0");

		this.addDomain(124, "", "m 423.61953,385.82079 -39.87374,0");
		this.addDomain(125, "", "m 467.57553,385.82079 -39.87373,0");
		this.addDomain(126, "", "m 511.53153,385.82079 -39.87373,0");
		this.addDomain(127, "", "m 555.48753,385.82079 -39.87373,0");
		this.addDomain(128, "", "m 599.44353,385.82079 -39.87373,0");
		this.addDomain(129, "", "m 643.39953,385.82079 -39.87373,0");
		this.addDomain(130, "", "m 687.35553,385.82079 -39.87373,0");

		this.addDomain(131, "", "m 647.4818,416.70229 39.87373,0");
		this.addDomain(132, "", "m 603.5258,416.70229 39.87373,0");
		this.addDomain(133, "", "m 559.5698,416.70229 39.87373,0");
		this.addDomain(134, "", "m 515.6138,416.70229 39.87373,0");
		this.addDomain(135, "", "m 471.6578,416.70229 39.87373,0");
		this.addDomain(136, "", "m 427.7018,416.70229 39.87373,0");
		this.addDomain(137, "", "m 383.74579,416.70229 39.87374,0");

		this.addDomain(138, "", "m 423.61953,447.22779 -39.87374,0");
		this.addDomain(139, "", "m 467.57553,447.22779 -39.87373,0");
		this.addDomain(140, "", "m 511.53153,447.22779 -39.87373,0");
		this.addDomain(141, "", "m 555.48753,447.22779 -39.87373,0");
		this.addDomain(142, "", "m 599.44353,447.22779 -39.87373,0");
		this.addDomain(143, "", "m 643.39953,447.22779 -39.87373,0");
		this.addDomain(144, "", "m 687.35553,447.22779 -39.87373,0");

		this.addDomain(145, "", "m 647.4818,478.10929 39.87373,0");
		this.addDomain(146, "", "m 603.5258,478.10929 39.87373,0");
		this.addDomain(147, "", "m 559.5698,478.10929 39.87373,0");
		this.addDomain(148, "", "m 515.6138,478.10929 39.87373,0");
		this.addDomain(149, "", "m 471.6578,478.10929 39.87373,0");
		this.addDomain(150, "", "m 427.7018,478.10929 39.87373,0");
		this.addDomain(151, "", "m 383.74579,478.10929 39.87374,0");

		this.addDomain(152, "", "m 423.61953,509.32779 -39.87374,0");
		this.addDomain(153, "", "m 467.57553,509.32779 -39.87373,0");
		this.addDomain(154, "", "m 511.53153,509.32779 -39.87373,0");
		this.addDomain(155, "", "m 555.48753,509.32779 -39.87373,0");
		this.addDomain(156, "", "m 599.44353,509.32779 -39.87373,0");
		this.addDomain(157, "", "m 643.39953,509.32779 -39.87373,0");
		this.addDomain(158, "", "m 687.35553,509.32779 -39.87373,0");

		this.addDomain(159, "", "m 647.4818,540.20929 39.87373,0");
		this.addDomain(160, "", "m 603.5258,540.20929 39.87373,0");
		this.addDomain(161, "", "m 559.5698,540.20929 39.87373,0");
		this.addDomain(162, "", "m 515.6138,540.20929 39.87373,0");
		this.addDomain(163, "", "m 471.6578,540.20929 39.87373,0");
		this.addDomain(164, "", "m 427.7018,540.20929 39.87373,0");
		this.addDomain(165, "", "m 337.51233,540.20929 86.1072,0");

		this.addSquare(0, "", "m -28.163425,129.71228 -46.591492,0 0,-46.591488 46.591492,0 0,46.591488 z");
		this.addSquare(1, "", "m 21.027904,129.71228 -46.591491,0 0,-46.591488 46.591491,0 0,46.591488 z");
		this.addSquare(2, "", "m 70.219255,129.71228 -46.591492,0 0,-46.591488 46.591492,0 0,46.591488 z");
		this.addSquare(3, "", "m 119.41681,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(4, "", "m 168.58946,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(5, "", "m 239.95118,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(6, "", "m 289.14251,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(7, "", "m 338.34009,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(8, "", "m 387.53142,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(9, "", "m 436.71029,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(10, "", "m 501.83737,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(11, "", "m 551.02248,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(12, "", "m 600.22005,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(13, "", "m 649.40516,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(14, "", "m 698.59026,129.71228 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(15, "", "m -28.163425,179.78894 -46.591492,0 0,-46.59149 46.591492,0 0,46.59149 z");
		this.addSquare(16, "", "m 21.027904,179.78894 -46.591491,0 0,-46.59149 46.591491,0 0,46.59149 z");
		this.addSquare(17, "", "m 70.219255,179.78894 -46.591492,0 0,-46.59149 46.591492,0 0,46.59149 z");
		this.addSquare(18, "", "m 119.41681,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(19, "", "m 168.58946,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(20, "", "m 239.95118,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(21, "", "m 289.14251,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(22, "", "m 338.34009,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(23, "", "m 387.53142,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(24, "", "m 436.71029,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(25, "", "m 501.83737,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(26, "", "m 551.02248,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(27, "", "m 600.22005,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(28, "", "m 649.40516,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(29, "", "m 698.59026,179.78894 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(30, "", "m -28.163425,229.87183 -46.591492,0 0,-46.59149 46.591492,0 0,46.59149 z");
		this.addSquare(31, "", "m 21.027904,229.87183 -46.591491,0 0,-46.591488 46.591491,0 0,46.591488 z");
		this.addSquare(32, "", "m 70.219255,229.87183 -46.591492,0 0,-46.591488 46.591492,0 0,46.591488 z");
		this.addSquare(33, "", "m 119.41681,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(34, "", "m 168.58946,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(35, "", "m 239.95118,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(36, "", "m 289.14251,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(37, "", "m 338.34009,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(38, "", "m 387.53142,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(39, "", "m 436.71029,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(40, "", "m 501.83737,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(41, "", "m 551.02248,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(42, "", "m 600.22005,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(43, "", "m 649.40516,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(44, "", "m 698.59026,229.87183 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(45, "", "m -28.163425,279.94849 -46.591492,0 0,-46.5915 46.591492,0 0,46.5915 z");
		this.addSquare(46, "", "m 21.027904,279.94849 -46.591491,0 0,-46.59149 46.591491,0 0,46.59149 z");
		this.addSquare(47, "", "m 70.219255,279.94849 -46.591492,0 0,-46.59149 46.591492,0 0,46.59149 z");
		this.addSquare(48, "", "m 119.41681,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(49, "", "m 168.58946,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(50, "", "m 239.95118,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(51, "", "m 289.14251,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(52, "", "m 338.34009,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(53, "", "m 387.53142,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(54, "", "m 436.71029,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(55, "", "m 501.83737,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(56, "", "m 551.02248,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(57, "", "m 600.22005,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(58, "", "m 649.40516,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(59, "", "m 698.59026,279.94849 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(60, "", "m -28.157199,330.02514 -46.591491,0 0,-46.59149 46.591491,0 0,46.59149 z");
		this.addSquare(61, "", "m 21.027904,330.02514 -46.591491,0 0,-46.591488 46.591491,0 0,46.591488 z");
		this.addSquare(62, "", "m 70.219255,330.02514 -46.591492,0 0,-46.591488 46.591492,0 0,46.591488 z");
		this.addSquare(63, "", "m 119.41681,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(64, "", "m 168.58946,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(65, "", "m 239.95118,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(66, "", "m 289.14251,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(67, "", "m 338.34009,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(68, "", "m 387.53142,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(69, "", "m 436.71029,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(70, "", "m 501.83737,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(71, "", "m 551.02248,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(72, "", "m 600.22005,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(73, "", "m 649.40516,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");
		this.addSquare(74, "", "m 698.59026,330.02514 -46.59149,0 0,-46.591488 46.59149,0 0,46.591488 z");

		this.addSquare(75, "", "m -148.16342,129.71227 -46.5915,0 0,-46.59149 46.5915,0 0,46.59149 z");
		this.addSquare(76, "", "m -148.16342,179.78893 -46.5915,0 0,-46.59149 46.5915,0 0,46.59149 z");
		this.addSquare(77, "", "m -148.16342,229.87182 -46.5915,0 0,-46.59149 46.5915,0 0,46.59149 z");
		this.addSquare(78, "", "m -148.16342,279.94848 -46.5915,0 0,-46.5915 46.5915,0 0,46.5915 z");
		this.addSquare(79, "", "m -148.1572,330.02513 -46.59149,0 0,-46.59149 46.59149,0 0,46.59149 z");
		this.addSquare(80, "", "m -98.972088,330.02477 -46.591502,0 0,-246.90399 46.591502,0 0,246.90399 z");

	}

	private void addSquare(int i, String coor, String line)
	{
		SvgObject square = new SvgObject(Integer.toString(i + 1000), coor, line);
		seamCorrelation[i] = square;
	}

	public void setDesign23ReinforceSeamV3Domains()
	{
		this.removeDomain(162);
		this.removeDomain(155);
		this.removeDomain(148);
		this.removeDomain(141);

		this.removeDomain(106);
		this.removeDomain(99);
		this.removeDomain(92);
		this.removeDomain(85);

		this.removeDomain(79);
		this.removeDomain(72);
		this.removeDomain(65);
		this.removeDomain(58);

		this.removeDomain(23);
		this.removeDomain(16);
		this.removeDomain(9);
		this.removeDomain(2);

		this.addDomain(1, "", "m 208.71201,540.20929 80.2765,0");
		this.addDomain(10, "", "m 288.98851,509.32779 -80.2765,0");
		this.addDomain(15, "", "m 208.71201,478.10929 80.2765,0");
		this.addDomain(24, "", "m 288.98851,447.22779 -80.2765,0");

		this.addDomain(57, "", "m 208.71201,293.06429 80.2765,0");
		this.addDomain(66, "", "m 288.98851,262.18279 -80.2765,0");
		this.addDomain(71, "", "m 208.71201,231.65729 80.2765,0");
		this.addDomain(80, "", "m 288.98851,200.77579 -80.2765,0");

		this.addDomain(84, "", "m 551.53153,200.77579 -80.87373,0");
		this.addDomain(93, "", "m 471.6578,231.65729 80.87373,0");
		this.addDomain(98, "", "m 551.53153,262.18279 -80.87373,0");
		this.addDomain(107, "", "m 471.6578,293.06429 80.87373,0");

		this.addDomain(140, "", "m 551.53153,447.22779 -80.87373,0");
		this.addDomain(149, "", "m 471.6578,478.10929 80.87373,0");
		this.addDomain(156, "", "m 551.53153,509.32779 -80.87373,0");
		this.addDomain(163, "", "m 471.6578,540.20929 80.87373,0");

	}

	public void setDesign23eAlternativeSeam()
	{
		this.addDomain(130, "", "m 727.35553,385.82079 -80.07373,0");
		this.removeDomain(131);
	}

	public void setDesign23eLowerRight()
	{
		this.addDomain(157, "", "m 763.39953,509.32779 -160.07373,0");

		this.removeDomain(158);
		this.removeDomain(159);
		this.removeDomain(160);
	}

	public void addShortStaple(int i, String coor, String line)
	{

		SvgObject staple = new SvgObject(Integer.toString(i + 1000), coor, line);
		shortStaples[i] = staple;
	}

	private void addDomain(int i, String coor, String line)
	{
		SvgObject domain = new SvgObject(Integer.toString(i + 1000), coor, line);
		domains[i] = domain;
	}

	private void addStaple(int i, String coor, String line)
	{
		SvgObject staple = new SvgObject(Integer.toString(i + 1000), coor, line);
		staples[i] = staple;
	}

	public void addStaple(String coor, String line)
	{
		staples = Util.expandStaples(staples);

		SvgObject staple = new SvgObject(Integer.toString(staples.length - 1 + 1000), coor, line);
		staples[staples.length - 1] = staple;
	}

	public void remove(int index)
	{
		staples = Util.remove(staples, index);
	}

	private void removeDomain(int index)
	{
		toRemoveDomains.add(index);
	}

	public void addShortStaple()
	{
		this.expandShortStaples();
		this.addShortStaple(shortStaples.length - 1, "0.0,0.0", "M 0,0 0.0,0");
	}

	private void expandShortStaples()
	{
		shortStaples = Util.expandStaples(shortStaples);
	}

	public void addShortStapleInSeam()
	{
		// set the short staples that were just created. 
		int num = shortStaples.length;

		String left = "330.0,";
		String right = "293.0,";

		shortStaples[num - 1].setCoorLine(left + height5, shortStapleDesc);
		shortStaples[num - 2].setCoorLine(right + height5, shortStapleDesc);

		shortStaples[num - 3].setCoorLine(left + height4, shortStapleDesc);
		shortStaples[num - 4].setCoorLine(right + height4, shortStapleDesc);

		shortStaples[num - 5].setCoorLine(left + height3, shortStapleDesc);
		shortStaples[num - 6].setCoorLine(right + height3, shortStapleDesc);

		shortStaples[num - 7].setCoorLine(left + height2, shortStapleDesc);
		shortStaples[num - 8].setCoorLine(right + height2, shortStapleDesc);

		shortStaples[num - 9].setCoorLine(left + height1, shortStapleDesc);
		shortStaples[num - 10].setCoorLine(right + height1, shortStapleDesc);

	}

	public void finalize()
	{
		Collections.sort(toRemoveDomains);
		Collections.reverse(toRemoveDomains);

		for (int i = 0; i < toRemoveDomains.size(); i++) {
			domains = Util.remove(domains, toRemoveDomains.get(i));

		}
	}

	public SvgObject[] getCustomData()
	{
		return customData;
	}

}
