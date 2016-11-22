import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFReader 
{
	private static PDFParser parser;
	private static PDFTextStripper splitter;
	private static PDDocument pdDoc;
	private static COSDocument cosDoc;
	
	private static String filePath;
	private static File file;
	
	public static void main(String args[]) throws IOException
	{
		System.out.println("Create a new PDF File with a blank page.");
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter the file path for the pdf: ");
		filePath = input.next();//set the file path for the program to use to read the pdf
		
		String pdfText = toText();
		//System.out.println(pdfText);	
		
		System.out.println(significantInfo());
	}
	
	public static String toText() throws FileNotFoundException, IOException
	{
		 splitter = null;
	     pdDoc = null;
	     cosDoc = null;
		
	     file = new File(filePath);
	     parser = new PDFParser(new RandomAccessFile(file, "r"));
	     
	     parser.parse();
	     
	     cosDoc = parser.getDocument();
	     splitter = new PDFTextStripper();
	     
	     pdDoc = new PDDocument(cosDoc);
	     int numPages = pdDoc.getNumberOfPages();
	     splitter.setStartPage(1);
	     splitter.setEndPage(numPages);
	          
		return splitter.getText(pdDoc);
	}
	
	public static String significantInfo() throws IOException
	{
		String pdfText = "";
		String text = splitter.getText(pdDoc).replace("\n", "").replace("\r", "");
		String[] pieces = text.split(" ");
		
		for(int i = 0; i < pieces.length; i++)
		{
			
			if(pieces[i].matches("\\d{2}\\/\\d{2}\\/\\d{4}") && (i+1)< pieces.length)
			{
				pdfText += "Purchase Price : "+ pieces[i-1]+"\n";
				pdfText += "Est. Closing Date : " + pieces[i] + "\n";
				pdfText += "Intrest Rate : " + pieces[i+1] + "\n";
			}
			
			//pdfText += pieces[i] + "\n";
		}
		return pdfText;
	}
	
}
