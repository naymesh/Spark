
package org.generationcp.analytics;

import static spark.Spark.post;
import static spark.SparkBase.port;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.generationcp.analytics.xml.MainDesign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import au.com.bytecode.opencsv.CSVReader;

public class Spark {

	private static final Logger LOG = LoggerFactory.getLogger(Spark.class);

	public static void main(String[] args) throws IOException, JAXBException {
		port(19080);
		post("/expdesign", (request, response) -> {
			return runBVDesign(request);
		}, new JsonTransformer());
	}

	private static Object runBVDesign(Request request) throws JAXBException, IOException, FileNotFoundException {
		MainDesign mainDesign = toMainDesign(request.body());
		int returnCode = -1;
		
		ProcessBuilder pb = new ProcessBuilder("C:\\Breeding Management System\\tools\\breeding_view\\bin\\BVDesign.exe", "-i" + writeToFile(request.body()));
		Process p = pb.start();
		try {
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String lineRead;
			while ((lineRead = br.readLine()) != null) {
				LOG.debug(lineRead);
			}

			returnCode = p.waitFor();
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (p != null) {
				p.getInputStream().close();
				p.getOutputStream().close();
				p.getErrorStream().close();
			}
		}

		List<String[]> myEntries = new ArrayList<String[]>();
		String outputFileName = mainDesign.getDesign().getParameterValue("outputfile");

		if (returnCode == 0) {
			File outputFile = new File(outputFileName);
			FileReader fileReader = new FileReader(outputFile);
			CSVReader reader = new CSVReader(fileReader);
			myEntries = reader.readAll();
			fileReader.close();
			reader.close();
		}
		return myEntries;
	}

	private static String writeToFile(String xml) {
		String filenamePath = new File(System.currentTimeMillis() + "-bv-input.xml").getAbsolutePath();
		try {
			File file = new File(filenamePath);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(xml);
			output.close();
			filenamePath = file.getAbsolutePath();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return filenamePath;
	}

	public static MainDesign toMainDesign(String xmlString) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(MainDesign.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (MainDesign) unmarshaller.unmarshal(new StringReader(xmlString));
	}

}
