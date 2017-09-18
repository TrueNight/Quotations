package xyz.truenight.quotations.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.jetbrains.annotations.NotNull;

public class OnSwipeItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerView mRecycler;
    private final OnItemSwipedListener mListener;

    public OnSwipeItemTouchHelperCallback(@NotNull RecyclerView view, @NotNull OnItemSwipedListener listener) {
        super(0, ItemTouchHelper.START);
        mRecycler = view;
        mListener = listener;

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
//        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        if (mAdapter.isPendingRemoval(viewHolder)) {
//            return 0;
//        }
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal
        mListener.onItemSwiped(mRecycler, viewHolder.itemView, viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }
}