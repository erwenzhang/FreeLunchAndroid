package com.application.Kevin_Wenwen.FreeLunch;


import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.GridView;

import android.util.Log;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> imageURLs;
    private ArrayList<String> stream_list;
    private static LayoutInflater inflater=null;

    public ImageAdapter(Context c, ArrayList<String> imageURLs, ArrayList<String> stream_list) {
        mContext = c;
        this.imageURLs = imageURLs;
        this.stream_list = stream_list;
        inflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return imageURLs.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        ImageView imageView;
        TextView stream_name;


        if (convertView == null) {  // if it's not recycled, initialize some attributes
            grid = inflater.inflate(R.layout.grid_item,null);
         //   imageView = new ImageView(mContext);
           // imageView.setLayoutParams(new GridView.LayoutParams(170, 160));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            grid = convertView;
        }
        Log.d("wenwen", "imageadapter");
        //Log.isLoggable("wenwen", position);
      //  System.out.println(position);
       // System.out.println(imageURLs.get(position));
       // URL url = new URL(imageURLs.get(position));
        //String tmp_url = ;

       //  URL url = new URL(imageURLs.get(position));

        stream_name = (TextView)grid.findViewById(R.id.stream_name);
        stream_name.setText(stream_list.get(position));

        imageView = (ImageView)grid.findViewById(R.id.image);
        imageView.setLayoutParams(new GridView.LayoutParams(170, 160));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        new DownloadImageTask(imageView).execute(imageURLs.get(position));
     //   Picasso.with(mContext).load(imageURLs.get(position)).placeholder(R.drawable.placeholder_square).into(imageView);

      //  Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        //imageView.startAnimation(hyperspaceJumpAnimation);
        return grid;

    }



   private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


   /* private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

       // private ProgressDialog mDialog;
        private ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "image download error");
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //set image of your imageview
            bmImage.setImageBitmap(result);
            //close
           // mDialog.dismiss();
        }
    }*/

}