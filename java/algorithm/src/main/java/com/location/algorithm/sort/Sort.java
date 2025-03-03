package com.location.algorithm.sort;

import java.util.Arrays;

public class Sort {
    /**
     * 冒泡排序
     */
    public static void bubblingSort(int[] dataArray){
        final int len = dataArray.length;
        for(int i = 0; i< len; i++){
            for(int j = 0; j < len - i - 1; j++){
                if(dataArray[j] > dataArray[j+1]){
                    int temp = dataArray[j];
                    dataArray[j] = dataArray[j+1];
                    dataArray[j+1] = temp;
                }
            }
        }
    }

    /**
     * 冒泡排序的优化版本 当没有数据交换时提前结束循环 关键查看exchange变量
     * @param dataArray
     */
    public static void bubblingSortRef(int[] dataArray){
        final int len = dataArray.length;
        boolean exchange;
        for(int i = 0; i< len; i++){
            exchange = false;
            for(int j = 0; j < len - i - 1; j++){
                if(dataArray[j] > dataArray[j+1]){
                    exchange = true;
                    int temp = dataArray[j];
                    dataArray[j] = dataArray[j+1];
                    dataArray[j+1] = temp;
                }
            }
            if(!exchange){
                //没有数据交换 提前结束
                break;
            }
        }
    }


    /**
     * 插入排序
     * 1.从前往后遍历未排序的数据 如果当前数据比已经排序的数据大 则插入到已经排序的数据后面 否则往
     * @param array
     */
    public static void insertSort(int[] array){
        /**
         * 已经排好序的索引 包含当前 默认为0
         */
        int sortIndex = 0;
        final int len = array.length;
        for(int i = sortIndex + 1; i < len; i++){
            //第一层for循坏 遍历后面未排序的数据
            boolean insert = false;//是否插入
            for(int j = sortIndex; j >= 0; j--){
                //第二层for循坏 遍历已经排序的数据
                //如果当前数据比已经排序的数据大 则插入到已经排序的数据后面
                if(array[i] > array[j]){
                    //要插入到j的后面
                    insert = true;
                    //如果j+1 == i 说明不需要插入 位置已经正确
                    if(j + 1 == i){
                        break;
                    }
                    //插入到j的后面
                    int temp = array[i];
                    //从排序的最后一个开始往后移动 j+1是要插入的位置
                    for(int k = sortIndex; k >= j +1; k--){
                        array[k + 1] = array[k];
                    }
                    //插入数据
                    array[j + 1] = temp;
                    //插入完成
                    break;
                }
            }
            //如果没有插入 则将当前数据插入到最前面
            if(!insert && i != 0){
                int temp = array[i];
                for(int k = i - 1; k >= 0; k--){
                    array[k + 1] = array[k];
                }
                array[0] = temp;
            }
            //已经排序的索引+1
            sortIndex++;
        }
    }


    /**
     * 插入排序的优化版本
     * 1.将要插入的数据保存到临时变量
     * 2.从后往前遍历已经排序的数据 如果当前数据比临时变量大 则将当前数据往后移动一位
     * 3.如果当前数据比临时变量小 则将临时变量插入到当前数据的后面
     * @param array
     */
    public static void insertSortRef(int[] array){
        final int len = array.length;
        for(int i = 1; i < len; i++){
            int temp = array[i];
            int j = i - 1;
            for(; j >= 0; j--){
                if(array[j] > temp){
                    array[j + 1] = array[j];
                }else{
                    break;
                }
            }
            array[j + 1] = temp;
        }

    }


    /**
     * 选择排序 分为未排序空间和已排序空间
     * 每次循环从未排序的空间中选择最小值放到已排序空间的末尾
     * eg
     * 10,8,3,2,9 原始数据
     * 2,8,3,10,9 2和10交换
     * 2,3,8,10,9 3和8交换
     * 2,3,8,10,9 没有交换
     * 2,3,8,9,10 10和9没有交换
     * 2,3,8,9,10 没有交换
     * 未排序空间已经没有数据 排序完成
     *
     * @param array
     */
    public static void selectSort(int[] array){
        int noSortStartIndex = 0;
        int len = array.length;
        while (noSortStartIndex <= len - 1){
            int minIndex = noSortStartIndex;
            int minValue = array[noSortStartIndex];
            for(int i = noSortStartIndex + 1; i < len; i++){
                if(array[i] < minValue){
                    minIndex = i;
                    minValue = array[i];
                }
            }
            if(minIndex != noSortStartIndex){
                array[minIndex] = array[noSortStartIndex];
                array[noSortStartIndex] = minValue;
            }
            noSortStartIndex++;
        }
    }

    /**
     * 归并排序 自己根据算法导论写的
     * p是数组的开始索引 r是数组的结束索引
     * merge_sort(p…r) = merge(merge_sort(p…q), merge_sort(q+1…r))
     * p >= r 不用再继续分解
     *
     * 1.将数组分为两部分
     * 2.将两部分分别排序
     * 3.将两部分合并
     * 采用递归的方式
     * 当分到最小的时候 也就是只有一个元素的时候 递归结束
     * @param array
     *
     */
    public static int[] mergeSort(int[] array, int start, int end) {
        //长度
        int len = end - start + 1;
        //如果长度为1 则直接返回
        if(start >= end){
            return new int[] {array[start]};
        }
        //分为两部分
        int middle = len / 2;
        //分别排序
        int[] array1 = mergeSort(array, start, end - middle);
        int[] array2 = mergeSort(array, end - middle + 1, end);
        //merge
        int i = 0;
        int j = 0;
        int[] newArray = new int[len];
        for (int k = 0; k < len; k++) {
            //如果i或者j超过了数组的长度 则直接将另外一个数组的数据放到新数组中
            if(i >= array1.length){
                newArray[k] = array2[j];
                j++;
                continue;
            }
            if(j >= array2.length){
                newArray[k] = array1[i];
                i++;
                continue;
            }
            //比较两个数组的数据 将小的放到新数组中
            if (array1[i] > array2[j]) {
                newArray[k] = array2[j];
                j++;
            } else {
                newArray[k] = array1[i];
                i++;
            }
        }
        return newArray;
    }

    /**
     * 快速排序
     * 1.选择一个基准值
     * 2.将数组分为两部分 一部分比基准值小 一部分比基准值大
     * 3.递归的对两部分进行排序
     * @param array
     * @param start
     * @param end
     */
    public static void quickSort(int[] array, int start, int end){
        if((end - start) <= 0){
            return;
        }
        int qIndex = end;
        int q = array[qIndex];
        int temp;
        for(int i = start; i < end; i++){
            if((array[i] > q && i < qIndex) || (array[i] < q && i > qIndex)){
                temp = array[i];
                array[qIndex] = temp;
                array[i] = q;
                qIndex = i;
            }
        }
        quickSort(array, start, qIndex - 1);
        quickSort(array, qIndex + 1, end);
    }


    public static void bucketSort(int[] array){


    }


    /**
     * 计数排序
     * @param array
     */
    public static void countingSort(int[] array){
        final int len = array.length;
        int min = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            int v = array[i];
            if(v > max){
                max = v;
            }
            if(v < min){
                min = v;
            }
        }
        //申请一个计数器数组
        int[] countArray = new int[max - min + 1];
        for (int v : array) {
            countArray[v - min]++;
        }

        // 依次累加
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }
        int[] sortArray = new int[len];
        for (int i = len - 1; i >= 0; i--) {
            int v = array[i];
            int index = --countArray[v - min];
            sortArray[index] = v;
        }

        System.arraycopy(sortArray, 0, array, 0, len);
    }



    public static void main(String[] args) {
//        int[] array = new int[]{10,8,3,2,9};
//        selectSort(array);
//        System.out.println(Arrays.toString(array));

//        int[] array = new int[]{8,2,6,1};
//        int[] ints = mergeSort(array, 0, array.length - 1);
//        System.out.println(Arrays.toString(ints));

        int[] array = new int[]{6,11,3,9,-1};
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));


        countingSort(array);
        System.out.println("t[0]=" + Arrays.toString(array));

    }


}
