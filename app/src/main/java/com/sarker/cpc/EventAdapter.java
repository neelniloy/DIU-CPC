package com.sarker.cpc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context nContext;
    private ArrayList<EventInfo> eList;
    private  long code;
    private static int DELAY_TIME= 1500;
    private ProgressDialog progressDialog1;
    private int lastPosition = 1000;
    private int expandedPosition = -1;
    private int mExpandedPosition= -1;
    private static String mdate;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;




    private static long currentDate() {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy hh:mm aa");
        String date = currentDate.format(calFordDate.getTime());
        return getDateInMillis(date);
    }

    public static String getTimeAgo(String date) {
        long time = Long.parseLong(date);
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate();
        if (time > now || time <= 0) {

            final long diff = time - now;
            if (diff < MINUTE_MILLIS) {
                return "Just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "1 minute to go";
            } else if (diff < 60 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes to go";
            } else if (diff < 2 * HOUR_MILLIS) {
                return "1 hour to go";
            } else{

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                String cDate = currentDate.format(calFordDate.getTime());

                if (diff / HOUR_MILLIS >12 && diff / HOUR_MILLIS < 24) {

                    return "Tomorrow";

                }else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + " hours to go";
                }
                else {
                    return mdate;
                }
            }
        }
        else {
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "Just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "1 minute ago";
            } else if (diff < 60 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 2 * HOUR_MILLIS) {
                return "1 hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "Yesterday";
            } else if (diff < 72 * HOUR_MILLIS) {
                return diff / DAY_MILLIS + " days ago";
            } else {
                return mdate;
            }
        }

    }



    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


    public EventAdapter(Context context, ArrayList<EventInfo> eLists) {
        nContext = context;
        eList = eLists;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(nContext).inflate(R.layout.event_item, parent, false);
        return new EventAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventAdapter.ViewHolder holder, final int position) {
        final EventInfo info = eList.get(position);

        final String key = info.getKey();


        holder.eTitle.setText(info.geteTitle());
        holder.wingType.setText(info.getWingType());
        holder.eType.setText("● "+info.geteType()+" Event");

        Picasso.get().load(info.geteImage()).fit().centerInside().placeholder(R.drawable.cpc).into(holder.eImage);


        holder.postTime.setText(getTimeAgo(info.getPostTime()));
        holder.eTime.setText(convertDate(info.geteTime(),"h:mm a  ┋  dd MMM yyyy"));


        holder.delete.setVisibility(View.GONE);

        holder.eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(info.eRegister));
                nContext.startActivity(intent);


            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog1 = new ProgressDialog(nContext);
                progressDialog1.show();
                progressDialog1.setMessage("Deleting...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog1.dismiss();


                        eList.remove(holder.getAdapterPosition());
                        holder.eventRef.child(key).removeValue();




                    }
                },DELAY_TIME);


            }
        });



        setAnimation(holder.itemView, position);



    }


    @Override
    public void onViewDetachedFromWindow(@NonNull EventAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eTitle,eTime,postTime,eType,wingType;
        public ImageView delete,eImage;
        public Button eRegister;
        public DatabaseReference eventRef;

        public ViewHolder(View itemView) {
            super(itemView);

            eTitle = itemView.findViewById(R.id.event_title);
            eTime = itemView.findViewById(R.id.event_time);
            postTime = itemView.findViewById(R.id.post_time);
            eType = itemView.findViewById(R.id.event_type);
            wingType = itemView.findViewById(R.id.wing_type);
            delete = itemView.findViewById(R.id.event_delete);
            eImage = itemView.findViewById(R.id.event_image);
            eRegister = itemView.findViewById(R.id.event_register);

            eventRef = FirebaseDatabase.getInstance().getReference().child("Events");

        }

    }





    private void setAnimation(View viewToAnimate, int position)
    {

        if (position < lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(nContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "dd-MMMM-yyyy hh:mm aa");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
