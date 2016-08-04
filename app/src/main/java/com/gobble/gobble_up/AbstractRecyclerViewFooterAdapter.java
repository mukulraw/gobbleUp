package com.gobble.gobble_up;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public abstract class AbstractRecyclerViewFooterAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VISIBLE_THRESHOLD = 5;

    private final int ITEM_VIEW_TYPE_BASIC = 0;
    private final int ITEM_VIEW_TYPE_FOOTER = 1;


    private ArrayList<T> dataSet;

    private int firstVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private boolean loading = true;

    public AbstractRecyclerViewFooterAdapter(RecyclerView recyclerView, ArrayList<T> dataSet, final OnLoadMoreListener onLoadMoreListener) {
        this.dataSet = dataSet;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                        // End has been reached

                        addItem(null);
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }

    public void resetItems(@NonNull ArrayList<T> newDataSet) {
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        previousTotal = 0;

        dataSet.clear();
        addItems(newDataSet);
    }

    public void addItems(@NonNull ArrayList<T> newDataSetItems) {
        dataSet.addAll(newDataSetItems);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return onCreateBasicItemViewHolder(parent, viewType);
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder genericHolder, int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_BASIC) {
            onBindBasicItemView(genericHolder, position);
        } else {
            onBindFooterView(genericHolder, position);
        }
    }

    abstract RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    abstract void onBindBasicItemView(RecyclerView.ViewHolder genericHolder, int position);

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        //noinspection ConstantConditions
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_bar, parent, false);
        return new ProgressViewHolder(v);
    }
     public void onBindFooterView(RecyclerView.ViewHolder genericHolder, int position) {
        ((ProgressViewHolder) genericHolder).progressBar.setIndeterminate(true);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar)
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
