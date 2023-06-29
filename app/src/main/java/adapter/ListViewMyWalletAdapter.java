package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlychitieu.R;

public class ListViewMyWalletAdapter extends BaseAdapter {

    Context context;
    String itemList[];
    String itemText2[];
    int itemImage[];
    LayoutInflater inflater;

    public ListViewMyWalletAdapter(Context ctx, String[] itemList,String[] itemText2, int[] itemImage){
        this.context = context;
        this.itemList = itemList;
        this.itemText2 = itemText2;
        this.itemImage = itemImage;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return itemList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_mywallet, null);
        TextView itemText = (TextView) convertView.findViewById(R.id.item_text_mywallet);
        TextView itemTextn2 = (TextView) convertView.findViewById(R.id.item_text_mywallet_2);
        ImageView itemImg = (ImageView) convertView.findViewById(R.id.item_image_mywallet);
        itemText.setText(itemList[position]);
        itemTextn2.setText(itemText2[position]);
        itemImg.setImageResource(itemImage[position]);
        return convertView;
    }
}
