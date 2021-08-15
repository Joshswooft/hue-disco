package com.example.huedisco

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.huedisco.databinding.ActivityMainBinding
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val PARTIAl_IP_ADDRESS: Pattern = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}" +
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val ipAddressTextWatcher = RegexMaskTextWatcher(PARTIAl_IP_ADDRESS.pattern());

        binding.editIpAddress.addTextChangedListener(ipAddressTextWatcher)

        binding.connectToBridge.setOnClickListener{connectToBridge()}
    }

    private fun connectToBridge() {

//        validate IP: Patterns.IP_ADDRESS.matcher(url).matches();
        val ip = binding.editIpAddress.text
        Log.d("Ip address: ", ip.toString())
//        TODO: connect to bridge
//        TODO: if successful then go to next page
    }



}