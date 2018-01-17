package me.bakumon.ugank.module.search;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.bakumon.ugank.R;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.entity.History;
import me.bakumon.ugank.utills.DateUtil;

/**
 * 搜索历史 Adapter
 *
 * @author bakumon https://bakumon.me
 * @date 2017/2/18
 */

public class HistoryListAdapter extends BaseQuickAdapter<History, BaseViewHolder> {

    public HistoryListAdapter(@Nullable List<History> data) {
        super(R.layout.item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, History item) {
        helper.setText(R.id.tv_item_content_history, item.getContent() == null ? "unknown" : item.getContent())
                .addOnClickListener(R.id.tv_item_content_history);

    }
}
