/**
 * @classname: ConfusionMatrix
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description Compute the confustion matrix from the result file and validate file.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import com.opencsv.CSVReader;


public class ConfusionMatrix {

	/**
	 * @name: main
	 * @description: Compute the confustion matrix from the result file and validate file.
	 * @param: file <validateFile> containing the validation dataset <predictionFile> file containing our prediction.
	 * @return: Confusion matrix and accuracy
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		if (args.length != 2) {
			System.out.println("Wrong Syntax: ConfusionMatrix <98validateFilePath> <RpredictionFilePath>");
			System.out.println("Provide fully qualified path");
			System.exit(-1);
		}

		HashMap<String,Boolean> predictedHash = new HashMap<String,Boolean>();
		HashMap<String,Boolean> validateHash = new HashMap<String,Boolean>();

		double TP = 0.0;
		double TN = 0.0;
		double FP = 0.0;
		double FN = 0.0;

		double precision = 0.0;
		double recall = 0.0;
		double accuracy = 0.0;

		String  validateFile = args[0];
		String predictFile = args[1];

		predictedHash = loadPredictedHash(predictFile);
		validateHash = loadvalidateHash(validateFile);

		// computing the confusion matrix.
		for (Map.Entry<String, Boolean> entry : predictedHash.entrySet()) {
			if(validateHash.containsKey(entry.getKey())){
				Boolean data1Value = entry.getValue();
				Boolean data2Value = validateHash.get(entry.getKey());

				if(data1Value == true && data2Value == true){
					++TP;
				}else if(data1Value == false && data2Value == false){
					++FN;
				}else if(data1Value == true && data2Value == false){
					++TN;
				}else if(data1Value == false && data2Value == true){
					++FP;
				}
			}
		}

		// calculate the accuracy based on the criteria provided in assignment description.
		System.out.println("TP : "+TP+" TN : "+TN+" FP : "+FP+" FN : "+FN);
		accuracy = FP/(FP + TP) + TN/(FN + TN);
		System.out.println("Precision : "+precision+" Recall : "+recall+" Accuracy : "+accuracy);
	}

	/**
	 * @name: loadvalidateHash
	 * @description: Loading the validation file into a hashmap.
	 * @param: file <validateFile> containing the validation dataset 
	 * @return: hashmap containing the validation data in a hashmap.
	 */
	private static HashMap<String, Boolean> loadvalidateHash(String validateFile) throws FileNotFoundException, IOException {

		HashMap<String,Boolean> validateHash = new HashMap<String,Boolean>();
		CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(validateFile)))));
		int count = 0;
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			if (nextLine.length == 2) {
				if(nextLine[1].equals("TRUE")){
					validateHash.put(nextLine[0], true);
				}
				else if(nextLine[1].equals("FALSE")){
					validateHash.put(nextLine[0], false);
				}
				++count;
			}
		}
		System.out.println("Loaded Validate Hash with count : "+count);
		return validateHash;
	}

	/**
	 * @name: loadPredictedHash
	 * @description: Loading the prediciton file into a hashmap. Our prediciton data contained serverl nuiances hence string operations.
	 * @param: file <predictionFile> file containing our prediction.
	 * @return: hashmap containing the prediction data in a hashmap.
	 */
	private static HashMap<String, Boolean> loadPredictedHash(String predictFile) throws FileNotFoundException, IOException {

		HashMap<String,Boolean> predictHash = new HashMap<String,Boolean>();
		CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(predictFile)))));
		int count = 0;
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			if (nextLine.length == 2 && !nextLine[1].equals("x")) {

				String keyValue[] = nextLine[1].split(",");
				String fnDtCarr[] = keyValue[0].split("_");

				fnDtCarr[0] = fnDtCarr[0].replaceAll("\\s","");
				String dt[] = fnDtCarr[1].split("-");
				fnDtCarr[2] = fnDtCarr[2].replaceAll("\\s","");

				if(dt[1].length() == 1){
					dt[1] = "0" + dt[1];
				}

				dt[2] = dt[2].replaceAll("\\s","");
				if(dt[2].length() == 1){
					dt[2] = "0" + dt[2];
				}

				if(fnDtCarr[2].length() < 4){
					for(int i = 0; i < 4 - fnDtCarr[2].length(); i++){
						fnDtCarr[2] = "0" + fnDtCarr[2];
					}
				}
				String data = fnDtCarr[0]+"_"+dt[0]+"-"+dt[1]+"-"+dt[2]+"_"+fnDtCarr[2];

				if(keyValue[1].equals(" TRUE")){
					predictHash.put(data, true);
					count++;
				}
				else if(keyValue[1].equals("FALSE")){
					predictHash.put(data, false);
					count++;
				}

			}
		}
		System.out.println("Loaded Predicted Hash with count : "+count);
		return predictHash;
	}

}
