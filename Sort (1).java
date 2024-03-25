import java.util.*;


/**
 * A class that contains a group of sorting algorithms.
 * The input to the sorting algorithms is assumed to be
 * an array of integers.
 * 
 * @author An Ha
 * @version Fall 2023
 */
public class Sort {

    // Constructor for objects of class Sort
    public Sort() {
    }


    /**
     * Given an array of integers and an integer k, sort the array
     * (ascending order) using k-way mergesort.
     * @param data  an array of integers
     * @param k     the k in k-way mergesort
     */
    public static void kwayMergesort (int[] data, int k) {
        kwayMergesortRecursive (data, 0, data.length - 1, k);
    }
    
    /**
     * The recursive part of k-way mergesort.
     * Given an array of integers (data), a low index, high index, and an integer k,
     * sort the subarray data[low..high] (ascending order) using k-way mergesort.
     * @param data  an array of integers
     * @param low   low index
     * @param high  high index
     * @param k     the k in k-way mergesort
     */
    public static void kwayMergesortRecursive (int[] data, int low, int high, int k) {
        if (low < high) {
            for (int i = 0; i < k; i++) {
                kwayMergesortRecursive (data,
                                        low + i*(high-low+1)/k,
                                        low + (i+1)*(high-low+1)/k - 1,
                                        k);
            }
            merge (data, low, high, k);
        }
    }
    

    /**
     * Given an array of integers (data), a low index, a high index, and an integer k,
     * sort the subarray data[low..high].  This method assumes that each of the
     * k subarrays  data[low + i*(high-low+1)/k .. low + (i+1)*(high-low+1)/k - 1],
     * for i = 0..k-1, are sorted.
     */
    public static void merge (int[] data, int low, int high, int k) {
    
        if (high < low + k) {
            // the subarray has k or fewer elements
            // just make one big heap and do deleteMins on it
            Comparable[] subarray = new MergesortHeapNode[high - low + 1];
            for (int i = 0, j = low; i < subarray.length; i++, j++) {
                subarray[i] = new MergesortHeapNode(data[j], 0);
            }
            BinaryHeap heap = BinaryHeap.buildHeap(subarray);
            for (int j = low; j <= high; j++) {
                try {
                    data[j] = ((MergesortHeapNode) heap.deleteMin()).getKey();
                }
                catch (EmptyHeapException e) {
                    System.out.println ("Tried to delete from an empty heap.");
                }
            }
            
        } else {
            // divide the array into k subarrays and do a k-way merge
            int[] dataIndex_in_subarray = new int[k];   // hold current index of each subarray in data
            int[] sortedArray = new int[high - low + 1]; // final array
            Comparable[] subarray = new MergesortHeapNode[k]; // make array to hold first value of each subarray

            for (int i = 0; i < k; i++) {
                int j = low + i*(high-low+1)/k;     // first index of each subarray
                dataIndex_in_subarray[i] = j;
                subarray[i] = new MergesortHeapNode(data[j] , i); // name of subarray
                // data[j] value in data at subarray index j
                // i: subarray name
            }
            BinaryHeap heap = BinaryHeap.buildHeap(subarray);
            for (int i = 0; i < high - low + 1; i++){
                MergesortHeapNode temp_Node = null;
                try {
                    temp_Node = (MergesortHeapNode) heap.deleteMin(); // put smallest node in temp_node
                } catch (EmptyHeapException e) {
                    System.out.println ("Tried to delete from an empty heap.");
                }

                sortedArray[i] = temp_Node.getKey();
                int Subarray_Num = temp_Node.getWhichSubarray();
                dataIndex_in_subarray[Subarray_Num] += 1;
                // the next item in that specific subarray after the previous one get pushed off
                int indexOfData = dataIndex_in_subarray[Subarray_Num];
                int lastIndexOfSubarrayOnData =(low + (Subarray_Num+1)*(high-low+1)/k - 1);
                // last element in data

                if (indexOfData <= lastIndexOfSubarrayOnData){
                    heap.insert(new MergesortHeapNode(data[indexOfData], Subarray_Num));
                    //put the next one into heap
                }
            }
            for (int j = low; j <= high; j++) {
                data[j] = sortedArray[j-low];
            }

            

        }
    }
    
    
    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the array are between 0 and size (inclusive) with
     * random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomArrayOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = (int) ((size + 1) * Math.random());
        }
        return data;
    }
    

    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the output array are between 0 and size-1 with
     * exactly one of each in the array.  Each permutation is generated
     * with random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomPermutationOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }
        // shuffle the array
        for (int i = 0; i < size; i++) {
            int temp;
            int swap = i + (int) ((size - i) * Math.random());
            temp = data[i];
            data[i] = data[swap];
            data[swap] = temp;
        }
        return data;
    }


    /**
     * Perform checks to see if the algorithm has a bug.
     */
    private static void testCorrectness() {
        int[] data = getRandomPermutationOfIntegers(100);

        for (int i = 0; i < data.length; i++) {
            System.out.println("data[" + i + "] = " + data[i]);
        }
        
        int k = 100;
        kwayMergesort(data, k);
        
        // verify that data[i] = i
        for (int i = 0; i < data.length; i++) {
            if (data[i] != i) {
                System.out.println ("Error!  data[" + i + "] = " + data[i] + ".");
            }
        }


        ///////////////////////////////////////
        /*
        System.out.print("DEBUG");
        for (int i = 0; i < data.length; i++) {
            System.out.println("data[" + i + "] = " + data[i]);
        }
        */

    }
    
    
    /**
     * Perform timing experiments.
     */
    private static void testTiming () {
        // timer variables
        long totalTime = 0;
        long startTime = 0;
        long finishTime = 0;

        // start the timer
        Date startDate = new Date();
        startTime = startDate.getTime();

        int n = 3200000;    // n = size of the array
        int k = 5;         // k = k in k-way mergesort
        int[] data = getRandomArrayOfIntegers(n);
        kwayMergesort(data, k);

        // stop the timer
        Date finishDate = new Date();
        finishTime = finishDate.getTime();
        totalTime += (finishTime - startTime);
        
        System.out.println("** Results for k-way mergesort:");
        System.out.println("    " + "n = " + n + "    " + "k = " + k);
        System.out.println("    " + "Time: " + totalTime + " ms.");
    }
    
    
    /**
     * code to test the sorting algorithms
     */
    public static void main (String[] argv) {
        testCorrectness();
        testTiming();
    }
}
