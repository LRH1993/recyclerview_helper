package com.lvr.recyclerview_helper.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lvr.library.adapter.CommonAdapter;
import com.lvr.library.anims.adapters.ScaleInAnimationAdapter;
import com.lvr.library.anims.animators.LandingAnimator;
import com.lvr.library.holder.BaseViewHolder;
import com.lvr.library.recyclerview.HRecyclerView;
import com.lvr.library.recyclerview.OnLoadMoreListener;
import com.lvr.library.recyclerview.OnRefreshListener;
import com.lvr.recyclerview_helper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lvr on 2017/5/25.
 */
public class AllActivity extends AppCompatActivity implements OnLoadMoreListener, OnRefreshListener {
    private String[] mStrings = {
            "Apple", "Ball", "Camera", "Day", "Egg", "Foo", "Google", "Hello", "Iron", "Japan", "Coke",
            "Dog", "Cat", "Yahoo", "Sony", "Canon", "Fujitsu", "USA", "Nexus", "LINE", "Haskell", "C++",
             "Go"
    };
    private int[] mImageRes = {R.drawable.image1, R.drawable.image2};
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                //刷新完毕
                mRecyclerView.setRefreshing(false);
            }else if(count==4){
                //没有更多数据
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            }else{
                //加载更多完毕
                mDatas.add("Alibaba");
                mDatas.add("Java");
                mDatas.add("Kotlin");
                mDatas.add("Swift");
                mAdapter.notifyItemInserted(mDatas.size()+1);
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }
        }
    };
    private List<String> mDatas;
    private CommonAdapter<String> mAdapter;
    private int count =1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        mRecyclerView = (HRecyclerView) findViewById(R.id.list);
        mDatas = new ArrayList<>();
        for(int i=0;i<mStrings.length;i++){
            mDatas.add(mStrings[0]);
        }
        mAdapter = new CommonAdapter<String>(this, R.layout.item_common, mDatas) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_content,mDatas.get(position));
                int number = new Random().nextInt(2);
                holder.setImageResource(R.id.iv_content,mImageRes[number]);
            }
        };
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);
        mRecyclerView.setAdapter(scaleAdapter);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                System.out.println("点击");
                showMyDialog("响应点击事件");
            }
        });
        mAdapter.setOnItemLongClickListener(new CommonAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                showMyDialog("响应长按事件");
                return false;
            }
        });
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setRefreshing(true);
            }
        });
    }

    public void showMyDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(AllActivity.this);
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

    @Override
    public void onLoadMore() {
        if(mLoadMoreFooterView.canLoadMore()){
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Message message = Message.obtain();
                    message.what =count;
                    count++;
                    mHandler.sendMessage(message);
                }
            }).start();
        }

    }

    @Override
    public void onRefresh() {
        System.out.println("刷新了");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                Message msg = Message.obtain();
                msg.what=0;
                mHandler.sendMessage(msg);
            }
        }).start();

    }
}
