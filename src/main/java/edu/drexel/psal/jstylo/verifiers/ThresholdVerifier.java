package edu.drexel.psal.jstylo.verifiers;

import java.util.List;

import edu.drexel.psal.jstylo.machineLearning.Verifier;
import weka.classifiers.Classifier;
import weka.core.Instance;

public class ThresholdVerifier extends Verifier{

    private static final long serialVersionUID = 1L;
    private String result;
	private List<String> authors;
	private boolean verified;
	private double threshold;
	private Instance instance;
	private Classifier classifier;
	
	public ThresholdVerifier(Classifier c, Instance i, double thresh,List<String> auths){
		classifier = c;
		authors = auths;
		result = "no one";
		verified = false;
		instance = i;
		threshold = thresh;
	}
	
	@Override
	public void verify() {
		try {
			double[] probabilities = classifier.distributionForInstance(instance);
			double highest = -1.0;
			double secondHighest = -1.0;
			int highestIndex = -1;
			for (int i = 1; i < probabilities.length; i++){
				if (probabilities[i] > highest){
					double tempVal = highest;
					
					highest = probabilities[i];
					highestIndex = i;
					
					if (tempVal > secondHighest){
						secondHighest = tempVal;
					}
				} else if (probabilities[i] > secondHighest){
					secondHighest = probabilities[i];
				}
			}
			if ((highest-secondHighest)>threshold){
				verified = true;
				result = authors.get(highestIndex-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getResultString() {
		if (verified)
			return result+" is the author!";
		else
			return "The author could not be verified";
	}

	/*
	 * This class does not yet support meta-verification and so only returns 0
	 */
	@Override
	public double getAccuracy(){
		return 0.0;
	}
}
