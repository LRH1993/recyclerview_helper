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
** 单类型 **
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

** 多类型 **
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

** 设置动画时长**
```
mRecyclerView.getItemAnimator().setAddDuration(1000);
mRecyclerView.getItemAnimator().setRemoveDuration(1000);
mRecyclerView.getItemAnimator().setMoveDuration(1000);
mRecyclerView.getItemAnimator().setChangeDuration(1000);
```
** 插值器** 
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
## 三、原理
上面介绍了使用，那么下面就介绍下实现原理，如果需要自定义实现，可以参考完成。

#### 1.ViewHolder及Adapter封装
参考鸿洋大神的实现，针对ViewHolder的功能：实现View缓存，避免重复findViewById，进行封装。针对Adapter在实际项目中重复书写onCreateViewHolder，onBindViewHolder方法，进行封装处理。

**ViewHolder**
```
    private SparseArray<View> viewArray;
    private View mItemView;

    /**
     * 构造ViewHolder
     *
     * @param parent 父类容器
     * @param resId  布局资源文件id
     */
    public BaseViewHolder(Context context, ViewGroup parent, @LayoutRes int resId) {
        super(LayoutInflater.from(context).inflate(resId, parent, false));
        viewArray = new SparseArray<>();
    }

    /**
     * 获取ItemView
     * @return ItemView
     */
     public View getItemView(){
         return this.itemView;
     }

    /**
     * 获取布局中的View
     *
     * @param viewId view的Id
     * @param <T>    View的类型
     * @return view
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = viewArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewArray.put(viewId, view);
        }
        return (T) view;
    }
```
将View存放在集合类中，进行缓存，getView方法可以在Adapter中直接调用，避免每次创建不同类型的ViewHolder,一个BaseViewHolder搞定一切情况。

**Adapter**
基类CommonAdapter
```
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public CommonAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder viewHolder = new BaseViewHolder(mContext,parent,mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        convert(holder, position);
    }

    public abstract void convert(BaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
.........
```
封装实现onCreateViewHolder方法，并把convert方法抽象出去，实现数据绑定工作。使得结构简单。

针对多类型情况，需要判断类型，设置不同布局，所以需要个帮助类：
```
public interface MultiItemTypeSupport<T>
{
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
```
剩下来基础CommonAdapter实现MultiItemCommonAdapter来应对多类型Item。
```
public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T>
{
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemCommonAdapter(Context context, List<T> datas,
                                  MultiItemTypeSupport<T> multiItemTypeSupport)
    {
        super(context, -1, datas);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        BaseViewHolder holder = new BaseViewHolder(mContext, parent, layoutId);
        return holder;
    }
}
```
整个实现很简单，但是效果不错。关于事件点击监听，很简单，就不介绍了。

#### 2.动画效果实现
先介绍个简单的，Adapter的动画效果：
```
 @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    mAdapter.onBindViewHolder(holder, position);

    int adapterPosition = holder.getAdapterPosition();
    if (!isFirstOnly || adapterPosition > mLastPosition) {
      for (Animator anim : getAnimators(holder.itemView)) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
      }
      mLastPosition = adapterPosition;
    } else {
      ViewHelper.clear(holder.itemView);
    }
  }
```
其实就是实现一个包装类AnimationAdapter，在其中onBindViewHolder方法中添加了动画效果。

下面继续介绍ItemAnimator的实现。关于这部分实现，实际代码比较长。就简要介绍下实现主要流程，具体实现可以看下这篇文章：[recyclerview-animators,让你的RecyclerView与众不同](http://www.jianshu.com/p/0292bf221966)。

其实RecyclerView中已经为我们默认提供了Item动画效果，就是通过这个类：DefaultItemAnimator,而这个类又从哪来？
```
public class DefaultItemAnimator extends SimpleItemAnimator
```
搞清楚源头，那么我们也可以 比照着进行实现动画效果，所以通过继承SimpleItemAnimator实现自定义动画类。
主要针对对animateRemove, animateAdd, animateMove, animateChange 这4个方法的实现，增加数据增、删、动、改的动画效果。
#### 3.头布局/尾布局实现
其实头布局，尾布局的添加和上面实现Adapter动画效果的方式一致，都是通过装饰者模式。

因为下面要实现上拉加载/下拉刷新，所以这两部分布局也像添加.头布局/尾布局一样实现。

先定义一个WrapperAdapter。
```
 @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return REFRESH_HEADER;
        } else if (position == 1) {
            return HEADER;
        } else if (1 < position && position < mAdapter.getItemCount() + 2) {
            return mAdapter.getItemViewType(position - 2);
        } else if (position == mAdapter.getItemCount() + 2) {
            return FOOTER;
        } else if (position == mAdapter.getItemCount() + 3) {
            return LOAD_MORE_FOOTER;
        }

        throw new IllegalArgumentException("Wrong type! Position = " + position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == REFRESH_HEADER) {
            return new RefreshHeaderContainerViewHolder(mRefreshHeaderContainer);
        } else if (viewType == HEADER) {
            return new HeaderContainerViewHolder(mHeaderContainer);
        } else if (viewType == FOOTER) {
            return new FooterContainerViewHolder(mFooterContainer);
        } else if (viewType == LOAD_MORE_FOOTER) {
            return new LoadMoreFooterContainerViewHolder(mLoadMoreFooterContainer);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (1 < position && position < mAdapter.getItemCount() + 2) {
            mAdapter.onBindViewHolder(holder, position - 2);
        }
    }
```
主要是针对上拉加载/下拉刷新，头布局/尾布局进行特殊处理。

其次，由于上面四部分都要独占一行，在LinearLayoutManager下没问题，但是在StaggeredGridLayoutManager以及GridLayoutManager要特殊处理。

**GridLayoutManager **
```
@Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    WrapperAdapter wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
                    if (isFullSpanType(wrapperAdapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position - 2);
                    }
                    return 1;
                }
            });
        }
    }
```
**StaggeredGridLayoutManager**
```
 @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }
```
最后可以放心的添加了，通过自定义RecyclerView，重写setAdapter()方法
```
 @Override
    public void setAdapter(Adapter adapter) {
        mOriginAdapter =adapter;
        ensureRefreshHeaderContainer();
        ensureHeaderViewContainer();
        ensureFooterViewContainer();
        ensureLoadMoreFooterContainer();
        super.setAdapter(new WrapperAdapter(adapter, mRefreshHeaderContainer, mHeaderViewContainer, mFooterViewContainer, mLoadMoreFooterContainer));
    }
```
这样就加入了头/尾布局以及上拉加载/下拉刷新布局。
#### 4.上拉加载/下拉刷新
首先是上拉加载，这个比较简单，通过监听OnScrollListener来实现
```
 @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();


        boolean triggerCondition = visibleItemCount > 0
                && newState == RecyclerView.SCROLL_STATE_IDLE
                && canTriggerLoadMore(recyclerView);

        if (triggerCondition) {
            onLoadMore(recyclerView);
        }
    }

    public boolean canTriggerLoadMore(RecyclerView recyclerView) {
        View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int position = recyclerView.getChildLayoutPosition(lastChild);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount - 1 == position;
    }
```
需要先判断是否触发加载条件，其中主要判断当前是否处于最后一条Item。满足触发条件，触发接口回调onLoadMore()方法。

然后是下拉刷新实现，这个比较复杂，但是说起来也比较简单，是通过onTouchEvent的监听来实现。

首先也是判断触发下拉条件
```
final boolean triggerCondition = isEnabled() && mRefreshEnabled && mRefreshHeaderView != null && isFingerDragging() && canTriggerRefresh();
```
其中主要的是这个方法canTriggerRefresh
```
public boolean canTriggerRefresh() {

        if (mOriginAdapter == null || mOriginAdapter.getItemCount() <= 0) {
            return true;
        }
        View firstChild = getChildAt(0);
        int position = getChildLayoutPosition(firstChild);
        if (position == 0) {
            if (firstChild.getTop() == mRefreshHeaderContainer.getTop()) {
                return true;
            }
        }
        return false;
    }
```
判断当前是否滑动到第一条Item。

满足触发条件，先是让下拉刷新的布局高度随下拉而改变：
```
 private void move(int dy) {
        if (dy != 0) {
            int height = mRefreshHeaderContainer.getMeasuredHeight() + dy;
            setRefreshHeaderContainerHeight(height);
            mRefreshTrigger.onMove(false, false, height);
        }
    }
```
同时在滑动过程中，需要根据高度判断刷新应该处于的状态：下拉刷新，释放刷新，以及动画的实现，在这个过程中需要回调RefreshHeaderView(就是你自定义的下拉刷新View，必须实现RefreshTrigger接口，面向接口编程)。

下面最重要的就是要监听抬起的一瞬间：ACTION_UP。

这个时候分两种情况：一种还处于下拉刷新的状态（下拉长度太短），返回默认状态。二是处于释放刷新状态，需要开启刷新。
```
    private void onFingerUpStartAnimating() {
        if (mStatus == STATUS_RELEASE_TO_REFRESH) {
            startScrollReleaseStatusToRefreshingStatus();
        } else if (mStatus == STATUS_SWIPING_TO_REFRESH) {
            startScrollSwipingToRefreshStatusToDefaultStatus();
        }
    }
```
然后就是开启动画，监听动画实现，更新状态，以及接口回调，虽然不难，但是逻辑还是比较复杂。

## 四、总结

目前由于多类型+头尾以及上拉刷新/下拉加载，判断类型过多，在头布局会出现卡顿，所以在使用多类型的情况下，不建议加入头尾布局，可以考虑重写setAdapter方法，去掉不需要的布局。

从效果，到使用，最后到原理，加深了对RecyclerView的理解，recyclerview_helper可以应对一般的使用场景，不过如有特殊应用场景，也可进行比对自定义实现。

#### Github地址：[https://github.com/LRH1993/recyclerview_helper](https://github.com/LRH1993/recyclerview_helper)，给个star支持下。
