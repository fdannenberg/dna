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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListWriter
{
	private List<BufferedWriter> wList;
	private String exportFilePath;

	public ListWriter(String exportFilePath2)
	{
		exportFilePath = exportFilePath2;
		wList = new ArrayList<BufferedWriter>();
	}

	public ListWriter(String exportFilePath2, String scriptName)
	{
		this(exportFilePath2);
		this.addWriter(scriptName);
	}

	public ListWriter(String exportFilePath2, String fileName, int n)
	{
		this(exportFilePath2);
		this.addWriters(fileName, n);
	}

	public void addWriters(String fileName, int n)
	{
		for (int i = 0; i < n; i++) {
			this.addWriter(fileName + Integer.toString(i) + ".txt");
		}
	}

	public void appendWriter(String name, String extension)
	{
		this.addWriter(name + wList.size() + extension);
	}

	public void addWriter(String exportFileName)
	{
		File exportFile;
		BufferedWriter out;

		try {
			// make path
			exportFile = new File(exportFilePath);
			exportFile.mkdirs();
			// add writer
			exportFile = new File(exportFilePath + exportFileName);
			exportFile.createNewFile();
			out = new BufferedWriter(new FileWriter(exportFile));
			wList.add(out);

		} catch (IOException e) {
			System.out.println("Error initialising the writer");
			System.out.println("ExportFilePath=" + exportFilePath);
			System.out.println("ExportFileName=" + exportFileName);
		}
	}

	public void writeToFile(String output)
	{
		this.writeToFile(output, 0);
	}

	public void writeToFile(String output, int n)
	{
		BufferedWriter writer = wList.get(n);

		try {
			writer.write(output);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			System.out.println("Something went wrong in writeToFile. Could not write: " + output);
		}
	}

	public void close()
	{
		for (int i = 0; i < wList.size(); i++) {

			try {
				wList.get(i).close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
