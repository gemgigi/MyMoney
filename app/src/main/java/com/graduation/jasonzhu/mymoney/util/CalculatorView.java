package com.graduation.jasonzhu.mymoney.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gemha on 2016/3/7.
 */
public class CalculatorView implements View.OnClickListener {

    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnPoint, btnDelete;
    private TextView resultTv;
    private String lastInputNumber = "";
    private int lastInputState = INPUT_INTEGER;
    private int lastDecimalState = INPUT_DECIMAL1;
    private static final int INPUT_INTEGER = 1;
    private static final int INPUT_POINT = 0;
    private static final int INPUT_DECIMAL1 = -1;
    private static final int INPUT_DECIMAL2 = -2;
    private static final int INPUT_DECIMAL3 = -3;
    private static final int END = -4;
    private static final String UPPERLIMIT = "9999999.99";
    private Map<View, String> map;




    public View getCalculatoView(Context context) {
        View calculatorView = LayoutInflater.from(context).inflate(R.layout.calculator_layout, null);
        btn0 = (Button) calculatorView.findViewById(R.id.zero);
        btn1 = (Button) calculatorView.findViewById(R.id.one);
        btn2 = (Button) calculatorView.findViewById(R.id.two);
        btn3 = (Button) calculatorView.findViewById(R.id.three);
        btn4 = (Button) calculatorView.findViewById(R.id.four);
        btn5 = (Button) calculatorView.findViewById(R.id.five);
        btn6 = (Button) calculatorView.findViewById(R.id.six);
        btn7 = (Button) calculatorView.findViewById(R.id.seven);
        btn8 = (Button) calculatorView.findViewById(R.id.eight);
        btn9 = (Button) calculatorView.findViewById(R.id.nine);
        btnPoint = (Button) calculatorView.findViewById(R.id.point);
        btnDelete = (Button) calculatorView.findViewById(R.id.calculator_delete);
        resultTv = (TextView) calculatorView.findViewById(R.id.result);
        setListener();
        initData();
        return calculatorView;
    }

    private void initData() {
        map = new HashMap<>();
        map.put(btn0, btn0.getText().toString());
        map.put(btn1, btn1.getText().toString());
        map.put(btn2, btn2.getText().toString());
        map.put(btn3, btn3.getText().toString());
        map.put(btn4, btn4.getText().toString());
        map.put(btn5, btn5.getText().toString());
        map.put(btn6, btn6.getText().toString());
        map.put(btn7, btn7.getText().toString());
        map.put(btn8, btn8.getText().toString());
        map.put(btn9, btn9.getText().toString());
        map.put(btnPoint, btnPoint.getText().toString());
        map.put(btnDelete, btnDelete.getText().toString());

    }

    private void setListener() {
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnPoint.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
            case R.id.zero:
                inputNumber(v);
                break;
            case R.id.point:
                inputPoint(v);
                break;
            case R.id.calculator_delete:
                clearScreen();
                break;
        }
    }

    private void clearScreen() {
        resultTv.setText("0.00");
        lastDecimalState = INPUT_DECIMAL1;
        lastInputNumber = "";
        lastInputState = INPUT_INTEGER;
    }

    private void inputPoint(View view) {
        if (lastInputState == INPUT_POINT) {
            return;
        }
        lastInputNumber += map.get(view);
        lastInputState = INPUT_POINT;
    }

    private void inputNumber(View view) {
        String key = map.get(view);
        if (lastInputState == INPUT_POINT) {
            if (lastDecimalState == INPUT_DECIMAL1) {
//                lastInputNumber += key;
//                resultTv.setText(lastInputNumber + "0");
                setResultTv(key, "0");
                lastDecimalState = INPUT_DECIMAL2;
            } else if (lastDecimalState == INPUT_DECIMAL2) {
//                lastInputNumber += key;
//                resultTv.setText(lastInputNumber);
                setResultTv(key, "");
                lastDecimalState = INPUT_DECIMAL3;
            }
        } else if (lastInputState == INPUT_INTEGER) {
            if (key.equals("0")) {
                if (!resultTv.getText().equals("0.00")) {
                    setResultTv(key, ".00");
                }
            } else {
                setResultTv(key, ".00");
            }
        }
    }

    public void setResultTv(String key, String endNum) {
        if (!"".equals(endNum)) {
            lastInputNumber += key;
            if (Float.valueOf(lastInputNumber) > Float.valueOf(UPPERLIMIT)) {
                resultTv.setText(UPPERLIMIT);
            } else {
                resultTv.setText(lastInputNumber + endNum);
            }
        }else {
            lastInputNumber += key;
            resultTv.setText(lastInputNumber);
        }
    }
    public void setResultTv(String text){
        resultTv.setText(text);
    }

    public TextView getResultTv() {
        return resultTv;
    }
}