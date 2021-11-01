package com.IRS.optaplanner;

import info.debatty.java.stringsimilarity.*;
import me.xdrop.fuzzywuzzy.ratios.PartialRatio;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import info.debatty.java.stringsimilarity.*;

public class Similarity {
	public static int getSimilarity(String a, String b) {
		NGram twogram = new NGram(2);
		return (int) ((1 - twogram.distance(a, b)) * 100);
	}
}
