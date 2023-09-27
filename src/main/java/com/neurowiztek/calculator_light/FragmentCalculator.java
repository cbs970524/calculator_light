package com.neurowiztek.calculator_light;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.neurowiztek.calculator_light.databinding.FragmentCalculatorBinding;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalculator extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private FragmentCalculatorBinding binding;
    private MainActivity main;
    private LinkedList<Float> num = new LinkedList<Float>();        //입력한 숫자 저장.
    private LinkedList<Float> temporaryNum = new LinkedList<Float>();       //자릿수대로 10^n 곱하고 더해서 반환해줄 임시 숫자 저장용 리스트.
    private LinkedList<String> cal = new LinkedList<String>();        //입력한 연산기호 저장
    private LinkedList<Float> numAfter = new LinkedList<Float>();     //곱셈, 나눗셈 후 숫자 리스트.
    private LinkedList<String> calAfter = new LinkedList<String>();       //곱, 나눗셈 후 덧셈, 뺄셈만 있는 연산기호 리스트.
    private LinkedList<String> record = new LinkedList<String>();
    CalculatorMethod CM = new CalculatorMethod();
    private float result;
    boolean checkResult;      //연산 결과가 editText창에 있는지 확인 위한 숫자.
    String recordResult="";
    int RECORD_SIZE = 5;
    Pattern pattern = Pattern.compile("\\n");
    Matcher matcher = pattern.matcher(recordResult);
    String recordString="";



    public FragmentCalculator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCalculator.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalculator newInstance(String param1, String param2) {
        FragmentCalculator fragment = new FragmentCalculator();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalculatorBinding.inflate(inflater);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.buttonOne.setOnClickListener(view1 -> button("1"));
        binding.buttonTwo.setOnClickListener(view1 -> button("2"));
        binding.buttonThree.setOnClickListener(view1 -> button("3"));
        binding.buttonFour.setOnClickListener(view1 -> button("4"));
        binding.buttonFive.setOnClickListener(view1 -> button("5"));
        binding.buttonSix.setOnClickListener(view1 -> button("6"));
        binding.buttonSeven.setOnClickListener(view1 -> button("7"));
        binding.buttonEight.setOnClickListener(view1 -> button("8"));
        binding.buttonNine.setOnClickListener(view1 -> button("9"));
        binding.buttonZero.setOnClickListener(view1 -> button("0"));

        binding.buttonPlus.setOnClickListener(view1 -> button("+"));
        binding.buttonMinus.setOnClickListener(view1 -> button("-"));
        binding.buttonMulti.setOnClickListener(view1 -> button("*"));
        binding.buttonDivide.setOnClickListener(view1 -> button("/"));

        binding.buttonClear.setOnClickListener(view1 -> {
            binding.editText.setText("");
            num.clear();
            temporaryNum.clear();
            cal.clear();
            checkResult = false;
            //recordResult = recordResult.substring(0,matcher.end());
            recordResult="";
        });
        binding.buttonEqual.setOnClickListener(view1 -> {
            if (temporaryNum.size() == 0) {
                Toast.makeText(getActivity(), "연산 기호로 수식을 끝낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            num.add(CM.returnListSumNum(temporaryNum));
            recordResult += CM.returnListSumNum(temporaryNum);
            temporaryNum.clear();
            if (CM.calMultiDivide((LinkedList<Float>) num.clone(), (LinkedList<String>) cal.clone(), (LinkedList<Float>) numAfter.clone(), (LinkedList<String>) calAfter.clone()) == 0) {
                Toast.makeText(getActivity(), "0으로 나눌 수 없습니다.", Toast.LENGTH_SHORT).show();
                binding.buttonClear.callOnClick();
                return;
            }else {
                CM.calMultiDivide(num, cal, numAfter, calAfter);
            }
            result = CM.calPlusMinus(numAfter, calAfter);
            binding.editText.setText(String.valueOf(result));
            recordResult += "=" + result + "\n";

            //연산 결과 기록
            //String[] recordResultSplited = pattern.split(recordResult);
            record.add(recordResult);
            recordResult="";
            while (record.size() > RECORD_SIZE) {
                record.poll();
            }
            recordString = "";
            for (int i = 0; i < record.size(); i++) {
                recordString += record.get(i).toString();
            }

            binding.textView.setText(recordString);
            checkResult = true;
        });
    }
    public void onDestryView(){
        super.onDestroyView();
        binding = null;
    }

    public void button(String button){     //버튼 클릭 시 숫자 or 기호 저장 후 text창에 표시.
        if (checkResult) {      //결과 값이 있을 때
            try {       //숫자 버튼
                float i = (Float.parseFloat(button));
                binding.buttonClear.callOnClick();
                temporaryNum.add(i);
                binding.editText.setText(button);
            } catch (NumberFormatException e) {         //연산 기호 버튼
                num.add(result);
                cal.add(button);
                recordResult+=result+button;
                checkResult = false;
                binding.editText.setText(binding.editText.getText() + button);
            }
        } else {        //결과 값 없을 때
            try {       //숫자 버튼
                temporaryNum.add(Float.parseFloat(button));
                binding.editText.setText(binding.editText.getText() + button);
            } catch (NumberFormatException e) {         //연산 기호 버튼
                if (temporaryNum.size() == 0) {
                    Toast.makeText(getActivity(), "수식을 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                num.add(CM.returnListSumNum(temporaryNum));
                recordResult+=CM.returnListSumNum(temporaryNum)+button;
                temporaryNum.clear();
                cal.add(button);
                binding.editText.setText(binding.editText.getText() + button);
            }
        }
    }
}