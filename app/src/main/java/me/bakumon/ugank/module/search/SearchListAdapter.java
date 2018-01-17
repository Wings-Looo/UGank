package me.bakumon.ugank.module.search;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.bakumon.ugank.R;
import me.bakumon.ugank.entity.SearchResult;
import me.bakumon.ugank.utills.DateUtil;

/**
 * 搜索结果 Adapter
 *
 * @author bakumon https://bakumon.me
 * @date 2016/12/20
 */

public class SearchListAdapter extends BaseQuickAdapter<SearchResult.ResultsBean, BaseViewHolder> {

    public SearchListAdapter(@Nullable List<SearchResult.ResultsBean> data) {
        super(R.layout.item_search, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResult.ResultsBean item) {
        helper.setText(R.id.tv_item_title_search, item.desc == null ? "unknown" : item.desc)
                .setText(R.id.tv_item_type_search, item.type == null ? "unknown" : item.type)
                .setText(R.id.tv_item_publisher_search, item.who == null ? "unknown" : item.who)
                .setText(R.id.tv_item_time_search, DateUtil.dateFormat(item.publishedAt))
                .addOnClickListener(R.id.ll_item_search);
    }
}
