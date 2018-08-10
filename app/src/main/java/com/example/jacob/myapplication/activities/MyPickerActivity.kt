package com.example.jacob.myapplication.activities

import com.example.jacob.myapplication.FilteredFilePicker
import com.nononsenseapps.filepicker.AbstractFilePickerActivity
import com.nononsenseapps.filepicker.AbstractFilePickerFragment

import java.io.File

class MyPickerActivity : AbstractFilePickerActivity<File>() {

    override fun getFragment(
            startPath: String?, mode: Int, allowMultiple: Boolean,
            allowCreateDir: Boolean, allowExistingFile: Boolean,
            singleClick: Boolean): AbstractFilePickerFragment<File> {
        // Only the fragment in this line needs to be changed
        val fragment = FilteredFilePicker()
        fragment.setArgs(startPath, mode, allowMultiple, allowCreateDir, allowExistingFile, singleClick)
        return fragment
    }
}
