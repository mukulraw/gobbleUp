package com.gobble.gobble_up;

/**
 * Created by hi on 7/29/2016.
 */

interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
