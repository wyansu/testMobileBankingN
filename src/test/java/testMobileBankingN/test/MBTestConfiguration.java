package testMobileBankingN.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class MBTestConfiguration implements TestConfiguration{
	
	private JSONObject capabilities; //android driver capabilites
	private int elementSearchTimeout;
	private int nextSequence; //next run sequence test
	
	private Workbook workbook;
	private Sheet sheet;
	private OutputStream fileout;
	
	private List<HashMap<String,String>> testParam;
	private List<String> header;
	
	private String fileInName;
	private String fileOutName;
	private String sheetName;
	
	private ResourceBundle resource;
	private Locale locale;
	
	
	public MBTestConfiguration(String fileIn, String fileOut, String sheetName)  {
		this.fileInName = fileIn;
		this.fileOutName = fileOut;
		this.sheetName = sheetName;
		
		populateTestConfiguration();
		populateTestParameter();
	}

	@Override
	public int getParameterCount() {
		return testParam.size();
	}

	@Override
	public void setNextSequence(int sequence) {
		nextSequence = sequence;
	}
	
	public int getSequence() {
		return nextSequence;
	}
 	
	
	private void populateTestConfiguration()   {
		JSONParser parser = new JSONParser();
		
		Reader reader;
		try {
			reader = new FileReader(new File("src/test/resources/config/config.json"));
			JSONObject configJSON = (JSONObject) parser.parse(reader);
			
			capabilities = (JSONObject) configJSON.get("capabilities");
			
			Long timeout = (Long) configJSON.get("elementSearchTimeout");
			elementSearchTimeout = Math.toIntExact(timeout);
			
			String languageTest = (String) configJSON.get("languageTest");
			locale = new Locale(languageTest, "ID");
			
			resource = ResourceBundle.getBundle("lang/String", locale);
			resource.clearCache(); //we dont need cache since this only test
			System.out.println("Info: Test running with language set to: " + resource.getLocale());
			
			reader.close();

		} catch (Exception e) {
			
			
			System.err.println("Error: failed to read file config");
		}
		
		
	}
	
	
	public JSONObject getCapabilities() {
		return capabilities;
	}
	
	public int getElementSearchTimeout() {
		return elementSearchTimeout;
	}
	
	/**
	 * Get all parameter test from excel file
	 * */
	public void populateTestParameter() {
		
		try {

			workbook = WorkbookFactory.create(new File(this.fileInName));
			
			fileout = new FileOutputStream(this.fileOutName);

			sheet = workbook.getSheet(sheetName);
			testParam = new ArrayList<HashMap<String, String>>();
			header = new ArrayList<String>();
			
			//sheet header as key HashMap
			Row row0 = sheet.getRow(0);
			
			for(Cell cell : row0 ) {
				header.add(cell.getStringCellValue());
			}
			
			for(Row row : sheet) {
				if(row.getRowNum() == 0) continue;
				
				HashMap<String, String> rowParam = new HashMap<String, String>();
				for(int col=0; col<row0.getLastCellNum() ; col++) {
					Cell cell = row.getCell(col);
					if(cell == null) {
						rowParam.put(row0.getCell(col).getStringCellValue(), "");
					}
					else {
						rowParam.put(row0.getCell(col).getStringCellValue(), row.getCell(col).getStringCellValue());
					}

				}
				
				testParam.add(rowParam);
			}

		}catch(Exception e) {
			
			System.err.println("Error: error populating test parameter from file.");
			e.printStackTrace();
		}
		
	}
	
	/*
	 * retrieve parameter for current sequence test case
	 * **/
	public String getParameterValue(String parameterName) {
		return testParam.get(this.nextSequence - 1).get(parameterName);
	}
	
	/*
	 * close all resource from afterClass at the end of all test
	 * */
	public void close() {
		try {
			workbook.write(fileout);
			workbook.close();
			fileout.close();
		}
		catch (Exception e) {
			System.err.println("Error: error writing file output.");
			e.printStackTrace();
		}
		
	}
	
	 /* Save screenshot to fileout workbook in field Printscreen when this field exist in config file
	 * **/	
	public void saveScreenshot(String columnName, byte[] scrFile ) {
		
		int col = header.indexOf(columnName);
		
		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(col);
		anchor.setRow1(this.nextSequence);
		int pictureIdx = workbook.addPicture(scrFile, Workbook.PICTURE_TYPE_PNG);
		
		Picture pic = drawing.createPicture(anchor, pictureIdx);
		pic.resize();
		pic.resize(0.3);
		
	}
	
	
	public void saveScreenshot(int colNumber, byte[] scrFile ) {
		
		int col = colNumber;
		
		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(col);
		anchor.setRow1(this.nextSequence);
		int pictureIdx = workbook.addPicture(scrFile, Workbook.PICTURE_TYPE_PNG);
		
		Picture pic = drawing.createPicture(anchor, pictureIdx);
		pic.resize();
		pic.resize(0.3);
		
	}
	
	public void writeToTestOuput(String columnName, String word) {
		
		int col = header.indexOf(columnName);
		if(col == -1) return;
		
		Row row = sheet.getRow(this.nextSequence);
		Cell cell = row.createCell(col);
		cell.setCellValue(word);
		
	}
	
	//return case name for the sequence
	@Override
	public String getTestCaseName(int sequence) {
		return testParam.get(sequence -1).get("Case");
	}
	
	public String getResourceValue(String resource) {
		return this.resource.getString(resource);
	}
	
	public Locale getLocale() {
		return locale;
	}

}
