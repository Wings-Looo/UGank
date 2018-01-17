package me.bakumon.ugank.module.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivitySearchBinding;
import me.bakumon.ugank.entity.History;
import me.bakumon.ugank.entity.SearchResult;
import me.bakumon.ugank.utills.KeyboardUtils;
import me.bakumon.ugank.utills.MDTintUtil;
import me.bakumon.ugank.widget.RecycleViewDivider;
import me.bakumon.ugank.widget.recyclerviewwithfooter.OnLoadMoreListener;

/**
 * 搜索
 *
 * @author bakumon https://bakumon.me
 */
public class SearchActivity extends BaseActivity implements SearchContract.View, TextWatcher, TextView.OnEditorActionListener, OnLoadMoreListener, HistoryListAdapter.OnItemClickListener, View.OnClickListener {

    private SearchContract.Presenter mSearchPresenter = new SearchPresenter(this);

    private ActivitySearchBinding binding;

    private SearchListAdapter mSearchListAdapter;
    private HistoryListAdapter mHistoryListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        initView();
        mSearchPresenter.subscribe();
        mSearchPresenter.queryHistory();
    }

    private void initView() {

        setSupportActionBar(binding.toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.edSearch.addTextChangedListener(this);
        binding.edSearch.setOnEditorActionListener(this);

        binding.swipeRefreshLayoutSearch.setColorSchemeResources(
                R.color.colorSwipeRefresh1,
                R.color.colorSwipeRefresh2,
                R.color.colorSwipeRefresh3,
                R.color.colorSwipeRefresh4,
                R.color.colorSwipeRefresh5,
                R.color.colorSwipeRefresh6);
        binding.swipeRefreshLayoutSearch.setRefreshing(false);
        binding.swipeRefreshLayoutSearch.setEnabled(false);

        mSearchListAdapter = new SearchListAdapter(this);
        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSearch.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        binding.recyclerViewSearch.setAdapter(mSearchListAdapter);
        binding.recyclerViewSearch.setOnLoadMoreListener(this);
        binding.recyclerViewSearch.setEmpty();

        mHistoryListAdapter = new HistoryListAdapter(this);

        mHistoryListAdapter.setOnItemClickListener(this);
        mHistoryListAdapter.mData = null;
        binding.recyclerSearchHistory.setLayoutManager(new FlexboxLayoutManager(getApplicationContext()));
        binding.recyclerSearchHistory.setAdapter(mHistoryListAdapter);

        binding.emojiRainLayout.addEmoji(R.mipmap.emoji1);
        binding.emojiRainLayout.addEmoji(R.mipmap.emoji2);
        binding.emojiRainLayout.addEmoji(R.mipmap.emoji3);
        binding.emojiRainLayout.addEmoji(R.mipmap.emoji4);
        binding.emojiRainLayout.addEmoji(R.mipmap.emoji5);
        binding.emojiRainLayout.addEmoji(R.mipmap.emoji6);

        binding.ivSearch.setOnClickListener(this);
        binding.ivEditClear.setOnClickListener(this);
        binding.tvSearchClean.setOnClickListener(this);

    }

    @Override
    protected View[] setImmersiveView() {
        return new View[]{binding.toolbarSearch};
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.unsubscribe();
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        binding.appbarSearch.setBackgroundColor(color);
    }

    @Override
    public void setEditTextCursorColor(int cursorColor) {
        MDTintUtil.setCursorTint(binding.edSearch, cursorColor);
    }

    @Override
    public void showEditClear() {
        binding.ivEditClear.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEditClear() {
        binding.ivEditClear.setVisibility(View.GONE);
    }

    @Override
    public void showSearchFail(String failMsg) {
        Toasty.error(this, failMsg).show();
    }

    @Override
    public void setSearchItems(SearchResult searchResult) {
        mSearchListAdapter.mData = searchResult.results;
        mSearchListAdapter.notifyDataSetChanged();
        binding.swipeRefreshLayoutSearch.setRefreshing(false);
    }

    @Override
    public void addSearchItems(SearchResult searchResult) {
        int start = mSearchListAdapter.getItemCount();
        mSearchListAdapter.mData.addAll(searchResult.results);
        mSearchListAdapter.notifyItemRangeInserted(start, searchResult.results.size());
    }

    @Override
    public void showSwipLoading() {
        binding.swipeRefreshLayoutSearch.setRefreshing(true);
    }

    @Override
    public void hideSwipLoading() {
        binding.swipeRefreshLayoutSearch.setRefreshing(false);
    }

    @Override
    public void showTip(String msg) {
        Toasty.warning(this, msg).show();
    }

    @Override
    public void setLoadMoreIsLastPage() {
        binding.recyclerViewSearch.setEnd("没有更多数据了");
    }

    @Override
    public void setEmpty() {
        binding.recyclerViewSearch.setEmpty();
    }

    @Override
    public void setLoading() {
        binding.recyclerViewSearch.setLoading();
    }

    @Override
    public void showSearchResult() {
        binding.llSearchHistory.setVisibility(View.GONE);
        binding.swipeRefreshLayoutSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchHistory() {
        binding.llSearchHistory.setVisibility(View.VISIBLE);
        binding.swipeRefreshLayoutSearch.setVisibility(View.GONE);
    }

    @Override
    public void setHistory(List<History> history) {
        mHistoryListAdapter.mData = history;
        mHistoryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void startEmojiRain() {
        binding.emojiRainLayout.startDropping();
    }

    @Override
    public void stopEmojiRain() {
        binding.emojiRainLayout.stopDropping();
    }

    @Override
    public void onLoadMore() {
        mSearchPresenter.search(binding.edSearch.getText().toString().trim(), true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                search();
                break;
            case R.id.tv_search_clean:
                cleanHistory();
                break;
            case R.id.iv_edit_clear:
                editClear();
                break;
        }
    }

    private void search() {
        KeyboardUtils.hideSoftInput(this);
        mSearchPresenter.search(binding.edSearch.getText().toString().trim(), false);
    }

    private void cleanHistory() {
        mSearchPresenter.deleteAllHistory();
    }

    public void editClear() {
        binding.recyclerViewSearch.setEmpty();
        binding.edSearch.setText("");
        KeyboardUtils.showSoftInput(this, binding.edSearch);
        hideSwipLoading();
        showSearchHistory();
        mSearchPresenter.unsubscribe();
        mSearchPresenter.queryHistory();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() > 0) {
            showEditClear();
        } else {
            hideEditClear();
            hideSwipLoading();
            mSearchPresenter.unsubscribe();
            binding.recyclerViewSearch.setEmpty();
            mSearchListAdapter.mData = null;
            mSearchListAdapter.notifyDataSetChanged();
            showSearchHistory();
            mSearchPresenter.queryHistory();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
        }
        return false;
    }

    @Override
    public void OnItemClick(History history) {
        if (history == null || history.getContent() == null) {
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        binding.edSearch.setText(history.getContent());
        binding.edSearch.setSelection(binding.edSearch.getText().toString().length());
        mSearchPresenter.search(history.getContent(), false);
    }
}
