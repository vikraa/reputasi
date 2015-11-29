package com.reputasi.callblocker.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.adapter.SearchResultAdapter;
import com.reputasi.callblocker.view.custom.ReputasiEditText;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.database.record.SearchResultItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikraa on 7/31/2015.
 */
public class SearchResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        if (getIntent().hasExtra("searchItemList")) {
            Bundle bundle =  getIntent().getBundleExtra("searchItemList");
            List<SearchResultItem> itemList = bundle.getParcelableArrayList("searchItem");
            ReputasiTextView tvSearchedItem = (ReputasiTextView)findViewById(R.id.edt_search_item);
            tvSearchedItem.setText(itemList.get(0).getPhoneNumber());
            ListView listView = (ListView)findViewById(R.id.lv_search_item_list);
            SearchResultAdapter adapter = new SearchResultAdapter(itemList);
            listView.setAdapter(adapter);
        }
    }


}
