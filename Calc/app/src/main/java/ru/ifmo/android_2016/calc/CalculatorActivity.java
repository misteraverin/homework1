package ru.ifmo.android_2016.calc;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.text.TextUtils;


public class CalculatorActivity extends AppCompatActivity {

    private final int DIGIT = 0;
    private final int OPERATION = 1;

    private final int EMPTY = 0;
    private final int ADD = 1;
    private final int SUB = 2;
    private final int MUL = 3;
    private final int DIV = 4;


    private String currentText = "0";
    int currentState = EMPTY;
    int currentOperation = DIGIT;
    private boolean hasPoint = false;
    TextView expression;
    double firstNumber = 0, secondNumber = 0;

    static final String CURRENT_STRING = "currentString";
    static final String FIRST_NUMBER = "firstNumber";
    static final String SECOND_NUMBER = "secondNumber";
    static final String STATE = "currentState";
    static final String CURRENT_OPERATION = "currentOperation";
    static final String HAS_POINT = "hasNumberPoint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        expression = (TextView) findViewById(R.id.result);
        if (savedInstanceState != null) {
            firstNumber = savedInstanceState.getDouble(FIRST_NUMBER);
            secondNumber = savedInstanceState.getDouble(SECOND_NUMBER);
            currentText = new String(savedInstanceState.getString(CURRENT_STRING));
            currentOperation = savedInstanceState.getInt(CURRENT_OPERATION);
            currentState = savedInstanceState.getInt(STATE);
            hasPoint = savedInstanceState.getBoolean(HAS_POINT);
            expression.setText(currentText);
        }
        expression.setText(currentText);
    }

    public void onClickDigit(View view) {
        Button button = (Button) view;
        currentState = DIGIT;
        if (TextUtils.equals("0", currentText) && button.getId() != R.id.d0) {
            currentText = button.getText().toString();
        } else if (!TextUtils.equals("0", currentText)) {
            currentText = currentText + button.getText().toString();
        }
        expression.setText(currentText);
        if (currentOperation != EMPTY) {
            secondNumber = Double.parseDouble(currentText);
        } else {
            firstNumber = Double.parseDouble(currentText);
        }
    }

    public void onClickClear(View view) {
        currentText = "0";
        hasPoint = false;
        expression.setText(currentText);
        resetState();
    }

    public void OnClickPoint(View view) {
        if (currentState != OPERATION && hasPoint == false) {
            currentText = currentText + ".";
            hasPoint = true;
            expression.setText(currentText);
            if (currentOperation != EMPTY) {
                secondNumber = Double.parseDouble(currentText);
            } else {
                firstNumber = Double.parseDouble(currentText);
            }
        }
    }

    public void OnClickDelete(View view) {
        if (currentState == OPERATION) {
            expression.setText("0");
            currentText = "";
            hasPoint = false;
            resetState();
            return;
        }

        if (currentText.length() > 0) {
            if (currentText.charAt(currentText.length() - 1) == '.')
                hasPoint = false;
            currentText = currentText.substring(0, currentText.length() - 1);
        }
        expression.setText(currentText);


        if (currentOperation != EMPTY) {
            if (currentText.length() == 0) {
                secondNumber = 0;
            } else
                secondNumber = Double.parseDouble(currentText);
        } else {
            if (currentText.length() == 0) {
                firstNumber = 0;
            } else
                firstNumber = Double.parseDouble(currentText);
        }


    }

    public void OnClickPlusMinus(View view) {
        if (currentText.charAt(0) == '-')
            currentText = currentText.substring(1);
        else if (currentText.length() > 0 && currentText.charAt(0) != '0')
            currentText = "-" + currentText;
        else if (currentText.length() > 1 && currentText.charAt(0) == '0')
            currentText = "-" + currentText;
        expression.setText(currentText);
        if (currentOperation != EMPTY) {
            secondNumber = Double.parseDouble(currentText);
        } else {
            firstNumber = Double.parseDouble(currentText);
        }
    }

    public int chooseOperation(String str) {
        switch (str) {
            case "+":
                return ADD;
            case "-":
                return SUB;
            case "*":
                return MUL;
            case "/":
                return DIV;
        }
        return EMPTY;
    }

    public void OnClickOperation(View view) {
        Button button = (Button) view;
        currentOperation = chooseOperation(button.getText().toString());
        currentState = OPERATION;
        currentText = "";
        hasPoint = false;
        expression.setText(currentText);
    }

    public void OnClickResult(View view) {
        double result = 0;
        switch (currentOperation) {
            case ADD:
                result = firstNumber + secondNumber;
                break;
            case SUB:
                result = firstNumber - secondNumber;
                break;
            case MUL:
                result = firstNumber * secondNumber;
                break;
            case DIV:
                if (Math.abs(secondNumber) < 1e-9) {
                    expression.setText("ERROR");
                    currentText = "";
                    resetState();
                    firstNumber = result;
                } else {
                    result = firstNumber / secondNumber;
                }
                break;
        }
        if (!expression.getText().equals("ERROR")) {
            currentText = String.valueOf(result);
            expression.setText(currentText);
            resetState();
            firstNumber = result;
        }
    }

    public void resetState() {
        currentOperation = EMPTY;
        currentState = OPERATION;
        firstNumber = 0;
        hasPoint = false;
        secondNumber = 0;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString(CURRENT_STRING, currentText);
        saveInstanceState.putDouble(FIRST_NUMBER, firstNumber);
        saveInstanceState.putBoolean(HAS_POINT, hasPoint);
        saveInstanceState.putDouble(SECOND_NUMBER, secondNumber);
        saveInstanceState.putInt(STATE, currentState);
        saveInstanceState.putInt(CURRENT_OPERATION, currentOperation);
    }


}
