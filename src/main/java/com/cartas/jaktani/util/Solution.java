package com.cartas.jaktani.util;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Result {

    /*
     * Complete the 'fizzBuzz' function below.
     *
     * The function accepts INTEGER n as parameter.
     */

    public static void fizzBuzz(int n) {
      for(int i=1; i<=n; i++) {
    	  if(i%3==0 && i%5==0) {
    		  System.out.println("FizzBuzz");
    	  }else if(i%3==0) {
    		  System.out.println("Fizz");
    	  }else if(i%5==0) {
    		  System.out.println("Buzz");
    	  }else {
    		  System.out.println(i);
    	  }
      }
      //divisibility of strings
      String sentence = "aa djdjd didi aa dkdkd dskdjsn";
      if(sentence!=null && sentence!="" && sentence.contains("\\s")){
          String[] strList = sentence.split("\\s");
          for(int i=0; i<strList.length; i++){
              for(int j=0; j<strList.length; j++){
            	  System.out.println(strList[i] + i);
            	  System.out.println(strList[j] + j);
            	  System.out.println();
                  if(j!=i && strList[j].equals(strList[i])){
                  	System.out.println(strList);
                  }
              }
          }            
      }
      
      List<Integer> arr = new ArrayList<>();
      arr.add(4); arr.add(6); arr.add(5); arr.add(5); arr.add(8); arr.add(9); arr.add(3);
      List<Integer> arrResult = new ArrayList<>();
      List<Integer> a = new ArrayList<>();
      List<Integer> b = new ArrayList<>();
      if(arr!=null && arr.size()>0 && arr.size()>2){
           Integer arrLength = arr.size();
           Integer subLength = 0;
           if(arrLength%2==0){
               subLength = arrLength/2;
               for(int i=0; i<subLength; i++){
                  a.add(arr.get(i)); 
               }
               for(int i=arrLength-1; i>=subLength; i++){
                  b.add(arr.get(i)); 
               }
           }else{
               subLength = (arrLength-1)/2;
               Integer midValue = arr.get(subLength++);
               for(int i=0; i<subLength; i++){
                  a.add(arr.get(i)); 
               }
               for(int i=arrLength-1; i>=subLength; i++){
                  b.add(arr.get(i)); 
               }
               
               System.out.println();
           }
           
       }else{
          System.out.println();
       }

    }
    
   

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        Result.fizzBuzz(n);

        bufferedReader.close();
    }
}