package com.luong.note;

import android.view.View;

public interface ItemClickListener {
    void onClickMember(View view, int position);
    void onClickMoney(View view, int position);
    void onClickItem(View view, int position);
}
