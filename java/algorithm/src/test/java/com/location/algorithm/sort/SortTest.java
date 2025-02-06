package com.location.algorithm.sort;


import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class SortTest {

    @Test
    public void testBubblingSort() {
        int[] array = {10, 8, 3, 2, 9};
        Sort.bubblingSort(array);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, array);
    }

    @Test
    public void testBubblingSortRef() {
        int[] array = {10, 8, 3, 2, 9};
        Sort.bubblingSortRef(array);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, array);
    }

    @Test
    public void testInsertSort() {
        int[] array = {10, 8, 3, 2, 9};
        Sort.insertSort(array);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, array);
    }

    @Test
    public void testInsertSortRef() {
        int[] array = {10, 8, 3, 2, 9};
        Sort.insertSortRef(array);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, array);
    }

    @Test
    public void testSelectSort() {
        int[] array = {10, 8, 3, 2, 9};
        Sort.selectSort(array);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, array);
    }

    @Test
    public void testMergeSort() {
        int[] array = {10, 8, 3, 2, 9};
        int[] sortedArray = Sort.mergeSort(array, 0, array.length - 1);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, sortedArray);
    }

    @Test
    public void testQuickSort() {
        int[] array = {10, 8, 3, 2, 9};
        Sort.quickSort(array, 0, array.length - 1);
        assertArrayEquals(new int[]{2, 3, 8, 9, 10}, array);
    }
}