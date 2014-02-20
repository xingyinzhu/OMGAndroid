package com.xyzhu.omgandroid.omgandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xingyinzhu on 14-2-19.
 */
public class JSONAdapter extends BaseAdapter {
    private static final String IMAGE_URL_BASE ="http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater){
        mContext = context;
        mInflater = inflater;
        mJsonArray =new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return mJsonArray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if(view ==null){
            // Inflate the custom row layout from your XML.
            view = mInflater.inflate(R.layout.row_book, null);

            // create a new "Holder" with subviews
            holder =new ViewHolder();
            holder.thumbnailImageView=(ImageView) view.findViewById(R.id.img_thumbnail);
            holder.titleTextView=(TextView) view.findViewById(R.id.text_title);
            holder.authorTextView=(TextView) view.findViewById(R.id.text_author);

            // hang onto this holder for future recyclage
            view.setTag(holder);

        }else{
            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder =(ViewHolder) view.getTag();


        }
        // Get the current book's data in JSON form
        JSONObject jsonObject =(JSONObject) getItem(i);

        // See if there is a cover ID in the Object
        if(jsonObject.has("cover_i")){
            // If so, grab the Cover ID out from the object
            String imageID = jsonObject.optString("cover_i");

            // Construct the image URL (specific to API)
            String imageURL = IMAGE_URL_BASE + imageID +"-S.jpg";

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        }else{
            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }

        // Grab the title and author from the JSON
        String bookTitle ="";
        String authorName ="";

        if(jsonObject.has("title")){
            bookTitle = jsonObject.optString("title");
        }

        if(jsonObject.has("author_name")){
            authorName = jsonObject.optJSONArray("author_name").optString(0);
        }

        // Send these Strings to the TextViews for display
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);

        return view;
    }

    public void updateData(JSONArray jsonArray){
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }


    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}
