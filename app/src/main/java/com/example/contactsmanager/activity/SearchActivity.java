package com.example.contactsmanager.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.contactsmanager.R;
import com.example.contactsmanager.frags.SearchFragment;

/**
 * 搜索的Activity
 */

public class SearchActivity extends AppCompatActivity {

    private SearchFragment mSearchFragment;

    /**
     * SearchActivity的入口方法
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActivityCollector.addActivity(this);
        initWidget();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        setTitle(R.string.action_search);
        initToolbar((Toolbar) findViewById(R.id.toolbar));
        mSearchFragment = new SearchFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mSearchFragment)
                .commit();
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        // 找到搜索菜单
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        Log.d("TAG", "searchView");
        if (searchView != null) {
            // 拿到一个搜索管理器
            Log.d("TAG", "searchView != null");
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            // 添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // 当点击提交按钮的时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // 当文字改变的时候，不会及时搜索，只在为null的情况下进行搜索
                    if (TextUtils.isEmpty(newText)) {
                        search("");
                        return true;
                    } else {
                        search(newText);
                        return true;
                    }
                }
            });
        }
        return true;
    }

    /**
     * 搜索的发起点
     *
     * @param query 要搜索的内容
     */
    private void search(String query) {
        if (mSearchFragment == null) {
            return;
        }
        mSearchFragment.search(query);
    }

    /**
     * 初始化ToolBar
     *
     * @param toolbar Toolbar
     */
    private void initToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // 返回键的监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initTitleNeedBack();
    }

    /**
     * 设置左上角的返回按钮为实际的返回效果
     */
    private void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
   //         Log.d("TAG", "actionbar != null");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    /**
     * 搜索Fragment必须实现的搜索接口
     */
    public interface SearchInterface {
        void search(String content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
