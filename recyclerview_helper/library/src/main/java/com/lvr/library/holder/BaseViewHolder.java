package com.lvr.library.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 基础的ViewHolder
 * ViewHolder只作View的缓存,避免每次findViewById，从而提升运行的效率
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
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

    /**
     * 给TextView设置内容
     *
     * @param viewId TextView的id
     * @param text   字符串内容
     */
    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

    /**
     * 给ImageView设置图片
     *
     * @param viewId     ImageView的id
     * @param drawableId 图片资源
     */
    public void setImageResource(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
    }

    /**
     * 给ImageView设置图片
     *
     * @param viewId ImageView的id
     * @param bitmap 图片资源
     */
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
    }
    /**
     * 给ImageView设置图片
     *
     * @param viewId ImageView的id
     * @param drawable 图片资源
     */
    public void setImageBitmap(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
    }

}
