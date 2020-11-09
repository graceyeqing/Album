/*
 * Copyright 2016 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.album.app.album;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;

import java.util.List;

/**
 * <p>Folder preview.</p>
 * Created by Yan yeqing on 2020/11/9.
 */
public class FolderDialog2 extends PopupWindow {

    private Widget mWidget;
    private FolderAdapter mFolderAdapter;
    private List<AlbumFolder> mAlbumFolders;

    private int mCurrentPosition = 0;
    private OnItemClickListener mItemClickListener;

    public FolderDialog2(Context context, Widget widget, List<AlbumFolder> albumFolders, OnItemClickListener itemClickListener) {
        this.mWidget = widget;
        this.mAlbumFolders = albumFolders;
        this.mItemClickListener = itemClickListener;

        View view = View.inflate(context, R.layout.album_dialog_floder, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_content_list);
        LinearLayout ll_back =  (LinearLayout)view.findViewById(R.id.ll_back);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setOutsideTouchable(true);
        setFocusable(true);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mFolderAdapter = new FolderAdapter(context, mAlbumFolders, widget.getBucketItemCheckSelector());
        mFolderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if (mCurrentPosition != position) {
                    mAlbumFolders.get(mCurrentPosition).setChecked(false);
                    mFolderAdapter.notifyItemChanged(mCurrentPosition);

                    mCurrentPosition = position;
                    mAlbumFolders.get(mCurrentPosition).setChecked(true);
                    mFolderAdapter.notifyItemChanged(mCurrentPosition);

                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, position);
                    }
                }
                dismiss();
            }
        });
        recyclerView.setAdapter(mFolderAdapter);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = this.getHeight()-rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}