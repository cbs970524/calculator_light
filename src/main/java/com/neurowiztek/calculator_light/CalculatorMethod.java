package com.neurowiztek.calculator_light;


import java.util.LinkedList;

public class CalculatorMethod {
    public int calMultiDivide(LinkedList<Float> num, LinkedList<String> cal, LinkedList<Float> numAfter, LinkedList<String> calAfter) {
        int count = cal.size();     //배열 사이즈가 계속 줄어들기 때문에 변수로 선언 후 for문 사용.
        if (num.size() != cal.size() + 1) return -1;
        for (int i = 0; i < count; i++) {
            if (cal.get(0) == "*") {
                num.addFirst(num.poll() * num.poll());
                cal.poll();
            } else if (cal.get(0) == "/") {
                if (num.get(1) == 0) {      //0으로 나누려 할 때.
                    return 0;
                } else {
                    num.addFirst(num.poll() / num.poll());
                    cal.poll();
                }
            } else {      //곱셈 나눗셈 아닌 덧셈 뺄셈일 때.
                numAfter.add(num.poll());
                calAfter.add(cal.poll());
            }
        }
        while (num.size() > 0) {
            numAfter.add(num.poll());
        }
        return 1;
    }

    public float calPlusMinus(LinkedList<Float> num, LinkedList<String> cal){
        float result = num.poll();
        int count = cal.size();
        for (int i = 0; i < count; i++) {
            if (cal.get(0) == "+") {
                result += num.poll();
                cal.poll();
            } else {
                result -= num.poll();
                cal.poll();
            }
        }
        return result;
    }

    public float returnListSumNum(LinkedList<Float> list){
        float result = (float) 0;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(list.size() - 1 - i) * Math.pow(10, i);
        }
        return result;
    }
}
