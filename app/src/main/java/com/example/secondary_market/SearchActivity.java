package com.example.secondary_market;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "hi";
    private EditText etInput;// 输入框
    private ImageView ivDelete;// 删除键
    private TextView btnBack;//返回按钮
    private ImageButton sbtn;//返回按钮
    private Context mContext;//上下文对象
    private ListView lvTips;//弹出列表
    private ArrayAdapter<String> mHintAdapter;//提示adapter （推荐adapter）
    private ArrayAdapter<String> mAutoCompleteAdapter;//自动补全adapter 只显示名字
    private SearchViewListener mListener;//搜索回调接口
    private TextView title;

    List<Commodity> searchCommodities = new ArrayList<>();

    CommodityDbHelper dbHelper;
    SearchAdapter adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        mContext=this;
        initViews();
        adapter = new SearchAdapter(getApplicationContext());
        dbHelper = new CommodityDbHelper(getApplicationContext(),CommodityDbHelper.DB_NAME,null,1);
    }

    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnBack = (TextView) findViewById(R.id.search_btn_back);
        lvTips = (ListView) findViewById(R.id.search_lv_tips);
        sbtn = (ImageButton) findViewById(R.id.search_btn);
        title = findViewById(R.id.tv_title);
        //设置删除图片的点击事件
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把EditText内容设置为空 ListView隐藏
                etInput.setText("");
                lvTips.setVisibility(View.GONE);
            }
        });
        //搜索按钮的点击事件
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把EditText内容设置为空 ListView隐藏
                String  s = etInput.getText().toString();
                if(s==title.getText().toString()){
                    adapter = new SearchAdapter(getApplicationContext());
                    dbHelper = new CommodityDbHelper(getApplicationContext(),CommodityDbHelper.DB_NAME,null,1);
                    searchCommodities = dbHelper.readCommodities(etInput.getText().toString());
                    adapter.setData(searchCommodities);
                    lvTips.setAdapter(adapter);
                }
            }
        });




        etInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}//文本改变之前执行
            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏“删除”图片
                    ivDelete.setVisibility(View.GONE);

                } else {//长度不为0
                    //显示“删除图片”
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
            //文本改变之后执行
            public void afterTextChanged(Editable s) {
            }
        });
        //返回
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //每个item的点击事件
        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set edit text
                String text = lvTips.getAdapter().getItem(i).toString();
                etInput.setText(text);
                etInput.setSelection(text.length());
                //hint list view gone and result list view show
                lvTips.setVisibility(GONE);
                notifyStartSearching(text);
            }
        });

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                title = findViewById(R.id.tv_title);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lvTips.setVisibility(GONE);
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


    }
    // 设置热搜版提示 adapter
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lvTips.getAdapter() == null) {
            lvTips.setAdapter(mHintAdapter);
        }
    }
    //设置自动补全adapter
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)   {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
                lvTips.setVisibility(VISIBLE);
                if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {
                    lvTips.setAdapter(mAutoCompleteAdapter);
                }
                //更新autoComplete数据
                if (mListener != null) {
                    mListener.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                ivDelete.setVisibility(GONE);
                if (mHintAdapter != null) {
                    lvTips.setAdapter(mHintAdapter);
                }
                lvTips.setVisibility(GONE);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_input:
                lvTips.setVisibility(VISIBLE);
                break;
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
        }
    }
    //search view回调方法
    public interface SearchViewListener {
        //更新自动补全内容 传入补全后的文本
        void onRefreshAutoComplete(String text);
         //开始搜索 传入输入框的文本
        void onSearch(String text);
    }

}
