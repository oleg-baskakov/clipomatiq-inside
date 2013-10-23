package com.clipomatiq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;

/*
 * 
 * Description:
 * 		To solve this exercise I split task on two part:
 * 		1. Find all arrangements for cubes (6 numbers each). createListOfArrangements() reccuring method
 * 		2. Define suitable  pairs of these arangements which applies initial conditions. findDistinctPairs() method
 * 		
 * Note. I use optimisation for code in cause of 6=9 condition and use 6 for 6 and 9, assuming, that resulting arrangement
 *  which include 6 including and 9 too.
 *  
 *      Oleg Baskakov (2011) baskakov.oleg@gmail.com
*/

public class Solution2 {

	// 01, 04, 09, 16, 25, 36, 49, 64, 81
	// With assumption of 6 equal 9 replace all 9 by 6
	// 01, 04, 06, 16, 25, 36, 46, 64, 81

	public static final String[][] VALUES = { { "0", "1" }, { "0", "4" },
			{ "0", "6" }, { "1", "6" }, { "2", "5" }, { "3", "6" },
			{ "4", "6" }, { "8", "1" } };
	// and we can throw a 9 number (instead using 6)
	public static final String[] NUMBERS = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8" };
	// Object-oriented programming assumes to use new class for Cube object.
	// This exercise have to show
	// algorithmic approaches instead of object-oriented. Anyway it could by
	// easily completed by this way.
	static HashSet<String> cube = new HashSet<String>();
	static HashSet<String> distinctCubesStr = new HashSet<String>();
	static ArrayList<HashSet<String>> cubes = new ArrayList<HashSet<String>>();

	public static void main(String[] args) {

		findDistinctArragements();
	}

	static void findDistinctArragements() {

		//5-is a length of arrangement to check (0-5) is 6 numbers.
		createListOfArrangements(5, cube);

		System.out.println("created list of arangement");

		ArrayList<ArrayList<HashSet<String>>> pairs = findDistinctPairs();
		System.out.println("Total:" + pairs.size());

	}

	private static ArrayList<ArrayList<HashSet<String>>> findDistinctPairs() {

		int counter;
		TreeMap<String, ArrayList<HashSet<String>>> pairs = new TreeMap<String, ArrayList<HashSet<String>>>();
		ArrayList<HashSet<String>> pair;
		
		//keys for checking distinct arrangements in map
		String cube1Key;
		String cube2Key;

		for (HashSet<String> cube1 : cubes) {
			for (HashSet<String> cube2 : cubes) {
				counter = 0;
				for (String[] value : VALUES) {
					//checking square values formation by 2 cubes
					if (!((cube1.contains(value[0]) && cube2.contains(value[1])) || (cube1
							.contains(value[1]) && cube2.contains(value[0])))) {
						break;
					}
					counter++;
				}
				//found suitable pair of arrangements. Cubes with these arrangements can form all squre numbers
				if (counter == 8) {
					cube1Key = cube1.toString();
					cube2Key = cube2.toString();
					//checking for uniqueness 
					if (!pairs.containsKey(cube1Key + cube2Key)
							&& !pairs.containsKey(cube2Key + cube1Key)) {
						pair = new ArrayList<HashSet<String>>();
						pair.add(cube1);
						pair.add(cube2);
						pairs.put(cube1Key + cube2Key, pair);
						System.out.println("found pairs: " + cube1Key + " and "
								+ cube2Key + "");

					} else {
						// System.out.println("dublicate:"+cube1Key+cube2Key);
					}
				}
			}

		}

		return new ArrayList<ArrayList<HashSet<String>>>(pairs.values());
	}

	// Reccuring function with 'a' depth. In this solution depth=6 (0-5)
	private static HashSet<String> createListOfArrangements(int a,
			HashSet<String> cube) {

		for (int i = 0; i < NUMBERS.length; i++) {
			if (cube.contains(NUMBERS[i])) {
				continue;
			}
			cube.add(NUMBERS[i]);
			if (a <= 0) {
				if (cube.size() == 6 && checkDistinction(cube)) {
					cubes.add(cube);
					cube = new HashSet<String>(cube);
				}
			} else
				cube = createListOfArrangements(a - 1, cube);
			cube.remove(NUMBERS[i]);
		}
		return cube;
	}

	//for optimisation I convert cube values to array, sort it and form string value for hashset.
	private static boolean checkDistinction(HashSet<String> cube2) {
		boolean result = false;

		String cubeStr = "";
		String[] numStr = new String[cube2.size()];
		cube2.toArray(numStr);
		Arrays.sort(numStr);
		for (String val : numStr) {
			cubeStr += val;
		}

		if (!distinctCubesStr.contains(cubeStr)) {
			distinctCubesStr.add(cubeStr);
			result = true;
		}
		return result;
	}
}
