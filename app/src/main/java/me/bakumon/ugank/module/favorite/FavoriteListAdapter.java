package me.bakumon.ugank.module.favorite;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.bakumon.ugank.R;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.utills.DateUtil;

/**
 * 收藏列表 Adapter
 *
 * @author bakumon https://bakumon.me
 * @date 2016/12/20
 */

public class FavoriteListAdapter extends BaseQuickAdapter<Favorite, BaseViewHolder> {

    public FavoriteListAdapter(@Nullable List<Favorite> data) {
        super(R.layout.item_favorite, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Favorite item) {
        helper.setText(R.id.tv_item_title_favorite, item.getTitle() == null ? "unknown" : item.getTitle())
                .setText(R.id.tv_item_type_favorite, item.getType() == null ? "unknown" : item.getType())
                .setText(R.id.tv_item_publisher_favorite, item.getAuthor() == null ? "unknown" : item.getAuthor())
                .setText(R.id.tv_item_time_favorite, DateUtil.dateFormat(item.getData()))
                .addOnClickListener(R.id.ll_item_favorite);
    }
}
