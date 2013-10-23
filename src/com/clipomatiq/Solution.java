package com.clipomatiq;

import java.util.Arrays;

/*
 * 
 * Description.
 * 		In this exercise Given sorted array, so optimal solution to find index of first appearance of k is 
 * 		check the value in the center of current segment of array. If this value less than k than shift 
 *      low border of seqment to this index, else shift high border of segment. So we can quickly find 
 *      position of k appearences.
 *      
 *      Oleg Baskakov (2011) baskakov.oleg@gmail.com
 */

public class Solution {

	/**
	 * @param args
	 */
	
	static final int ARRAY_LENGHT=20000000;
	static final int MAX_NUMBER=10000000;
	static final int FIND_NUMBER=3;
	
	public static void main(String[] args) {
		
		int[] a=new int[ARRAY_LENGHT];
		//System.out.print("a[]=");
		
		for (int i = 0; i < ARRAY_LENGHT; i++) {
			a[i]=(int) (Math.random()*(MAX_NUMBER+1));
		//	System.out.print(a[i]+"| ");
		}

		long startTime=System.currentTimeMillis();
		long endTime;
		Arrays.sort(a);
		endTime=System.currentTimeMillis();
		System.out.println("sorting time="+(endTime-startTime));
		//I've commented this output due overloading 
		//System.out.println("\nsorted:");
//		for (int i = 0; i < ARRAY_LENGHT; i++) {
//			System.out.print("["+i+"]"+a[i]+"| ");
//		}

		startTime=System.currentTimeMillis();
		int result=findNumberOfOccurences(a, FIND_NUMBER);
		endTime=System.currentTimeMillis();
		System.out.println("\nresult:"+result);
		System.out.println("finding time="+(endTime-startTime));
	}
	
	static int findNumberOfOccurences(int[] a, int k) {
		int result=0;
		boolean running=true;
		if(a==null||a.length==0)
			return result;
		int highIndex=a.length;
		int lowIndex=0;
		int curIndex=0;
		while(running) {
			//some trick to speed up dividing, but actually this method run quite fast without optimisation )
			curIndex=lowIndex+(highIndex-lowIndex)>>2;
			if(curIndex==lowIndex) {
				//check a case when array begin with k (a[0]=k)
				if(a[lowIndex]==k) {
					return countOccurances(k,a,highIndex)+1;
				}else {
					return countOccurances(k,a,highIndex);
				}
			}
				
			if(a[curIndex]<k) {
				lowIndex=curIndex;
			}else {
				highIndex=curIndex;
			}
		}
	        // write your code here
		return result;
	}

	private static int countOccurances(int k, int[] a, int curIndex) {

		int result=0;
		int len=a.length;
		if(a[curIndex]==k) {
			while(len>curIndex&&a[curIndex++]==k) {
				result++;
				
			}
		}
		
		return result;

	}

}
