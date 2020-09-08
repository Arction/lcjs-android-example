package lightningchart.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class


MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Method to get the user input values for X & Y values and passing it to next activity
     *
     * @param view main activity view
     */
    public void createChart(View view) {
        Intent intent = new Intent(this, ChartDisplayActivity.class);
        EditText xValuesET = findViewById(R.id.xValues);
        EditText yValuesET = findViewById(R.id.yValues);
        boolean validationsPassed = checkValidations(xValuesET, yValuesET);
        if (!validationsPassed) {
            return;
        }
        String xValues = "[" + xValuesET.getText().toString() + "]";
        String yValues = "[" + yValuesET.getText().toString() + "]";
        String xyValues = xValues + "||" + yValues;
        Bundle bundle = new Bundle();
        bundle.putString("xyValues", xyValues);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Method to create a chart with random data
     *
     * @param view
     */
    public void createChartWithRandomData(View view) {
        Intent intent = new Intent(this, ChartDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("useRandom", true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Function to add validations for the Edit Text fields
     *
     * @param xValues EditText field for x-Values
     * @param yValues EditText field for y-Values
     * @return boolean value whether all the validations passed or not
     */
    private boolean checkValidations(EditText xValues, EditText yValues) {
        if (!emptyStringValidation(xValues, yValues)) {
            return false;
        }
        if (!lengthAndNumberValidation(xValues, yValues)) {
            return false;
        }
        return true;
    }

    /**
     * Function to check values are entered in the editText field
     *
     * @param xValues EditText field for x-Values
     * @param yValues EditText field for y-Values
     * @return boolean value whether the validation is passed or not
     */
    private boolean emptyStringValidation(EditText xValues, EditText yValues) {
        if (xValues.getText().toString().trim().equalsIgnoreCase("")) {
            String errorMessageStr = "X-Values can not be blank";
            showErrorMessage(errorMessageStr);
            return false;
        }
        if (yValues.getText().toString().trim().equalsIgnoreCase("")) {
            String errorMessageStr = "Y-Values can not be blank";
            showErrorMessage(errorMessageStr);
            return false;
        }
        return true;
    }

    /**
     * function to see whether the length of x-values and y-values are same and check if all the values are numbers
     *
     * @param xValues EditText field for x-Values
     * @param yValues EditText field for y-Values
     * @return Boolean value whether the validation passed or not
     */
    private boolean lengthAndNumberValidation(EditText xValues, EditText yValues) {
        String[] xValuesArr = xValues.getText().toString().split(",");
        String[] yValuesArr = yValues.getText().toString().split(",");
        if (xValuesArr.length == yValuesArr.length) {
            for (int i = 0; i < yValuesArr.length; i++) {
                try {
                    Float.parseFloat(xValuesArr[i]);
                    Float.parseFloat(yValuesArr[i]);
                } catch (Exception e) {
                    String errorMessageStr = "Please enter numbers only";
                    showErrorMessage(errorMessageStr);
                    return false;
                }
            }
        } else {
            String errorMessageStr = "x-values & y-values length is not the same";
            showErrorMessage(errorMessageStr);
            return false;
        }
        return true;
    }

    /**
     * Display the error message generated
     *
     * @param errorMessageStr String containing the error message to be displayed
     */
    private void showErrorMessage(String errorMessageStr) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setMessage(errorMessageStr);
        dlgAlert.setTitle("Error Message...");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}








