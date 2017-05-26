package com.lvr.recyclerview_helper.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lvr.library.adapter.CommonAdapter;
import com.lvr.library.adapter.MultiItemCommonAdapter;
import com.lvr.library.anims.adapters.ScaleInAnimationAdapter;
import com.lvr.library.anims.adapters.SlideInBottomAnimationAdapter;
import com.lvr.library.anims.animators.FlipInLeftYAnimator;
import com.lvr.library.anims.animators.LandingAnimator;
import com.lvr.library.holder.BaseViewHolder;
import com.lvr.library.recyclerview.HRecyclerView;
import com.lvr.library.support.MultiItemTypeSupport;
import com.lvr.recyclerview_helper.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private HRecyclerView mRecyclerView;
    private Spinner mSpinner;
    private Toolbar mToolbar;
    private String[] mTypes = {"单类型", "多类型", "单类型+动画", "多类型+动画", "单类型+点击", "多类型+点击","单类型+头/尾","多类型+头/尾"};
    private String[] mStrings = {
            "Google", "Hello", "Iron", "Japan", "Coke", "Yahoo", "Sony", "Canon", "Fujitsu", "USA", "Nexus", "LINE", "Haskell", "C++",
            "Java", "Go", "Swift", "Objective-c", "Ruby", "PHP", "Bash", "ksh", "C", "Groovy", "Kotlin"
    };
    private int[] mImageRes = {R.drawable.image1, R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6};
    private int TYPE_HEAD = 0;
    private int TYPE_COMMON = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        Button button = (Button) findViewById(R.id.btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView = (HRecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        setSupportActionBar(mToolbar);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        spinnerAdapter.addAll(Arrays.asList(mTypes));
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setSingleItem();
                }
                if (position == 1) {
                    setMultiItem();
                }
                if (position == 2) {
                    setSingleWithAnimItem();
                }
                if (position == 3) {
                    setMultiWithAnimItem();
                }
                if (position == 4) {
                    setSingleItemWithClick();
                }
                if (position == 5) {
                    setMultiItemWithClick();
                }
                //每点击一次就会多加一对头尾布局
                if(position==6){
                    setSingleItemWithView();
                }
                //每点击一次就会多加一对头尾布局
                if(position==7){
                    setMultiItemWithView();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    /**
     * 多类型+头/尾
     */
    private void setMultiItemWithView() {
        MultiItemCommonAdapter<String> adapter = multiSetting();

        View headView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_head,null,false);
        View footView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_foot,null,false);
        mRecyclerView.addHeaderView(headView);
        mRecyclerView.addFooterView(footView);
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 单类型+头/尾
     */
    private void setSingleItemWithView() {
        CommonAdapter<String> adapter = singleSetting();

        View headView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_head,null,false);
        View footView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_foot,null,false);
        mRecyclerView.addHeaderView(headView);
        mRecyclerView.addFooterView(footView);
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 多类型+点击
     */
    private void setMultiItemWithClick() {
        MultiItemCommonAdapter<String> adapter = multiSetting();
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showMyDialog("点击事件");
            }
        });
        adapter.setOnItemLongClickListener(new CommonAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                showMyDialog("长按事件");
                return false;
            }
        });
        mRecyclerView.setItemAnimator(new FlipInLeftYAnimator());
        SlideInBottomAnimationAdapter scaleAdapter = new SlideInBottomAnimationAdapter(adapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);
        mRecyclerView.setAdapter(scaleAdapter);
    }

    /**
     * 单类型+点击
     */
    private void setSingleItemWithClick() {
        CommonAdapter<String> adapter = singleSetting();
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                System.out.println("点击");
                showMyDialog("响应点击事件");
            }
        });
        adapter.setOnItemLongClickListener(new CommonAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                showMyDialog("响应长按事件");
                return false;
            }
        });
        mRecyclerView.setItemAnimator(new LandingAnimator());
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);
        mRecyclerView.setAdapter(scaleAdapter);

    }

    /**
     * 多类型+动画
     */
    private void setMultiWithAnimItem() {
        MultiItemCommonAdapter<String> adapter = multiSetting();
        mRecyclerView.setItemAnimator(new FlipInLeftYAnimator());
        SlideInBottomAnimationAdapter scaleAdapter = new SlideInBottomAnimationAdapter(adapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);
        mRecyclerView.setAdapter(scaleAdapter);
    }

    /**
     * 单类型+动画
     */
    private void setSingleWithAnimItem() {
        CommonAdapter<String> adapter = singleSetting();
        mRecyclerView.setItemAnimator(new LandingAnimator());
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);
        mRecyclerView.setAdapter(scaleAdapter);
    }


    /**
     * 多类型
     */
    private void setMultiItem() {
        multiSetting();
    }

    /**
     * 单类型
     */
    public void setSingleItem() {
        singleSetting();
    }

    /**
     * 单类型基本设置
     */
    public CommonAdapter<String> singleSetting() {
        List<String> mDatas = Arrays.asList(mStrings);
        CommonAdapter<String> mAdapter = new CommonAdapter<String>(this, R.layout.item_common, mDatas) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_content,mDatas.get(position));
                int number = new Random().nextInt(3);
                holder.setImageResource(R.id.iv_content,mImageRes[number]);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    /**
     * 多类型基本设置
     */
    public MultiItemCommonAdapter<String> multiSetting() {
        MultiItemTypeSupport<String> support = new MultiItemTypeSupport<String>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_HEAD) {
                    return R.layout.item_special;
                } else {
                    return R.layout.item_common;
                }

            }

            @Override
            public int getItemViewType(int position, String s) {
                if (position%3==0&& position>0) {
                    return TYPE_HEAD;
                } else {
                    return TYPE_COMMON;
                }
            }
        };
        List<String> mDatas = Arrays.asList(mStrings);
        MultiItemCommonAdapter<String> mAdapter = new MultiItemCommonAdapter<String>(this, mDatas, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                if (position%3==0&& position>0) {
                    holder.setImageResource(R.id.iv_head,R.drawable.multi_image);
                } else {
                    holder.setText(R.id.tv_content,mDatas.get(position));
                    int number = new Random().nextInt(3)+3;
                    holder.setImageResource(R.id.iv_content,mImageRes[number]);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }
    public void showMyDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("事件类型");
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        builder.setNegativeButton("知道", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }





}
