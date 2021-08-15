package com.example.huedisco

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import java.util.regex.Pattern

/**
 * Add this to (e.g.) an EditText via addTextChangedListener() to prevent any user input
 * that doesn't match its supplied regex.
 *
 * Inspired by original Java code here: http://stackoverflow.com/a/11545229/1405990
 */
class RegexMaskTextWatcher(regexForInputToMatch : String) : TextWatcher {

    private val regex = Pattern.compile(regexForInputToMatch)
    private var previousText: String = ""

    override fun afterTextChanged(s: Editable) {
        if (regex.matcher(s).matches()) {
            previousText = s.toString();
        } else {
            Log.d("invalid ip address: ", previousText)
            s.replace(0, s.length, previousText);
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

}