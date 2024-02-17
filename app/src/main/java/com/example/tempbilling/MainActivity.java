package com.example.tempbilling;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RecyclerViewAdapter.OnItemListener {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Item> itemList;
    Button btnAdd, save;
    MaterialButtonToggleGroup btnRadio;
    Spinner itemName;
    TextView weightTotal, amountTotal, subtotalAmount, cartageAmount;
    TextInputEditText weight, quantity, name, phone, cartage, basic, rate, total;
    String itemNameValue;
    ConstraintLayout addForm;
    ConstraintLayout main_layout;
    FormatHelper format;
    DataClass data;

    float rateValue;
    float weightValue;
    int quantityValue;
    float totalValue;
    float amountTotalValue;
    float weightTotalValue;
    float cartageValue, basicValue;
    boolean isItemNameEmpty, isRateEmpty, isWeightEmpty, isQuantityEmpty, isBasicEmpty, isCartageEmpty, isNameEmpty, isPhoneEmpty, isSelectedBundle;

    final int pageWidth = 298, pageHeight = 420;
    final int margin = 15;
    PdfDocument mPdfDocument;
    PdfDocument.PageInfo mPageInfo;
    PdfDocument.Page mPage;
    Canvas canvas;

    Date dateObj;
    DateFormat dateFormat;
    String date;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();

        data = new DataClass();
        format = new FormatHelper();

        dateObj = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(dateObj);

        main_layout = findViewById(R.id.main_layout);
        recyclerView = findViewById(R.id.recycler_view);
        btnAdd = findViewById(R.id.add);
        save = findViewById(R.id.save);
        btnRadio = findViewById(R.id.radioButton);
        itemName = findViewById(R.id.itemName);
        rate = findViewById(R.id.rate);
        weight = findViewById(R.id.weight);
        quantity = findViewById(R.id.quantity);
        total = findViewById(R.id.total);
        weightTotal = findViewById(R.id.weightTotal);
        amountTotal = findViewById(R.id.amountTotal);
        subtotalAmount = findViewById(R.id.subtotalAmount);
        cartageAmount = findViewById(R.id.cartageAmount);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        cartage = findViewById(R.id.cartage);
        basic = findViewById(R.id.basic);
        addForm = findViewById(R.id.addForm);

        weightTotalValue = 0f;
        amountTotalValue = 0f;

        isItemNameEmpty = true;
        isRateEmpty = true;
        isWeightEmpty = true;
        isQuantityEmpty = true;
        isBasicEmpty = true;
        isCartageEmpty = true;
        isNameEmpty = true;
        isPhoneEmpty = true;
        isSelectedBundle = true;
        btnAdd.setEnabled(false);
        btnAdd.setAlpha(.5f);
        save.setEnabled(false);
        save.setAlpha(.5f);
        rate.setEnabled(false);
        total.setEnabled(false);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.item_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemName.setAdapter(spinnerAdapter);
        itemName.setOnItemSelectedListener(this);

        basic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                String sbasicValue = basic.getText().toString();
                if(sbasicValue.length()!=0) {
                    isBasicEmpty = false;
                    basicValue = Float.parseFloat(basic.getText().toString());

                    if(!isBasicEmpty&&!isNameEmpty&&!isPhoneEmpty&&!isCartageEmpty&&itemList.size()!=0) {
                        save.setEnabled(true);
                        save.setAlpha(1f);
                    }
                } else {
                    isBasicEmpty = true;
                    basicValue = 0f;
                    addForm.setVisibility(ConstraintLayout.INVISIBLE);

                    save.setEnabled(false);
                    save.setAlpha(.5f);
                }

                if(!isBasicEmpty) {
                    addForm.setVisibility(ConstraintLayout.VISIBLE);
                }
            }
        });

        cartage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                String sCartageValue = cartage.getText().toString();
                if(sCartageValue.length()!=0) {
                    isCartageEmpty = false;
                    cartageValue = Float.parseFloat(cartage.getText().toString());
                    subtotalAmount.setText(format.money(amountTotalValue));
                    cartageAmount.setText(format.money(cartageValue));
                    amountTotal.setText(format.total(amountTotalValue+cartageValue));

                    if(!isBasicEmpty&&!isNameEmpty&&!isPhoneEmpty&&!isCartageEmpty&&itemList.size()!=0) {
                        save.setEnabled(true);
                        save.setAlpha(1f);
                    }
                } else {
                    isCartageEmpty = true;
                    cartageValue = 0f;
                    subtotalAmount.setText(format.money(amountTotalValue));
                    cartageAmount.setText("-");
                    amountTotal.setText(format.total(amountTotalValue));

                    save.setEnabled(false);
                    save.setAlpha(.5f);
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                String sName = name.getText().toString();
                if(sName.length()!=0) {
                    isNameEmpty = false;

                    if(!isBasicEmpty&&!isNameEmpty&&!isPhoneEmpty&&!isCartageEmpty&&itemList.size()!=0) {
                        save.setEnabled(true);
                        save.setAlpha(1f);
                    }
                } else {
                    isNameEmpty = true;

                    save.setEnabled(false);
                    save.setAlpha(.5f);
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                String sPhone = phone.getText().toString();
                if(sPhone.length()!=0) {
                    isPhoneEmpty = false;

                    if(!isBasicEmpty&&!isNameEmpty&&!isPhoneEmpty&&!isCartageEmpty&&itemList.size()!=0) {
                        save.setEnabled(true);
                        save.setAlpha(1f);
                    }
                } else {
                    isPhoneEmpty = true;

                    save.setEnabled(false);
                    save.setAlpha(.5f);
                }
            }
        });

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                String sweightValue = weight.getText().toString();
                if(sweightValue.length()==0) {
                    isWeightEmpty = true;
                    weightValue=0;
                    btnAdd.setEnabled(false);
                    btnAdd.setAlpha(.5f);
                    total.getText().clear();
                } else {
                    weightValue = Float.parseFloat(sweightValue);
                    isWeightEmpty = false;

                    if(!isItemNameEmpty && !isRateEmpty) {
                        totalValue = weightValue*rateValue;
                        total.setText(format.money(totalValue));
                    }

                    if(!isItemNameEmpty&&!isRateEmpty&&!isWeightEmpty&&!isQuantityEmpty) {
                        btnAdd.setEnabled(true);
                        btnAdd.setAlpha(1f);
                    }
                }
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                String squantityValue = quantity.getText().toString();
                if(squantityValue.length()==0) {
                    isQuantityEmpty = true;
                    quantityValue = 0;
                    btnAdd.setEnabled(false);
                    btnAdd.setAlpha(.5f);
                } else {
                    quantityValue = Integer.parseInt(squantityValue);
                    isQuantityEmpty = false;

                    if(!isItemNameEmpty&&!isRateEmpty&&!isWeightEmpty&&!isQuantityEmpty) {
                        btnAdd.setEnabled(true);
                        btnAdd.setAlpha(1f);
                    }
                }
            }
        });

        btnRadio.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked) {
                    if(checkedId==R.id.bundle) {
                        isSelectedBundle = true;
                    } else {
                        isSelectedBundle = false;
                    }
                }
            }
        });

        rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sRateValue = rate.getText().toString();
                if(sRateValue.length()==0) {
                    isRateEmpty = true;
                    rateValue=0;
                    btnAdd.setEnabled(false);
                    btnAdd.setAlpha(.5f);
                    total.getText().clear();
                } else {
                    rateValue = Float.parseFloat(sRateValue);
                    isRateEmpty = false;

                    if(!isItemNameEmpty && !isRateEmpty && !isWeightEmpty) {
                        totalValue = weightValue*rateValue;
                        total.setText(format.money(totalValue));
                    }

                    if(!isItemNameEmpty&&!isRateEmpty&&!isWeightEmpty&&!isQuantityEmpty) {
                        btnAdd.setEnabled(true);
                        btnAdd.setAlpha(1f);
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                Item item = new Item(itemNameValue, rateValue, weightValue, quantityValue, isSelectedBundle, totalValue);
                //add to recycler view arraylist
                itemList.add(item);
                //update total weight and amount
                weightTotalValue+=(weightValue);
                amountTotalValue+=(totalValue);

                //clear all the fields
                itemName.setSelection(0);    //spinnerAdapter.getPosition("Select a Planet") = 0?
                weight.getText().clear();
                quantity.getText().clear();
                rate.getText().clear();
                total.getText().clear();
                btnRadio.check(R.id.bundle);

                isItemNameEmpty = true;
                isWeightEmpty = true;
                isQuantityEmpty = true;
                isRateEmpty = true;
                rate.setEnabled(false);
                btnAdd.setEnabled(false);
                btnAdd.setAlpha(.5f);
                main_layout.requestFocus();

                //disable basic
                basic.setEnabled(false);

                //hide keyboard
                hideKeyboard(MainActivity.this);

                //update recyclerview
                recyclerViewAdapter.notifyDataSetChanged();

                weightTotal.setText(format.weight(weightTotalValue));
                if(!isCartageEmpty) {
                    subtotalAmount.setText(format.money(amountTotalValue));
                    cartageAmount.setText(format.money(cartageValue));
                    amountTotal.setText(format.total(amountTotalValue+cartageValue));
                } else {
                    subtotalAmount.setText(format.money(amountTotalValue));
                    cartageAmount.setText("-");
                    amountTotal.setText(format.total(amountTotalValue));
                }

                if(!isBasicEmpty&&!isNameEmpty&&!isPhoneEmpty&&!isCartageEmpty&&itemList.size()!=0) {
                    save.setEnabled(true);
                    save.setAlpha(1f);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                main_layout.requestFocus();
                save();
            }
        });

        // Recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(itemList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    //for item list spinner in add section
    @SuppressLint("ResourceAsColor")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemNameValue = parent.getItemAtPosition(position).toString();
        if(itemNameValue.equals("Select an item")) {
            //do nothing
            btnAdd.setEnabled(false);
            btnAdd.setAlpha(.5f);
            isItemNameEmpty = true;
            isRateEmpty = true;
            rate.setEnabled(false);
            rate.getText().clear();
            rateValue = 0f;
            totalValue = 0f;
            total.getText().clear();
            if(itemList.size()==0) {
                basic.setEnabled(true);
            }
        } else if(data.plusValueOf(itemNameValue)!=-1f){
            //calculate rate
            basic.setEnabled(false);
            rateValue = basicValue + data.plusValueOf(itemNameValue);
            rate.setText(format.rate(rateValue));
            isItemNameEmpty = false;
            isRateEmpty = false;
            rate.setEnabled(false);

            if(!isWeightEmpty && !isRateEmpty) {
                totalValue = weightValue*rateValue;
                total.setText(format.money(totalValue));
            }

            if(!isItemNameEmpty&&!isRateEmpty&&!isWeightEmpty&&!isQuantityEmpty) {
                btnAdd.setEnabled(true);
                btnAdd.setAlpha(1f);
            }
        } else {
            //set rate field enabled
            rate.getText().clear();
            rate.setEnabled(true);
            isItemNameEmpty = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        btnAdd.setEnabled(false);
        btnAdd.setAlpha(.5f);
        isItemNameEmpty = true;
        isRateEmpty = true;
        rate.setEnabled(false);
        rate.getText().clear();
        rateValue = 0f;
        totalValue = 0f;
        total.getText().clear();
        if(itemList.size()==0) {
            basic.setEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void generatePDF() {
        //defining different paints
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(1);

        Paint rightAlign = new Paint();
        rightAlign.setTextSize(9);
        rightAlign.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        rightAlign.setColor(Color.BLACK);
        rightAlign.setTextAlign(Paint.Align.RIGHT);

        Paint rightAlignSmall = new Paint();
        rightAlignSmall.setTextSize(7);
        rightAlignSmall.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        rightAlignSmall.setColor(Color.BLACK);
        rightAlignSmall.setTextAlign(Paint.Align.RIGHT);

        Paint leftAlign = new Paint();
        leftAlign.setTextSize(9);
        leftAlign.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        leftAlign.setColor(Color.BLACK);
        leftAlign.setTextAlign(Paint.Align.LEFT);

        Paint leftAlignSmall = new Paint();
        leftAlignSmall.setTextSize(8);
        leftAlignSmall.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        leftAlignSmall.setColor(Color.BLACK);
        leftAlignSmall.setTextAlign(Paint.Align.LEFT);

        Paint leftAlignBoldText = new Paint();
        leftAlignBoldText.setTextSize(9);
        leftAlignBoldText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        leftAlignBoldText.setColor(Color.BLACK);
        leftAlignBoldText.setTextAlign(Paint.Align.LEFT);

        Paint boldText = new Paint();
        boldText.setTextSize(9);
        boldText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        boldText.setColor(Color.BLACK);
        boldText.setTextAlign(Paint.Align.CENTER);

        Paint bigBoldText = new Paint();
        bigBoldText.setTextSize(12);
        bigBoldText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        bigBoldText.setColor(Color.BLACK);
        bigBoldText.setTextAlign(Paint.Align.CENTER);

        Paint datePaint = new Paint();
        datePaint.setTextSize(10);
        datePaint.setColor(Color.BLACK);
        datePaint.setTextAlign(Paint.Align.CENTER);

        //borders
        canvas.drawLine(margin, margin, pageWidth-margin, margin, linePaint);
        canvas.drawLine(margin, margin, margin, pageHeight-margin, linePaint);
        canvas.drawLine(pageWidth-margin, margin, pageWidth-margin, pageHeight-margin, linePaint);
        canvas.drawLine(margin, pageHeight-margin, pageWidth-margin, pageHeight-margin, linePaint);
        //other lines
        canvas.drawLine(margin, margin+45, pageWidth-margin, margin+45, linePaint);
        canvas.drawLine(margin+175, margin, margin+175, margin+45, linePaint);
        canvas.drawLine(margin+175, margin+15, pageWidth-margin, margin+15, linePaint);
        //label
        canvas.drawLine(margin, margin+65, pageWidth-margin, margin+65, linePaint);

        //bottom totals
        canvas.drawLine(margin, pageHeight-margin-3*20, pageWidth-margin, pageHeight-margin-3*20, linePaint);
        canvas.drawLine(margin, pageHeight-margin-2*20, pageWidth-margin, pageHeight-margin-2*20, linePaint);
        canvas.drawLine(margin+(pageWidth-2*margin)/2, pageHeight-margin-20, pageWidth-margin, pageHeight-margin-20, linePaint);
        canvas.drawLine(margin+(pageWidth-2*margin)/2, pageHeight-margin-3*20, margin+(pageWidth-2*margin)/2, pageHeight-margin, linePaint);

        canvas.drawText(date, margin+175+46, margin+33, datePaint);

        //labels
        canvas.drawText("ITEM", margin+85/2, margin+45+13, boldText);
        canvas.drawText("QTY.", margin+85+35/2, margin+45+13, boldText);
        canvas.drawText("WEIGHT", margin+120+60/2, margin+45+13, boldText);
        canvas.drawText("RATE", margin+175+33/2, margin+45+13, boldText);
        canvas.drawText("TOTAL", margin+175+33+30, margin+45+13, boldText);

        //vertical lines
        canvas.drawLine(margin+85, margin+45, margin+85, margin+65+17*(itemList.size()), linePaint);
        canvas.drawLine(margin+120, margin+45, margin+120, margin+65+17*(itemList.size()), linePaint);
        canvas.drawLine(margin+175, margin+45, margin+175, margin+65+17*(itemList.size()), linePaint);
        canvas.drawLine(margin+175+33, margin+45, margin+175+33, margin+65+17*(itemList.size()), linePaint);

        //variable values
        canvas.drawText(name.getText().toString(), margin+175/2,margin+20, bigBoldText);
        canvas.drawText(phone.getText().toString(), margin+175/2, margin+35, boldText);
        canvas.drawText("DATE", margin+175+46, margin+11+1, bigBoldText);

        //items list
        for(int i = 1; i <= itemList.size(); i++) {
            canvas.drawText(itemList.get(i-1).getItemName(), margin+1, margin+65+17*i-4, leftAlignBoldText);
            canvas.drawText(Integer.toString(Math.round(itemList.get(i-1).getQuantity())), margin+85+2, margin+65+17*i-4, leftAlignSmall);
            canvas.drawText(itemList.get(i-1).isBundleChecked()?"B":"Pc", margin+120-1, margin+65+17*i-4, rightAlignSmall);
            canvas.drawText(format.weight(itemList.get(i-1).getWeight()), margin+175-2, margin+65+17*i-4, rightAlign);
            canvas.drawText(format.rate(itemList.get(i-1).getRate()), margin+175+33-2, margin+65+17*i-4, rightAlign);
            canvas.drawText(format.money(itemList.get(i-1).getTotal()), pageWidth-margin-2, margin+65+17*i-4, rightAlign);

            //horizontal line
            canvas.drawLine(margin, margin+65+17*i, pageWidth-margin, margin+65+17*i, linePaint);
        }

        //bottom pane
        canvas.drawText("Total Weight", margin+2, pageHeight-margin-20*3+12, leftAlignBoldText);
        canvas.drawText("Kg", margin+(pageWidth-2*margin)/2-15, pageHeight-margin-20*3+12, leftAlign);
        canvas.drawText(format.weight(weightTotalValue), margin+(pageWidth-2*margin)/2-17, pageHeight-margin-20*3+12, rightAlign);

        canvas.drawText("Subtotal", margin+(pageWidth-2*margin)/2+2, pageHeight-margin-20*3+12, leftAlignBoldText);
        canvas.drawText("₹", pageWidth-margin-(pageWidth-2*margin)/4+7, pageHeight-margin-20*3+12, rightAlign);
        canvas.drawText(format.money(amountTotalValue), pageWidth-margin-(pageWidth-2*margin)/4+9, pageHeight-margin-20*3+12, leftAlign);

        canvas.drawText("Cartage", margin+(pageWidth-2*margin)/2+2, pageHeight-margin-20*2+12, leftAlignBoldText);
        canvas.drawText("₹", pageWidth-margin-(pageWidth-2*margin)/4+7, pageHeight-margin-20*2+12, rightAlign);
        canvas.drawText(format.money(cartageValue), pageWidth-margin-(pageWidth-2*margin)/4+9, pageHeight-margin-20*2+12, leftAlign);

        canvas.drawText("Total Amount", margin+(pageWidth-2*margin)/2+2, pageHeight-margin-20*1+12, leftAlignBoldText);
        canvas.drawText("₹", pageWidth-margin-(pageWidth-2*margin)/4+7, pageHeight-margin-20*1+12, rightAlign);
        canvas.drawText(format.total(amountTotalValue+cartageValue), pageWidth-margin-(pageWidth-2*margin)/4+9, pageHeight-margin-20*1+12, leftAlignBoldText);

        mPdfDocument.finishPage(mPage);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void viewPdfFile(File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

//        Uri apkURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Uri apkURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);
        intent.setDataAndType(apkURI, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void save() {
        mPdfDocument = new PdfDocument();
        mPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        mPage = mPdfDocument.startPage(mPageInfo);
        canvas = mPage.getCanvas();

        generatePDF();

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ParchiFolder/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String dateFileName = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ssaa", Locale.getDefault()).format(System.currentTimeMillis());
        String targetPdf = directory_path + dateFileName + "_" + (name.getText().toString().split(" "))[0] + "_Rs" + format.total(amountTotalValue+cartageValue) + ".pdf";

//        String targetPdf = directory_path + "parchi.pdf";

        File filePath = new File(targetPdf);
        if(filePath.exists()) {
            filePath.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            mPdfDocument.writeTo(fos);
            fos.close();
            viewPdfFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("main", "error "+ e.toString());
        }

        mPdfDocument.close();
    }

    //on listItem delete button click
    @Override
    public void onItemClick(int position) {
        //subtract that item
        weightTotalValue-=itemList.get(position).weight;
        amountTotalValue-=itemList.get(position).total;

        //remove that item
        itemList.remove(position);

        //update recycler view
        recyclerViewAdapter.notifyDataSetChanged();

        //refresh the total values, just in case
        if(itemList.size()==0) {
            weightTotalValue = 0f;
            amountTotalValue = 0f;
            save.setEnabled(false);
            save.setAlpha(.5f);
        }

        //update the total values
        weightTotal.setText(format.weight(weightTotalValue));
        if(!isCartageEmpty) {
            subtotalAmount.setText(format.money(amountTotalValue));
            cartageAmount.setText(format.money(cartageValue));
            amountTotal.setText(format.total(amountTotalValue+cartageValue));
        } else {
            subtotalAmount.setText(format.money(amountTotalValue));
            cartageAmount.setText("-");
            amountTotal.setText(format.total(amountTotalValue));
        }
    }
}