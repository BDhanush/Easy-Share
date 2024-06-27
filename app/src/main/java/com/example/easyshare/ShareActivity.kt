package com.example.easyshare

import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.easyshare.adapter.addLink
import com.example.easyshare.databinding.ActivityShareBinding
import com.example.easyshare.databinding.AddLinkBinding
import com.example.easyshare.model.Link
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ShareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareBinding

    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        binding = ActivityShareBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        val title=intent.getStringExtra("title").toString()
        val linkString=intent.getStringExtra("linkString").toString()
        val id=intent.getLongExtra("id",0)

        setInfo(title,linkString)

        binding.nfcToggle.setOnClickListener {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, nfcFragment.newInstance(linkString),"nfcFragment")
            transaction.commit()
        }
        binding.qrToggle.setOnClickListener {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, qrFragment.newInstance(linkString),"qrFragment")
            transaction.commit()
        }

        binding.editButton.setOnClickListener {
            val addLinkBinding = AddLinkBinding.inflate(layoutInflater)
            val addLinkView = addLinkBinding.root
            addLinkBinding.title.setText(title)
            addLinkBinding.link.setText(linkString)
            val alertDialog = MaterialAlertDialogBuilder(this)
                .setTitle("Edit Link")
                .setView(addLinkView)
                .setPositiveButton("Edit") { dialog, which ->
                    val link:Link = Link(addLinkBinding.title.text.toString(),addLinkBinding.link.text.toString(),id)
                    if(addLink(link,this))
                        setInfo(link.title,link.linkString)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        }

    }

    fun setInfo(title:String,linkString: String)
    {
        setTitle(title)
        val nfcFragment = supportFragmentManager.findFragmentByTag("nfcFragment")
        val qrFragment = supportFragmentManager.findFragmentByTag("qrFragment")

        val hasNFC=supportNfcHceFeature()
        if(!hasNFC)
        {
            binding.nfcToggle.isEnabled=false
            binding.toggleButtons.check(R.id.qrToggle)
        }

        if(!hasNFC || (qrFragment!=null && qrFragment.isVisible))
        {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, com.example.easyshare.qrFragment.newInstance(linkString),"qrFragment")
            transaction.commit()
        }else{
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, com.example.easyshare.nfcFragment.newInstance(linkString),"nfcFragment")
            transaction.commit()
        }
    }

    private fun supportNfcHceFeature() =
        checkNFCEnable() && packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)

    private fun checkNFCEnable(): Boolean {
        return if (mNfcAdapter == null) {
            false
        } else {
            mNfcAdapter?.isEnabled == true
        }
    }
}