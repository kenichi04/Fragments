package com.example.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

public class HeadlinesFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_expandable_list_item_1;

        // Create an array adapter for the list view, using the Ipsum headlines array
        // ListFragmentにはListAdapterを設定する必要がある
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines));
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            // リストにおける選択モードを単一選択モードにする
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    // フラグメントがアクティビティに追加された時に呼び出される（現在は非推奨、onCreateを利用する）
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This make sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    // このフラグメントが持つListViewでリストが選択された場合に、
    // mCallbacに格納されている呼び出し元のActivityのonArticleSelectedを呼び出す
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        // 2ペインレイアウト時、リストが選択状態を維持する
        getListView().setItemChecked(position, true);
    }
}
