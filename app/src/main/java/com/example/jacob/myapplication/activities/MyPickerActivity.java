package com.example.jacob.myapplication.activities;

import com.example.jacob.myapplication.FilteredFilePicker;
import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;

import java.io.File;

public class MyPickerActivity extends AbstractFilePickerActivity<File> {

    public MyPickerActivity() {
        super();
    }

    @Override
    protected AbstractFilePickerFragment<File> getFragment(
            final String startPath, final int mode, final boolean allowMultiple,
            final boolean allowCreateDir, final boolean allowExistingFile,
            final boolean singleClick) {
        // Only the fragment in this line needs to be changed
        AbstractFilePickerFragment<File> fragment = new FilteredFilePicker();
        fragment.setArgs(startPath, mode, allowMultiple, allowCreateDir, allowExistingFile, singleClick);
        return fragment;
    }
}
