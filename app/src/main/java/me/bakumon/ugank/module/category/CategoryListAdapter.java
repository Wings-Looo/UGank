package me.bakumon.ugank.module.category;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.bakumon.ugank.ConfigManage;
import me.bakumon.ugank.GlobalConfig;
import me.bakumon.ugank.R;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.utills.DateUtil;

/**
 * CategoryListAdapter
 * Created by bakumon on 2016/10/13.
 */

public class CategoryListAdapter extends BaseQuickAdapter<CategoryResult.ResultsBean, BaseViewHolder> {

    public CategoryListAdapter( @Nullable List<CategoryResult.ResultsBean> data) {
        super(R.layout.item, data);
    }

//    public void convert(CommonHolder4RecyclerView holder, CategoryResult.ResultsBean androidResult) {
//        if (androidResult != null) {
//            AppCompatImageView imageView = holder.getView(R.id.iv_item_img);
//            if (ConfigManage.INSTANCE.isListShowImg()) { // 列表显示图片
//                imageView.setVisibility(View.VISIBLE);
//                String quality = "";
//                if (androidResult.images != null && androidResult.images.size() > 0) {
//                    switch (ConfigManage.INSTANCE.getThumbnailQuality()) {
//                        case 0: // 原图
//                            quality = "?imageView2/0/w/400";
//                            break;
//                        case 1: // 默认
//                            quality = "?imageView2/0/w/280";
//                            break;
//                        case 2: // 省流
//                            quality = "?imageView2/0/w/190";
//                            break;
//                    }
//                    imageView.setVisibility(View.VISIBLE);
////                    Picasso.with(mContext).setIndicatorsEnabled(true);//显示指示器
//                    Picasso.with(mContext)
//                            .load(androidResult.images.get(0) + quality)
//                            .placeholder(R.mipmap.image_default)
//                            .tag(GlobalConfig.PICASSO_TAG_THUMBNAILS_CATEGORY_LIST_ITEM)
//                            .centerCrop()
//                            .fit()
//                            .config(Bitmap.Config.RGB_565)
//                            .into(imageView);
//                } else { // 图片 URL 不存在
//                    imageView.setVisibility(View.GONE);
//                }
//            } else { // 列表不显示图片
//                imageView.setVisibility(View.GONE);
//            }
//            holder.setTextViewText(R.id.tv_item_title, androidResult.desc == null ? "unknown" : androidResult.desc);
//            holder.setTextViewText(R.id.tv_item_publisher, androidResult.who == null ? "unknown" : androidResult.who);
//            holder.setTextViewText(R.id.tv_item_time, DateUtil.dateFormat(androidResult.publishedAt));
//            holder.setOnClickListener(this, R.id.ll_item);
//        }
//    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryResult.ResultsBean item) {
        if (item != null) {
            AppCompatImageView imageView = helper.getView(R.id.iv_item_img);
            if (ConfigManage.INSTANCE.isListShowImg()) { // 列表显示图片
                imageView.setVisibility(View.VISIBLE);
                String quality = "";
                if (item.images != null && item.images.size() > 0) {
                    switch (ConfigManage.INSTANCE.getThumbnailQuality()) {
                        case 0: // 原图
                            quality = "?imageView2/0/w/400";
                            break;
                        case 1: // 默认
                            quality = "?imageView2/0/w/280";
                            break;
                        case 2: // 省流
                            quality = "?imageView2/0/w/190";
                            break;
                    }
                    imageView.setVisibility(View.VISIBLE);
//                    Picasso.with(mContext).setIndicatorsEnabled(true);//显示指示器
                    Picasso.with(mContext)
                            .load(item.images.get(0) + quality)
                            .placeholder(R.mipmap.image_default)
                            .tag(GlobalConfig.PICASSO_TAG_THUMBNAILS_CATEGORY_LIST_ITEM)
                            .centerCrop()
                            .fit()
                            .config(Bitmap.Config.RGB_565)
                            .into(imageView);
                } else { // 图片 URL 不存在
                    imageView.setVisibility(View.GONE);
                }
            } else { // 列表不显示图片
                imageView.setVisibility(View.GONE);
            }
            helper.setText(R.id.tv_item_title, item.desc == null ? "unknown" : item.desc)
                    .setText(R.id.tv_item_publisher, item.who == null ? "unknown" : item.who)
                    .setText(R.id.tv_item_time, DateUtil.dateFormat(item.publishedAt))
                    .addOnClickListener(R.id.ll_item);
        }
    }

//    @Override
//    public void onClick(View v, int position, CommonHolder4RecyclerView holder) {
//        if (mData == null || mData.get(position) == null) {
//            Toasty.error(mContext, "数据异常").show();
//            return;
//        }
//        Intent intent = new Intent(mContext, WebViewActivity.class);
//        intent.putExtra(WebViewActivity.GANK_TITLE, mData.get(position).desc);
//        intent.putExtra(WebViewActivity.GANK_URL, mData.get(position).url);
//        Favorite favorite = new Favorite();
//        favorite.setAuthor(mData.get(position).who);
//        favorite.setData(mData.get(position).publishedAt);
//        favorite.setTitle(mData.get(position).desc);
//        favorite.setType(mData.get(position).type);
//        favorite.setUrl(mData.get(position).url);
//        favorite.setGankID(mData.get(position)._id);
//        intent.putExtra(WebViewActivity.FAVORITE_DATA, favorite);
//        mContext.startActivity(intent);
//    }
}
