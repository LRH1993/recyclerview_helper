>RecyclerView作为列表使用，在项目中的应用场景实在是太普遍了。针对项目应用，主要使用了RecyclerView的单或多类型Item，点击/长按事件，ItemAnimator动画效果以及上拉加载、下拉刷新。recyclerview_helper就是针对以上应用场景进行的封装与使用，避免在项目使用中重复的敲代码以及依赖多个库或者自定义实现等复杂方式。


## 一、简介

![](http://upload-images.jianshu.io/upload_images/3985563-f84a7b0f0d300690.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
如上所示，recyclerview_helper针对不同应用场景封装了不同的功能。

**具体功能如下：** 

1.封装了ViewHolder以及Adapter,避免每次都要重复写创建ViewHolder以及重写onCreateViewHolder，onBindViewHolder方法，支持单/多类型Item。

2.封装了OnItemClickListener以及OnItemLongClickListener，不必每次写接口，定义回调。仅支持对Item的点击事件。

3.具有各种炫酷的动画效果，功能装饰者模式封装Adapter添加初始化动画效果 ，以及自定义ItemAnimator实现各种炫酷的Item增删动画效果。

4.支持添加头尾布局，以及支持下拉刷新和上拉加载更多功能。同时支持自定义下拉刷新布局及动画，以及上拉加载更多的布局，可实现各种炫酷效果，跟随你的想象放飞。

## 二、使用
#### 1.添加依赖
①.在项目的 build.gradle 文件中添加
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
②.在 module 的 build.gradle 文件中添加依赖
``` 
dependencies {
            compile 'com.github.LRH1993:recyclerview_helper:V1.0.3'
    }
```
#### 2.单/多类型Item使用

![](http://upload-images.jianshu.io/upload_images/3985563-b0a41886571a77da.gif?imageMogr2/auto-orient/strip)      
**单类型**    
```
CommonAdapter<String> mAdapter = new CommonAdapter<String>(this, R.layout.item_common, mDatas) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_content,mDatas.get(position));
                int number = new Random().nextInt(3);
                holder.setImageResource(R.id.iv_content,mImageRes[number]);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
```
通过实现CommonAdapter，传入context,布局以及数据，然后实现convert方法，设置view的显示数据就完成了。很简洁方便。

**多类型**  
```
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
-----------------------------------------------------------------------------------------------------------------------------
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
```
和单类型的区别就是需要实现MultiItemTypeSupport，在MultiItemCommonAdapter对象中传入实现的该对象，该对象完成两个方法，功能是通过类型判别Item布局以及通过位置和数据判断返回类型。通过这个对象,避免我们在Adapter中进行类别判断的书写。

该部分实现参考了鸿洋大神对RecyclerView的封装。
#### 3.事件监听使用  
![](http://upload-images.jianshu.io/upload_images/3985563-66bd80ade059df09.gif?imageMogr2/auto-orient/strip)  
事件监听的使用就比较简单了，和正常使用一样。  
```
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
```
#### 4.动画使用  
![](http://upload-images.jianshu.io/upload_images/3985563-16e767bc760f5467.gif?imageMogr2/auto-orient/strip)      
gif录制效果不太明显，实际手机上看着效果还是不错的。    
```
mRecyclerView.setItemAnimator(new LandingAnimator());
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);
        mRecyclerView.setAdapter(scaleAdapter);
```
动画效果分两种：

第一种：adapter初始化item的动画效果

第二种:ItemAnimator定义的动画效果

第一种动画效果使用了装饰者模式，需要再封装一层，然后通过setAdapter添加进去。

第二种直接和平时使用一样。

除此之外，ItemAnimator还有以下高级功能：

**设置动画时长**
```
mRecyclerView.getItemAnimator().setAddDuration(1000);
mRecyclerView.getItemAnimator().setRemoveDuration(1000);
mRecyclerView.getItemAnimator().setMoveDuration(1000);
mRecyclerView.getItemAnimator().setChangeDuration(1000);
```
**插值器** 
```
SlideInLeftAnimator animator = new SlideInLeftAnimator();
animator.setInterpolator(new OvershootInterpolator());
mRecyclerView.setItemAnimator(animator);
```
也可以通过自定义AnimateViewHolder实现类，实现其他动画效果。

Adapter也有一些高级功能：

**动画时长**
```
AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
alphaAdapter.setDuration(1000);
mRecyclerView.setAdapter(alphaAdapter);
```
**插值器**
```
AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
alphaAdapter.setInterpolator(new OvershootInterpolator());
mRecyclerView.setAdapter(alphaAdapter);
```
**是否仅显示一次动画效果**
```
AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
scaleAdapter.setFirstOnly(false);
recyclerView.setAdapter(alphaAdapter);
```
设置成true，则动画效果只在第一次创建Item使用，设置成false，则每次上下滑动都带动画效果。

复杂情况下，可以设置成true。

**复合动画**
```
AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
```

recyclerview_helper中已经自定义了各种动画效果，如果有好的实现动画，可以通过自定义实现。

![](http://upload-images.jianshu.io/upload_images/3985563-e5825e68eef51b93.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
该部分实现参考了recyclerview-animators这个动画库。  
#### 5.添加头/尾布局  
![](http://upload-images.jianshu.io/upload_images/3985563-21e7d86af38a8559.gif?imageMogr2/auto-orient/strip)  
自定义头/尾布局，随意添加。  
```
 View headView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_head,null,false);
        View footView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_foot,null,false);
        mRecyclerView.addHeaderView(headView);
        mRecyclerView.addFooterView(footView);
```
几行代码搞定。
#### 6.下拉刷新/上拉加载  
![](http://upload-images.jianshu.io/upload_images/3985563-ab69df2b3f4aefb4.gif?imageMogr2/auto-orient/strip)    
布局设置    
```
<com.lvr.library.recyclerview.HRecyclerView
        app:loadMoreEnabled="true"
        app:loadMoreFooterLayout="@layout/layout_hrecyclerview_load_more_footer"
        app:refreshEnabled="true"
        app:refreshHeaderLayout="@layout/layout_hrecyclerview_refresh_header"
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```
其中头/尾布局需要自定义View实现。在例子中已经分别实现了一种    
![](http://upload-images.jianshu.io/upload_images/3985563-e48885ab683e2f07.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)    
如果想实现不同的加载图片以及动画效果，可以对比实现。    

首先设置监听
```
mRecyclerView.setOnRefreshListener(this);
mRecyclerView.setOnLoadMoreListener(this);
```
实现回调方法
```
 @Override
    public void onLoadMore() {
        if(mLoadMoreFooterView.canLoadMore()){
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //假装加载耗时数据
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                //假装加载耗时数据
                SystemClock.sleep(1000);
                Message msg = Message.obtain();
                msg.what=0;
                mHandler.sendMessage(msg);
            }
        }).start();

    }
```
开启刷新/关闭刷新
```
mRecyclerView.setRefreshing(true);
```
根据不同情况设置不同加载状态：
```
//正在加载
 mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
  //没有更多数据
mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
//加载完毕
mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//出现错误
mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
```
出现错误，还可以通过实现onRetry方法，实现再次加载。

以上两部分效果参考了IRecyclerView实现。

