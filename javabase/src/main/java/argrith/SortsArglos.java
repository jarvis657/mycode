package argrith;

import java.util.*;

/**
 * @Author:lmq
 * @Date: 2020/12/1
 * @Desc:
 **/
public class SortsArglos {
    // 冒泡排序，a表示数组，n表示数组大小
    public void bubbleSort(int[] a) {
        if (a.length <= 1) return;
        for (int i = 0; i < a.length; ++i) {//这个标记i是记录冒泡到末尾第几个了 注意 a.length -i - 1     从 数组 最右端 --》 最左端
            // 提前退出冒泡循环的标志位
            boolean flag = false;
            for (int j = 0; j < a.length - i - 1; ++j) {
                if (a[j] > a[j + 1]) { // 交换
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    flag = true;  // 表示有数据交换
                }
            }
            if (!flag) break;  // 没有数据交换，提前退出
        }
    }

    // 插入排序，a表示数组，n表示数组大小
    public void insertionSort(int[] a) {
        int n = a.length;
        if (n <= 1) return;
        for (int i = 1; i < n; ++i) { //从数组 最左端 --》 最右端
            int value = a[i];
            int j = i - 1;
            // 查找插入的位置
            for (; j >= 0; --j) { // 这个是已经排好序的 子序列 ，判断要插入到哪里
                if (a[j] > value) {
                    a[j + 1] = a[j];  // 数据移动
                } else {
                    break;
                }
            }
            a[j + 1] = value; // 插入数据
        }
    }

    public static void main(String[] args) {
        int[] arr = {4, 3, 1, 31, 5, 7, 2, 11, 9};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    // 快排
    public static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int index = partitionRec(arr, start, end);
            quickSort(arr, start, index - 1);
            quickSort(arr, index + 1, end);
        }
    }

    private static int partitionRec(int[] arr, int start, int end) {
        //arr[start]为挖的第一个坑
        int key = arr[start];
        while (start < end) {
            while (arr[end] >= key && end > start)
                end--;
            arr[start] = arr[end];
            while (arr[start] <= key && end > start)
                start++;
            arr[end] = arr[start];
        }
        arr[start] = key;
        return start;
    }

    // 快排非递归  start和end为前闭后闭
    private static void nonRec_quickSort(int[] a, int start, int end) {
        // 用栈模拟
        Stack<Integer> stack = new Stack<>();
        if (start < end) {
            stack.push(end);
            stack.push(start);
            while (!stack.isEmpty()) {
                int l = stack.pop();
                int r = stack.pop();
                int index = partition(a, l, r);
                if (l < index - 1) {
                    stack.push(index - 1);
                    stack.push(l);
                }
                if (r > index + 1) {
                    stack.push(r);
                    stack.push(index + 1);
                }
            }
        }
        System.out.println(Arrays.toString(a));
    }

    private static int partition(int[] a, int start, int end) {
        int pivot = a[start];
        while (start < end) {
            while (start < end && a[end] >= pivot)
                end--;
            a[start] = a[end];
            while (start < end && a[start] <= pivot)
                start++;
            a[end] = a[start];
        }
        a[start] = pivot;
        return start;
    }

    /**
     * 利用快排的partition 来获取 第k大的数  topN 问题
     *
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargest(int[] nums, int k) {
        // 转换成第k小
        k = nums.length - k;
        int start = 0, end = nums.length - 1;
        while (start < end) {
            int pivot = partition(nums, start, end);
            if (pivot == k) {
                break;
            } else if (pivot < k) {
                start = pivot + 1;
            } else {
                end = pivot - 1;
            }
        }
        return nums[k];
    }


    //    // 归并排序算法, A是数组，n表示数组大小
//merge_sort(A, n) {
//  merge_sort_c(A, 0, n-1)
//}
//
//// 递归调用函数
//merge_sort_c(A, p, r) {
//  // 递归终止条件
//  if p >= r  then return
//
//  // 取p到r之间的中间位置q
//  q = (p+r) / 2
//  // 分治递归
//  merge_sort_c(A, p, q)
//  merge_sort_c(A, q+1, r)
//  // 将A[p...q]和A[q+1...r]合并为A[p...r]
//  merge(A[p...r], A[p...q], A[q+1...r])
//}
    //ListNode* MergeTwoSortedList(ListNode *head1, ListNode *head2){
    //    if (head1 == NULL) {
    //        return head2;
    //    }
    //    if (head2 == NULL) {
    //        return head1;
    //    }
    //    ListNode* p1 = head1, p2 = head2;
    //    //分配一个新的节点
    //    ListNode* newHead = (ListNode*)(malloc(size(ListNode)));
    //    ListNode* tail = newHead;
    //    while(p1 != NULL && p2 != NULL){
    //        if (p1 -> val <= p2 -> val){
    //            tail -> next = p1;
    //            tail = p1;
    //            p1 = p1 -> next;
    //        }else {
    //            tail -> next = p2;
    //            tail = p2;
    //            p2 = p2 -> next;
    //        }
    //    }
    //    if (p1){
    //        tail -> next = p1;
    //    }
    //    if(p2){
    //        tail -> next = p2;
    //    }
    //    return newHead -> next;
    //}
    public static int mypartitions(int[] arrs, int start, int end) {
        int base = arrs[start];
        while (start < end) {
            while (base < arrs[end] && start < end) {
                end--;
            }
            arrs[start] = arrs[end];
            while (base > arrs[start] && start < end) {
                start++;
            }
            arrs[end] = arrs[start];
        }
        arrs[start] = base;
        return start;
    }


}
