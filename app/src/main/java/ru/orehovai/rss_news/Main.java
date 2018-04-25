package ru.orehovai.rss_news;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main extends AppCompatActivity {

    //private static final String TAG = "MyLog";

    //public static Rss selectedRSSItem = null;
    String[] feedUrl = {"http://lifehacker.com/rss", "http://feeds.feedburner.com/TechCrunch/"};//массив адресов для RSS
    ListView newsListView = null;//вьюха для списка новостей
    ListView bookmarkedNewsListView = null;//вьюха для списка добавленных в закладки новостей
    ParseXMLTask pt;
    int iterAsync;//итератор для контроля заполнения вьюхи новостей

    private static ArrayList<Rss> news = new ArrayList<>();//список новостей
    private static ArrayList<Rss> bookmarkedNews = new ArrayList<>();//список добавленных в закладки новостей

    NewsListAdapter newsListAdapter = null;//для заполнения спика новостей
    NewsListAdapter bookmarkedNewsListAdapter = null;//для заполнения списка добавленных в закладки нвостей

    RssItems rssItems = new RssItems();

    public static ArrayList<Rss> getNews() {//для передачи news в rssdetails
        return news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //код для созздания закладок
        TabHost tabHost = findViewById(android.R.id.tabhost);

        tabHost.setup();;

        TabHost.TabSpec tabSpec;

        //вкладка и тег
        tabSpec = tabHost.newTabSpec("bookNews");
        //Название вкладки
        tabSpec.setIndicator("Bookmarked News");
        //связываем к компонентом по id
        tabSpec.setContent(R.id.bookmarkedNewsListView);
        //добавление в корень
        tabHost.addTab(tabSpec);

        //даьше то же со вторым списком
        //вкладка и тег
        tabSpec = tabHost.newTabSpec("news");
        //Название вкладки
        tabSpec.setIndicator("News");
        //связываем к компонентом по id
        tabSpec.setContent(R.id.newsListView);
        //добавление в корень
        tabHost.addTab(tabSpec);
        //вкладка по умолчанию
        tabHost.setCurrentTabByTag("news");


        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "news":
                        break;
                    case "bookNews"://при открытии вкладки с добавленными в закладку новостями
                        bookmarkedNews.clear();
                        for (Rss rss:news
                                ) {
                            if (rss.isBookmarked()) {
                                if (bookmarkedNews.contains(rss)) break;
                                bookmarkedNews.add(rss);//добавляем из общего массива нвостей если отмечено поле для закладки
                            }
                        }
                        bookmarkedNewsListAdapter.notifyDataSetChanged();//оповещение об обнослении
                        break;
                    default:
                        break;
                }
            }
        });



        // настраиваем списки
        newsListView = findViewById(R.id.newsListView);
        bookmarkedNewsListView = findViewById(R.id.bookmarkedNewsListView);

        newsListAdapter = new NewsListAdapter(this, news, false);// создаем адаптер для списка новостей и передаем туда массив новостей и флаг о том что это не закладки
        bookmarkedNewsListAdapter = new NewsListAdapter(this, bookmarkedNews, true);// создаем адаптер для списка закладок передае5м туда массив закладок и флаг о том что это закладки
        //назначаем адлаптеры вьюхам
        newsListView.setAdapter(newsListAdapter);
        bookmarkedNewsListView.setAdapter(bookmarkedNewsListAdapter);
        //обработчик наждатия на пункт списка новостей
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {
                Intent intent = new Intent(Main.this, RssDetails.class);
                intent.putExtra("mIndex", index);
                startActivity(intent);//открываем новую активность и передаем туда индекс нажатого элемента
            }
        });
        //обработчик нажатия на пункт списка закладок
        bookmarkedNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.get(index).getLink())));//открываем полную овость в браузере
            }
        });

        pt = new ParseXMLTask(rssItems, news);//инициализируем эекземпляр потока и передаем туда екзкмпляр rssItem для создания массива и массив новостей
        pt.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, feedUrl[iterAsync]);
        //используем метод executeOnExecuto для принудитеьного запуска в 2 поточном режиме и передаем в него элемент массива адресов выбранный с помощью итератора
    }
//для "тяжелой задачи" подключения и сбора данных
    private class ParseXMLTask extends AsyncTask<String, Void , ArrayList<Rss>> {

        RssItems rssItems;
        ArrayList<Rss> asNews;


        public ParseXMLTask(RssItems rssItems, ArrayList<Rss> asNews) {
            this.rssItems = rssItems;
            this.asNews = asNews;
        }

        @Override
        protected ArrayList<Rss> doInBackground(String...params) {
            if (!(asNews.isEmpty())) {//если список нвостей не пуст спим 10 секунд
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ArrayList<Rss> addNews = rssItems.getRSSItems(params[0]);//затем добавляем новости к списку
                ArrayList<Rss> fullNews = new ArrayList<>(asNews);
                fullNews.addAll(addNews);
                arraySort(fullNews);
                asNews = fullNews;
            } else asNews = rssItems.getRSSItems(params[0]);//если пуст просто добавляем в пустой список новости
            return asNews;
        }

        @Override
        protected void onPostExecute(ArrayList<Rss> result) {
            news.clear();
            news.addAll(result);
            arraySort(news);//формируем и сорируем список по дате, не учитывая разнсть источников
            newsListAdapter.notifyDataSetChanged();
            iterAsync++;//переходим к следующем адресу
            if (iterAsync < feedUrl.length) {
                new ParseXMLTask(rssItems, news).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, feedUrl[iterAsync]);
                //и выполняем его в отедльном потоке, после того как отработал прошлый адрес
            }
        }
    }

   public void arraySort(ArrayList<Rss> news)  {
        Collections.sort(news, new Comparator<Rss>() {
            public int compare(Rss o1, Rss o2) {
                return o2.getPubDate().compareTo(o1.getPubDate());//сортировка по убыванию по дате
            }
        });
    }

    @Override
    protected void onResume() {
        //при возвращении из другой активности обносляем данные списка
        super.onResume();
        newsListAdapter.notifyDataSetChanged();

    }
}
