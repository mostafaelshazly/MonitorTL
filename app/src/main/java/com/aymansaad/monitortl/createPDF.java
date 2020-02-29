package com.aymansaad.monitortl;




import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class createPDF extends AppCompatActivity {

    //ArrayList<String> startTimeList = new ArrayList<String>(31);
    String [] startTimeList = new String [32];
    //ArrayList<String> endTimeList = new ArrayList<String>(31);
    String [] endTimeList = new String [32];
   // ArrayList<String> mamoriaList = new ArrayList<String>(31);
    String [] mamoriaList = new String [32];
    //ArrayList<String>  checkedList = new ArrayList<String>(31);
    String [] checkedList = new String [32];
    //ArrayList<String>  overTimeList = new ArrayList<String>(31);
    String [] overTimeList = new String [32];
    //ArrayList<String>  actionList = new ArrayList<String>(31);
    String [] actionList = new String [32];
   // ArrayList<String>  datList = new ArrayList<String>(31);
    String [] datList = new String [32];
   // ArrayList<String>  overTimeMinsList = new ArrayList<String>(31);
    String [] overTimeMinsList = new String [32];
    String salaryText=null ;
    String salaryTotalText ;
    Timestamp timeStart=null,timeEnd=null;
    Double sumTotalActionList=0.0;
    int countMamoriaList;
    private FirebaseFirestore db,dbList;
    float sumOverTimeMins= (float) 0.0;
    float  sumOverTime= (float) 0.0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //email = user.getEmail();
    //String email ="pop@gmail.com";
    private PdfPTable table;
    private EditText myEditText;
    private String path;
    private File dir;
    private File file;
    private PdfPCell cell;
    private Image bgImage;
    Button Button1,Button2;
    TextView nameText,emailText;
    BaseColor myColor = WebColors.getRGBColor("#e3e3e3");
    BaseColor myColor1 = WebColors.getRGBColor("#e3e3e3");
    String name,date;
    EditText month,year;
     String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad  = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_create_pdf);
        Intent intent = getIntent();
         email = intent.getStringExtra("email");
         name = intent.getStringExtra("name");
        nameText= findViewById(R.id.name);
        emailText= findViewById(R.id.email);

        nameText.setText(name);
        emailText.setText(email);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
         Button1 = findViewById(R.id.button1);
        Button2 = findViewById(R.id.button2);
        Button1.setVisibility(View.GONE);
        ActivityCompat.requestPermissions(createPDF.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);



        for (int i = 0; i < 32; i ++) {

            startTimeList[i]="false";
            endTimeList[i]="false";
            mamoriaList[i]="false";
            checkedList[i]="false";


            overTimeList[i]="false";
            actionList[i]="false";

            datList[i]="false";
            overTimeMinsList[i]="false";


        }

        sumTotalActionList=0.0;
        countMamoriaList=0;
        dbList = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //email = user.getEmail();
        //email ="bob@gmail.com";

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(month.getText()!=null&&year.getText()!=null){
                    date=String.valueOf(String.valueOf(month.getText())+"-"+String.valueOf(year.getText()));
                    //Toast.makeText(createPDF.this, " data!!!" +date, Toast.LENGTH_SHORT).show();

                    dbList.collection("users").document(email).collection(date).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent( QuerySnapshot documentSnapshots ,  FirebaseFirestoreException e) {
                        if(e!=null){
                            Toast.makeText(createPDF.this, "no data", Toast.LENGTH_SHORT).show();
                        }

                        for (DocumentSnapshot doc : documentSnapshots){
//                            totalActionList.add(Double.valueOf(doc.getString("action")));
//                            //Toast.makeText(reportJob.this, "dis >"+String.valueOf(doc.getString("action")), Toast.LENGTH_SHORT).show();
//                            totalMamoriaList.add(doc.getBoolean("mamoria"));
//                            //
                            if(doc!=null){
                                //Toast.makeText(createPDF.this, "overTimeMins  >"+doc.getString("overTimeMins"), Toast.LENGTH_SHORT).show();

                                timeStart = doc.getTimestamp("start_time");
                                timeEnd = doc.getTimestamp("end_time");

                                Calendar calStart = Calendar.getInstance(Locale.ENGLISH);
                                calStart.setTimeInMillis(timeStart.getSeconds() * 1000);
                                Date DateStart = calStart.getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm: a");

                                Calendar calEnd = Calendar.getInstance(Locale.ENGLISH);
                                calEnd.setTimeInMillis(timeEnd.getSeconds() * 1000);
                                Date DateEnd = calEnd.getTime();
                                SimpleDateFormat sdfEnd = new SimpleDateFormat("hh:mm: a");
                                //sdf.format(DateStart)+" -->> "+sdfEnd.format(DateEnd);

                                datList[Integer.valueOf(doc.getId())]=String.valueOf(doc.getId()) ;
                                startTimeList[Integer.valueOf(doc.getId())]=String.valueOf(sdf.format(DateStart)) ;
                                endTimeList[Integer.valueOf(doc.getId())]=String.valueOf(sdfEnd.format(DateEnd));
                                actionList[Integer.valueOf(doc.getId())]=doc.getString("action");
                                mamoriaList[Integer.valueOf(doc.getId())]=String.valueOf(doc.getBoolean("mamoria"));
                                checkedList[Integer.valueOf(doc.getId())]=doc.getString("checked");
                                overTimeList[Integer.valueOf(doc.getId())]=doc.getString("overTime");
                                overTimeMinsList[Integer.valueOf(doc.getId())]=doc.getString("overTimeMins");

                                sumTotalActionList=sumTotalActionList+Double.valueOf(doc.getString("action"));
                                sumOverTime=sumOverTime+Float.valueOf(doc.getString("overTime"));
                                sumOverTimeMins=sumOverTimeMins+Float.valueOf(doc.getString("overTimeMins"));

                                if(doc.getBoolean("mamoria")==true){
                                    countMamoriaList=countMamoriaList+1;

                                }
                            }


                        }


//

                    }
                });




                db = FirebaseFirestore.getInstance();
                final DocumentReference docRef = db.collection("users").document(email).collection(String.valueOf("monthlySalary")).document(date);
                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            Toast.makeText(createPDF.this, "Listen failed.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            salaryText =snapshot.getString("salary");
                            salaryTotalText =snapshot.getString("salaryTotal");

                            if(salaryText !=null&&salaryTotalText !=null){



                                //
                            }
                            else{
                                Toast.makeText(createPDF.this,   "salary data null", Toast.LENGTH_SHORT).show();

                            }










                        } else {
                            //Log.d(TAG, "Current data: null");
                            Toast.makeText(createPDF.this, "Current data: null", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }
                Button1.setVisibility(View.VISIBLE);
            }
        });
if(salaryText!=null){

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createMyPDF(View view)throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MonitoryTL/"+email;
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            Calendar calTime = Calendar.getInstance(Locale.ENGLISH);
            calTime.setTimeInMillis( new Timestamp(new Date()).getSeconds()* 1000);
            Date fileTime = calTime.getTime();
            SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            //sdf.format(DateStart)+" -->> "+sdfEnd.format(DateEnd);





            file = new File(dir, name+" "+ sdfFile.format(fileTime )+timeEnd.getSeconds()+ ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = createPDF.this.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("EGYPT AIR CONDITIONING"));


                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                table = new PdfPTable(8);

                                             float[] columnWidth = new float[]{20,36,39, 38, 38, 35, 38, 40};
                table.setWidths(columnWidth);
                //table.setLockedWidth(true);

                cell = new PdfPCell();


                cell.setBackgroundColor(myColor1);
                cell.setColspan(8);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(8);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(8);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("date"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("checked"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("start time"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("end time"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("discount"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("mamoria"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("over time"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("over time mins"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(8);



                for (int i = 1; i <= 31; i ++) {
                    if(startTimeList[i]!="false"){
                    //startTimeList[i];

                    table.addCell(datList[i]);
                    table.addCell(checkedList[i]);
                    table.addCell(startTimeList[i]);
                    table.addCell(endTimeList[i]);
                    table.addCell(actionList[i]);
                    table.addCell(mamoriaList[i]);
                    table.addCell(overTimeList[i]);
                    table.addCell(overTimeMinsList[i]);
                    }
                    // i is the index
                    // yourArrayList.get(i) is the element
                }
//                for (int i = 1; i <= 10; i++) {
//                    table.addCell(String.valueOf(i));
//                    table.addCell("Header 1 row " + i);
//                    table.addCell("Header 2 row " + i);
//                    table.addCell("Header 3 row " + i);
//                    table.addCell("Header 4 row " + i);
//                    table.addCell("Header 5 row " + i);
//                    table.addCell("Header 6 row " + i);
//
//                }
                //Toast.makeText(createPDF.this, "sumTotalActionList  >"+sumTotalActionList, Toast.LENGTH_SHORT).show();

                PdfPTable ftable = new PdfPTable(6);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{30, 5, 5, 30, 20, 10};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(8);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Name : "+name));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);date=String.valueOf(String.valueOf(month.getText())+"-"+String.valueOf(year.getText()));
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase("Date : "+date));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Salary :"+salaryText));
                cell.setColspan(8);
                ftable.addCell(cell);


                cell = new PdfPCell(new Paragraph("Total Salary : "+salaryTotalText));
                cell.setColspan(8);
                ftable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Total Discount :"+sumTotalActionList));
                cell.setColspan(8);
                ftable.addCell(cell);


                cell = new PdfPCell(new Paragraph("Total Mamoria : "+countMamoriaList));
                cell.setColspan(8);
                ftable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Total overTime : "+Integer.valueOf((int) sumOverTime)));
                cell.setColspan(8);
                ftable.addCell(cell);
                cell = new PdfPCell(new Paragraph("Total overTimeMins : "+sumOverTimeMins));
                cell.setColspan(8);
                ftable.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(8);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                Toast.makeText(getApplicationContext(), "created PDF", Toast.LENGTH_LONG).show();
                for (int i = 0; i < 32; i ++) {

                    startTimeList[i]="false";
                    endTimeList[i]="false";
                    mamoriaList[i]="false";
                    checkedList[i]="false";


                    overTimeList[i]="false";
                    actionList[i]="false";

                    datList[i]="false";
                    overTimeMinsList[i]="false";


                }
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
