package xyz.truenight.quotations.util;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public final class VoidSelectionActionModeCallback implements ActionMode.Callback {

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    public void onDestroyActionMode(ActionMode mode) {
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return true;
    }
}