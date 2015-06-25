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

import com.facebook.internal.ImageRequest;
import com.google.android.imageloader.ImageLoader;
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
    private ImageLoader imageLoader;

    public FriendListAdapter(Context context, ArrayList<Friend> mListFriend){
        super(context,R.layout.friend_list, mListFriend);
        this.context = context;
        this.mList = mListFriend;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.from(context).inflate(com.tuandn.connectfb.R.layout.friend_list,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.friend_name);
            holder.userImage = (ImageView) convertView.findViewById(R.id.imageID);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        //Set Friend's name
        holder.name.setText(mList.get(position).getName());

        //Set Friend Image
        ImageLoader.Callback callback = new ImageLoader.Callback() {
            @Override
            public void onImageLoaded(ImageView imageView, String s) {
                holder.userImage = imageView;
            }

            @Override
            public void onImageError(ImageView imageView, String s, Throwable throwable) {

            }
        };

        imageLoader = new ImageLoader();
        imageLoader.bind(holder.userImage,mList.get(position).getImage(),callback);
        return convertView;
    }



    static class ViewHolder {
        TextView name;
        ImageView userImage;
    }
}
