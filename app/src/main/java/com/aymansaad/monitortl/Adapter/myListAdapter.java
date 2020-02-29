package com.aymansaad.monitortl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.aymansaad.monitortl.R;
import com.aymansaad.monitortl.addEmp;
import com.aymansaad.monitortl.createPDF;
import com.aymansaad.monitortl.editaEmp;
import com.aymansaad.monitortl.report;
import com.aymansaad.monitortl.reportCard;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class myListAdapter extends ArrayAdapter<String> {
    private Context context;
    private Activity activity;
    private ArrayList<String> Names;
    private ArrayList<String> Emails;
    private ArrayList<String> passwords;
    private ArrayList<String>salarys;
    private ArrayList<String>  salaryTotal;
    private String[] Imgs;

    public myListAdapter(@NonNull Context mContext, Activity mActivity, ArrayList<String> mNames, ArrayList<String> mEmails, String[] mImgs ,ArrayList<String> passwords,ArrayList<String>salarys,ArrayList<String>salaryTotal) {
        super(mContext, R.layout.mylist,mNames);

        this.context = mContext;
        this.activity = mActivity;
        this.Names = mNames;
        this.Emails = mEmails;
        this.Imgs = mImgs;
        this.passwords=passwords;
        this.salarys=salarys;
        this.salaryTotal=salaryTotal;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist,null,true);

        TextView name = rowView.findViewById(R.id.Title);
        TextView email = rowView.findViewById(R.id.subTitle);
        AvatarView avatarView = rowView.findViewById(R.id.avatar);
        ImageButton btnEdit=rowView.findViewById(R.id.btnEdit);
        ImageView btnPdf=rowView.findViewById(R.id.pdf);
        //AvatarView avatar=rowView.findViewById(R.id.avatar);

        name.setText(Names.get(position));
        email.setText(Emails.get(position));

        IImageLoader iImageLoader = new PicassoLoader();
        iImageLoader.loadImage(avatarView,"https://avatars.dicebear.com/v2/initials/"+ Names.get(position) +".svg", Names.get(position));

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, Names.get(position),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, createPDF.class);
                i.putExtra("name", Names.get(position));
                i.putExtra("email", Emails.get(position));

                // i.putExtra("img",Imgs[position]);
                context.startActivity(i);
                //activity.finish();

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context, Names.get(position),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, editaEmp.class);
                i.putExtra("name", Names.get(position));
                i.putExtra("email", Emails.get(position));
                i.putExtra("salary",String.valueOf(salarys.get(position)) );
                i.putExtra("password", passwords.get(position));
               // i.putExtra("img",Imgs[position]);
                context.startActivity(i);
                //activity.finish();

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,String.valueOf(Emails.get(position)),Toast.LENGTH_SHORT).show();

                // Intent ii = new Intent(context, reportCard.class);
                //activity.finish();
                Intent ii = new Intent(context, report.class);
                ii.putExtra("name", Names.get(position));
                ii.putExtra("email",String.valueOf(Emails.get(position)) );
                ii.putExtra("salary",String.valueOf(salarys.get(position)) );
                ii.putExtra("salaryTotal",String.valueOf(salaryTotal.get(position)) );
                context.startActivity(ii);
                //activity.finish();
            }
        });
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,String.valueOf(Emails.get(position)),Toast.LENGTH_SHORT).show();

                // Intent ii = new Intent(context, reportCard.class);
                //activity.finish();
                Intent ii = new Intent(context, report.class);
                ii.putExtra("name", Names.get(position));
                ii.putExtra("email",String.valueOf(Emails.get(position)) );
                ii.putExtra("salary",String.valueOf(salarys.get(position)) );
                ii.putExtra("salaryTotal",String.valueOf(salaryTotal.get(position)) );
                context.startActivity(ii);
                //activity.finish();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,String.valueOf(Emails.get(position)),Toast.LENGTH_SHORT).show();

               // Intent ii = new Intent(context, reportCard.class);
                //activity.finish();
                Intent ii = new Intent(context, report.class);
                ii.putExtra("name", Names.get(position));
                ii.putExtra("email",String.valueOf(Emails.get(position)) );
                ii.putExtra("salary",String.valueOf(salarys.get(position)) );
                ii.putExtra("salaryTotal",String.valueOf(salaryTotal.get(position)) );
                context.startActivity(ii);
                //activity.finish();
            }
        });
        return rowView;
    }


}
