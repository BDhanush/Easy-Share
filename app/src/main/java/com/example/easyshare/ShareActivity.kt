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

    private lateinit var title: String
    private lateinit var linkString: String
    private var id:Long = 0

    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        binding = ActivityShareBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        title=intent.getStringExtra("title").toString()
        linkString=intent.getStringExtra("linkString").toString()
        id=intent.getLongExtra("id",0)

        setInfo(title,linkString)

        binding.nfcToggle.setOnClickListener {
            setNFCFragment()
        }
        binding.qrToggle.setOnClickListener {
            setQRFragment()
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

    private fun setInfo(newTitle:String, newLinkString: String)
    {
        title = newTitle
        linkString = newLinkString
        setTitle(title)

        val nfcFragment = supportFragmentManager.findFragmentByTag("nfcFragment")
        val qrFragment = supportFragmentManager.findFragmentByTag("qrFragment")

        val hasNFC=supportNfcHceFeature()
        if(!hasNFC)
        {
            binding.nfcToggle.isEnabled=false
        }

        if(!hasNFC || (qrFragment!=null && qrFragment.isVisible))
        {
            setQRFragment()
        }else{
            setNFCFragment()
        }
    }

    private fun setQRFragment()
    {
        binding.toggleButtons.check(R.id.qrToggle)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, qrFragment.newInstance(linkString),"qrFragment")
        transaction.commit()
    }

    private fun setNFCFragment()
    {
        binding.toggleButtons.check(R.id.nfcToggle)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, nfcFragment.newInstance(linkString),"nfcFragment")
        transaction.commit()
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