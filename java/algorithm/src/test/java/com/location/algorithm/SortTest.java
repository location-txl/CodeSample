package com.location.algorithm;

import static org.junit.Assert.assertArrayEquals;

import com.location.algorithm.sort.Sort;

import org.junit.Before;
import org.junit.Test;

public class SortTest {
    private int[] emptyArray;
    private int[] singleElementArray;
    private int[] sortedArray;
    private int[] reverseSortedArray;
    private int[] randomOrderArray;
    private int[] arrayWithDuplicates;

    @Before
    public void setUp() {
        emptyArray = new int[]{};
        singleElementArray = new int[]{1};
        sortedArray = new int[]{1, 2, 3, 4, 5};
        reverseSortedArray = new int[]{5, 4, 3, 2, 1};
        randomOrderArray = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        arrayWithDuplicates = new int[]{3, 3, 2, 2, 1, 1};
    }

    @Test
    public void bubblingSort_EmptyArray_ShouldRemainEmpty() {
        Sort.bubblingSort(emptyArray);
        assertArrayEquals(new int[]{}, emptyArray);
    }

    @Test
    public void bubblingSort_SingleElementArray_ShouldRemainUnchanged() {
        Sort.bubblingSort(singleElementArray);
        assertArrayEquals(new int[]{1}, singleElementArray);
    }

    @Test
    public void bubblingSort_SortedArray_ShouldRemainUnchanged() {
        Sort.bubblingSort(sortedArray);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, sortedArray);
    }

    @Test
    public void bubblingSort_ReverseSortedArray_ShouldBeSorted() {
        Sort.bubblingSort(reverseSortedArray);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, reverseSortedArray);
    }

    @Test
    public void bubblingSort_RandomOrderArray_ShouldBeSorted() {
        Sort.bubblingSort(randomOrderArray);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9}, randomOrderArray);
    }

    @Test
    public void bubblingSort_ArrayWithDuplicates_ShouldBeSorted() {
        Sort.bubblingSort(arrayWithDuplicates);
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, arrayWithDuplicates);
    }
}