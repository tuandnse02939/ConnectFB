package Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuandn.connectfb.R;

import java.io.InputStream;
import java.util.ArrayList;

import Model.Friend;

/**
 * Created by Anh Trung on 6/24/2015.
 */
public class FriendListAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private final ArrayList<Friend> mList;
    private LayoutInflater mInflater;

    public FriendListAdapter(Context context, ArrayList<Friend> mListFriend){
        super(context,R.layout.friend_list, mListFriend);
        this.context = context;
        this.mList = mListFriend;
    }

    public void showImage(String img_url, View v){
        new DownloadImageTask((ImageView) v.findViewById(R.id.imageID))
                .execute(img_url);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.from(context).inflate(com.tuandn.connectfb.R.layout.friend_list,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.friend_name);
            holder.userImage = (ImageView) convertView.findViewById(R.id.imageID);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        //Display Friends'avatar and name
        holder.name.setText(mList.get(position).getName());
        showImage(mList.get(position).getImage(),convertView);
        return convertView;
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

    static class ViewHolder {
        TextView name;
        ImageView userImage;
    }
}
