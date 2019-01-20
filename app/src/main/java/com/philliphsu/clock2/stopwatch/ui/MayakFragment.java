/*
 * Copyright 2017 Phillip Hsu
 *
 * This file is part of ClockPlus.
 *
 * ClockPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClockPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClockPlus.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.philliphsu.clock2.stopwatch.ui;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philliphsu.clock2.R;
import com.philliphsu.clock2.list.RecyclerViewFragment;
import com.philliphsu.clock2.stopwatch.Lap;
import com.philliphsu.clock2.stopwatch.data.LapCursor;
import com.philliphsu.clock2.stopwatch.data.LapsCursorLoader;
import com.philliphsu.clock2.util.DelayedSnackbarHandler;

import java.lang.ref.WeakReference;

/**
 * Created by Sergey Batsura 08.01.2019
 */
public class MayakFragment extends RecyclerViewFragment<
        Lap,
        LapViewHolder,
        LapCursor,
        LapsAdapter> {
    private static final String TAG = "MayakFragment";

    private WeakReference<FloatingActionButton> mActivityFab;
    private Drawable mStartDrawable;
    private Drawable mStopDrawable;
    private boolean running;

    private SharedPreferences mPrefs;



    /**
     * This is called only when a new instance of this Fragment is being created,
     * especially if the user is navigating to this tab for the first time in
     * this app session.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_start_24dp);
        mStopDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_stop_24dp);
        running = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivityFab = new WeakReference<>((FloatingActionButton) getActivity().findViewById(R.id.fab));
    }

    /**
     * If the user navigates away, this is the furthest point in the lifecycle
     * this Fragment gets to. Here, the view hierarchy returned from onCreateView()
     * is destroyed--the Fragment itself is NOT destroyed. If the user navigates back
     * to this tab, this Fragment goes through its lifecycle beginning from onCreateView().
     * <p/>
     * TODO: Verify that members are not reset.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
    }

    @Override
    protected boolean hasEmptyView() {
        return false;
    }

    @Override
    public Loader<LapCursor> onCreateLoader(int id, Bundle args) {
        return new LapsCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<LapCursor> loader, LapCursor data) {
        Log.d(TAG, "onLoadFinished()");
        super.onLoadFinished(loader, data);
    }

    @Override
    public void onFabClick() {
        running = !running;
        syncFabIconWithStopwatchState(running/*invert the current state*/);
        if ( running )
            DelayedSnackbarHandler.prepareMessage("Сигналы радио Маяк активированы!");
        else
            DelayedSnackbarHandler.prepareMessage("Сигналы радио Маяк остановлены!");

        DelayedSnackbarHandler.makeAndShow(getView());
    }

    private void syncFabIconWithStopwatchState(boolean running) {
        mActivityFab.get().setImageDrawable(running ? mStopDrawable : mStartDrawable);
    }

    @Override
    public void onPageSelected() {

        mActivityFab.get().setImageDrawable(mStartDrawable);

        DelayedSnackbarHandler.prepareMessage("Сигналы радио Маяк. Каждый час с 10 до 19");
        DelayedSnackbarHandler.makeAndShow(getView());

    }

    @Override
    protected LapsAdapter onCreateAdapter() {
        return new LapsAdapter();
    }



    @Override
    protected void onScrolledToStableId(long id, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onListItemClick(Lap item, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onListItemDeleted(Lap item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onListItemUpdate(Lap item, int position) {
        throw new UnsupportedOperationException();
    }
}
