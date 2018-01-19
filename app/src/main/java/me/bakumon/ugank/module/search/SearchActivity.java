package me.bakumon.ugank.module.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivitySearchBinding;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.entity.History;
import me.bakumon.ugank.entity.SearchResult;
import me.bakumon.ugank.module.bigimg.BigimgActivity;
import me.bakumon.ugank.module.webview.WebViewActivity;
import me.bakumon.ugank.utills.KeyboardUtils;
import me.bakumon.ugank.utills.MDTintUtil;
import me.bakumon.ugank.widget.RecycleViewDivider;

/**
 * 搜索
 *
 * @author bakumon https://bakumon.me
 */
public class SearchActivity extends BaseActivity implements SearchContract.View, TextWatcher, TextView.OnEditorActionListener, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private SearchContract.Presenter mSearchPresenter = new SearchPresenter(this);

    private ActivitySearchBinding binding;

    private SearchListAdapter mSearchListAdapter;
    private HistoryListAdapter mHistoryListAdapter;

    /**
     * 打开搜索
     *
     * @param activity activity
     */
    public static void openSearchActivity(Activity activity) {
        activity.startActivity(new Intent(activity, SearchActivity.class));
    }

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

        mSearchListAdapter = new SearchListAdapter(null);
        mSearchListAdapter.setOnItemChildClickListener(this);
        mSearchListAdapter.setOnLoadMoreListener(this, binding.recyclerViewSearch);
        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSearch.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        binding.recyclerViewSearch.setAdapter(mSearchListAdapter);

        mHistoryListAdapter = new HistoryListAdapter(null);

        mHistoryListAdapter.setOnItemChildClickListener(this);

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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_item_content_history:
                // 搜索历史 item
                History history = mHistoryListAdapter.getData().get(position);
                if (history.getContent() == null) {
                    return;
                }
                KeyboardUtils.hideSoftInput(this);
                binding.edSearch.setText(history.getContent());
                binding.edSearch.setSelection(binding.edSearch.getText().toString().length());
                mSearchPresenter.search(history.getContent(), false);
                break;
            case R.id.ll_item_search:
                // 搜索结果 item
                if (mSearchListAdapter.getData().get(position) == null) {
                    Toasty.error(this, "数据异常").show();
                    return;
                }
                Intent intent = new Intent();
                if (TextUtils.equals("福利", mSearchListAdapter.getData().get(position).type)) {
                    BigimgActivity.openBigimgActivity(this,
                            mSearchListAdapter.getData().get(position).url,
                            mSearchListAdapter.getData().get(position).desc);
                } else {
                    intent.setClass(this, WebViewActivity.class);
//                    intent.putExtra(WebViewActivity.GANK_TITLE, mSearchListAdapter.getData().get(position).desc);
//                    intent.putExtra(WebViewActivity.GANK_URL, mSearchListAdapter.getData().get(position).url);
                    Favorite favorite = new Favorite();
                    favorite.setAuthor(mSearchListAdapter.getData().get(position).who);
                    favorite.setData(mSearchListAdapter.getData().get(position).publishedAt);
                    favorite.setTitle(mSearchListAdapter.getData().get(position).desc);
                    favorite.setType(mSearchListAdapter.getData().get(position).type);
                    favorite.setUrl(mSearchListAdapter.getData().get(position).url);
                    favorite.setGankID(mSearchListAdapter.getData().get(position).ganhuo_id);
//                    intent.putExtra(WebViewActivity.FAVORITE_DATA, favorite);

                    WebViewActivity.openWebViewActivity(this,
                            mSearchListAdapter.getData().get(position).url,
                            mSearchListAdapter.getData().get(position).desc,
                            favorite);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
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
        mSearchListAdapter.setNewData(searchResult.results);
        binding.swipeRefreshLayoutSearch.setRefreshing(false);
    }

    @Override
    public void addSearchItems(SearchResult searchResult) {
        mSearchListAdapter.addData(searchResult.results);
        mSearchListAdapter.loadMoreComplete();
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
        mSearchListAdapter.loadMoreEnd();
    }

    @Override
    public void setEmpty() {

    }

    @Override
    public void setLoading() {

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
        mHistoryListAdapter.setNewData(history);
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
    public void onLoadMoreRequested() {
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
            default:
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
            mSearchListAdapter.setNewData(null);
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
}
