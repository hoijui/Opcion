/*
 * QuickSort.java
 *
 * Created on 25 April 2004, 08:11
 */

package FontViewer.util;

/**
 *
 * @author  Paul
 */
public class QuickSort {
    public static void sort(String[] array, int start, int end, boolean ascending) {
	// more than one element
	if (end > start) {
	    String pivot = array[start];
	    int left  = start+1;
	    int right = end;

	    boolean finished = false;
	    while (!finished) {
                if (!ascending) {
                    while (left <= end && array[left].compareToIgnoreCase(pivot) >= 0 ) {
                        ++left;
                    }
                    while (right > start && array[right].compareToIgnoreCase(pivot) < 0) {
                        --right;
                    }
                } else {
                    while (left <= end && array[left].compareToIgnoreCase(pivot) <= 0 ) {
                        ++left;
                    }
                    while (right > start && array[right].compareToIgnoreCase(pivot) > 0) {
                        --right;
                    }
                }

		if (left >= right) {
		    finished = true;
		} else {
		    swap(array, left, right);
		}
	    }

	    // update pivot
	    swap(array, start, right);
	    // sort left and right partition
	    sort(array, start, right-1, ascending);
	    sort(array, right+1, end, ascending);
	}
    }

    public static void swap(String[] a, int x, int y) {
	String t = a[x];
	a[x] = a[y];
	a[y] = t;
    }
    
    public static void sort(String[] array, boolean ascending) {
	sort(array, 0, array.length-1, ascending);
    }
}
