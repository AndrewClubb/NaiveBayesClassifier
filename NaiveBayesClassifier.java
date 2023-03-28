import java.util.Scanner;

public class NaiveBayesClassifier {
	private int numberOfTrainingInstances;
	private int attributesPerRow;
	private int numberOfClassValues;
	private int numberOfTestInstances;
	private ClassNode[] trainingDataList;
	private ClassNode[] testDataList;
	private String[] classValues;
	
	public static void main(String[] args) {
		NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
	}
	
	public NaiveBayesClassifier() {
		Scanner reader = new Scanner(System.in);
		
		//System.out.println("Enter the test data");
		String token[] = reader.nextLine().split(" ");
		
		this.numberOfTrainingInstances = Integer.parseInt(token[0]);
		this.attributesPerRow = Integer.parseInt(token[1]);
		this.numberOfClassValues = Integer.parseInt(token[2]);
		
		trainingDataList = new ClassNode[this.numberOfTrainingInstances];
		classValues = new String[this.numberOfClassValues];
		
		this.fillTrainingData();
		
		token = reader.nextLine().split(" ");
		this.numberOfTestInstances = Integer.parseInt(token[0]);
		testDataList = new ClassNode[this.numberOfTestInstances];
		
		this.fillTestData();
		
		this.calcClasses();
		
		this.outputTestResults();
	}
	
	private void outputTestResults() {
		for(int testIndex = 0; testIndex < this.numberOfTestInstances; testIndex++)
			System.out.println("Test instance:" + attributeString(testDataList[testIndex]) + ", Classification: " + testDataList[testIndex].getClassValue());
	}
	
	private void calcClasses() {
		for(int testIndex = 0; testIndex < this.numberOfTestInstances; testIndex++) {			
			double bestClassChance = -1;
			String bestClassStr = "";

			for(int classIndex = 0; classIndex < this.numberOfClassValues; classIndex++) {
				double currentClassChance = 1;
				String currentClassStr = classValues[classIndex];
				double numInClass;
				
				numInClass = 0;
				for(int x = 0; x < this.numberOfTrainingInstances; x++)
					if(trainingDataList[x].getClassValue().contentEquals(currentClassStr))
						numInClass++;
				currentClassChance *= (numInClass / this.numberOfTrainingInstances);
				
				for(int a = 0; a < this.attributesPerRow; a++) {
					int attributeCounter = 0;
					for(int x = 0; x < this.numberOfTrainingInstances; x++)
						if(trainingDataList[x].getClassValue().contentEquals(currentClassStr) && trainingDataList[x].getAttribute(a).contentEquals(testDataList[testIndex].getAttribute(a)))
							attributeCounter++;
					currentClassChance *= (attributeCounter / numInClass);
				}
				
				if(currentClassChance > bestClassChance) {
					bestClassChance = currentClassChance;
					bestClassStr = currentClassStr;
				}
			}
			testDataList[testIndex].setClassValue(bestClassStr);
		}
	}
	
	private String attributeString(ClassNode node) {
		String tempStr = "";
		
		for(int i = 0; i < this.attributesPerRow; i++)
			tempStr += " " + node.getAttribute(i);
		
		return tempStr;
	}
	
	private void fillTrainingData() {
		Scanner reader = new Scanner(System.in);
		ClassNode tempNode;
		String token[];
		Boolean classValueFound;
		
		for(int i = 0; i < this.numberOfTrainingInstances; i++) {
			tempNode = new ClassNode(this.attributesPerRow);
			
			token = reader.nextLine().split(" ");
			
			for(int a = 0; a < this.attributesPerRow; a++) {
				tempNode.addAttribute(token[a], a);
			}
			
			tempNode.setClassValue(token[attributesPerRow]);
			
			classValueFound = false;
			for(int c = 0; c < this.numberOfClassValues && !classValueFound; c++) {
				if(classValues[c] == null) {
					classValues[c] = token[attributesPerRow];
					classValueFound = true;
				}
				else if(classValues[c].contentEquals(token[attributesPerRow]))
					classValueFound = true;
			}
			
			trainingDataList[i] = tempNode;
		}
	}
	
	private void fillTestData() {
		Scanner reader = new Scanner(System.in);
		ClassNode tempNode;
		String token[];
		
		for(int i = 0; i < this.numberOfTestInstances; i++) {
			tempNode = new ClassNode(this.attributesPerRow);
			token = reader.nextLine().split(" ");
			
			for(int a = 0; a < this.attributesPerRow; a++)
				tempNode.addAttribute(token[a], a);
			
			testDataList[i] = tempNode;
		}
	}
	
	private class ClassNode {
		private String[] attributes;
		private String classValue;
		
		public ClassNode(int numberOfAttributes) {
			attributes = new String[numberOfAttributes];
		}
		
		public void addAttribute(String attribute, int index) {
			attributes[index] = attribute;
		}
		
		public String getAttribute(int index) {
			return attributes[index];
		}

		public String getClassValue() {
			return classValue;
		}

		public void setClassValue(String classValue) {
			this.classValue = classValue;
		}
	}
}
