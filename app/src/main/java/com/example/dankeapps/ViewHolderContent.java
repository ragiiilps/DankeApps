package com.example.dankeapps;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

public class ViewHolderContent extends RecyclerView.ViewHolder {

    private TextView mJudulPst, mUpahPst;
    View mView;

    public ViewHolderContent(@NonNull View itemView) {
        super(itemView);
        mJudulPst = itemView.findViewById(R.id.card_content_judul);
        mUpahPst = itemView.findViewById(R.id.card_content_upah);
    }

    public void bind(ModelContent modelContent){
        mJudulPst.setText(modelContent.Judul);
        mUpahPst.setText(modelContent.Upah);
    }
}
