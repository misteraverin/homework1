package ru.ifmo.android_2016.calc;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;


public class CalculatorActivity extends AppCompatActivity {

    private final int DIGIT = 0;
    private final int OPERATION = 1;

    private final int EMPTY = 0;
    private final int ADD = 1;
    private final int SUB = 2;
    private final int MUL = 3;
    private final int DIV = 4;


    private StringBuilder currentText = new StringBuilder("0");
    int currentState = EMPTY;
    int currentOperation = DIGIT;
    TextView expression;
    double firstNumber = 0, secondNumber = 0;

    static final String CURRENT_STRING = "currentString";
    static final String FIRST_NUMBER = "firstNumber";
    static final String SECOND_NUMBER = "secondNumber";
    static final String STATE = "currentState";
    static final String CURRENT_OPERATION = "currentOperation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        expression = (TextView) findViewById(R.id.result);
        if (savedInstanceState != null) {
            firstNumber = savedInstanceState.getDouble(FIRST_NUMBER);
            secondNumber = savedInstanceState.getDouble(SECOND_NUMBER);
            currentText = new StringBuilder(savedInstanceState.getString(CURRENT_STRING));
            currentOperation = savedInstanceState.getInt(CURRENT_OPERATION);
            currentState = savedInstanceState.getInt(STATE);
            expression.setText(currentText.toString());
        }
        expression.setText(currentText.toString());
    }

    public void onClickDigit(View view) {
        Button button = (Button) view;
        currentState = DIGIT;
        if (currentText.length() == 1 && button.getText().toString().charAt(0) == '0' && currentText.charAt(0) == '0') {
            // I don't know how to write effective if-statement, sorry ;)
        } else if (currentText.length() == 1 && button.getText().toString().charAt(0) != '0' && currentText.charAt(0) == '0') {
            currentText.setLength(0);
            currentText.append((button.getText().toString()));
            expression.setText(currentText.toString());
        } else {
            currentText.append(button.getText().toString());
            expression.setText(currentText.toString());
        }
        if (currentOperation != EMPTY) {
            secondNumber = Double.parseDouble(currentText.toString());
        } else {
            firstNumber = Double.parseDouble(currentText.toString());
        }
    }

    public void onClickClear(View view) {
        currentText.setLength(0);
        currentText.append('0');
        expression.setText(currentText.toString());
        resetState();
    }

    public void OnClickPoint(View view) {
        if (currentState != OPERATION) {
            currentText.append('.');
            expression.setText(currentText.toString());
            if (currentOperation != EMPTY) {
                secondNumber = Double.parseDouble(currentText.toString());
            } else {
                firstNumber = Double.parseDouble(currentText.toString());
            }
        }
    }

    public void OnClickDelete(View view) {
        if (currentState == OPERATION) {
            expression.setText("0");
            currentText.setLength(0);
            resetState();
            return;
        }

        if (currentText.length() > 0) {
            currentText.deleteCharAt(currentText.length() - 1);
        }
        expression.setText(currentText.toString());


        if (currentOperation != EMPTY) {
            if (currentText.length() == 0) {
                secondNumber = 0;
            } else
                secondNumber = Double.parseDouble(currentText.toString());
        } else {
            if (currentText.length() == 0) {
                firstNumber = 0;
            } else
                firstNumber = Double.parseDouble(currentText.toString());
        }


    }

    public void OnClickPlusMinus(View view) {
        if (currentText.charAt(0) == '-')
            currentText.deleteCharAt(0);
        else if (currentText.length() > 0 && currentText.charAt(0) != '0')
            currentText.insert(0, '-');
        else if (currentText.length() > 1 && currentText.charAt(0) == '0')
            currentText.insert(0, '-');
        expression.setText(currentText.toString());
        if (currentOperation != EMPTY) {
            secondNumber = Double.parseDouble(currentText.toString());
        } else {
            firstNumber = Double.parseDouble(currentText.toString());
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
        currentText.setLength(0);
        expression.setText(currentText.toString());
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
                    currentText.setLength(0);
                    resetState();
                    firstNumber = result;
                } else {
                    result = firstNumber / secondNumber;
                }
                break;
        }
        if (!expression.getText().equals("ERROR")) {
            currentText = new StringBuilder(String.valueOf(result));
            expression.setText(currentText);
            resetState();
            firstNumber = result;
        }
    }

    public void resetState() {
        currentOperation = EMPTY;
        currentState = OPERATION;
        firstNumber = 0;
        secondNumber = 0;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString(CURRENT_STRING, currentText.toString());
        saveInstanceState.putDouble(FIRST_NUMBER, firstNumber);
        saveInstanceState.putDouble(SECOND_NUMBER, secondNumber);
        saveInstanceState.putInt(STATE, currentState);
        saveInstanceState.putInt(CURRENT_OPERATION, currentOperation);
    }


}
