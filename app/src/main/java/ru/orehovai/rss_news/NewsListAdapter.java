package ru.orehovai.rss_news;


import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class NewsListAdapter extends BaseAdapter {

    //private static final String TAG = "MyLog";

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Rss> objects;
    boolean bookmarked;

    NewsListAdapter(Context context, ArrayList<Rss> news, boolean bookmarked) {
        ctx = context;
        objects = news;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bookmarked = bookmarked;//отметка о том какой это список(новости или закладки)
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Rss p = getGetRSS(position);//получаем элемент списка по позициии
        if (bookmarked) {//если список закладок
            if (!(p.isBookmarked())) {//и элемент не закладка
                return null;//тогда будет пустой
            }
        }
        // заполняем View в пункте списка данными из новостей или закладок: закголовок,цвет
        // и картинка
        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.getTitle());
        if (p.getImage() != null) { //если поле с адресом картинки не пустое
            ((ImageView) view.findViewById(R.id.ivImage)).setImageBitmap(p.getBitmap());//ставим картинку
        } else ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(R.drawable.prphoto);//если пустое заглушку
        if (p.isBookmarked()) {//если это закладка в общем списке
            view.setBackgroundColor(Color.YELLOW); //меняем цвет пункта на желтый
        } else view.setBackgroundColor(Color.WHITE); //еслинет на белый

        return view;
    }

    // новость по позиции
    Rss getGetRSS(int position) {
        return ((Rss) getItem(position));
    }




}