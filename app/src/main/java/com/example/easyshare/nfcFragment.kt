package com.example.easyshare

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easyshare.customviews.EmittingCirclesView
import com.example.easyshare.nfc.MyHostApduService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LINK = "link"

/**
 * A simple [Fragment] subclass.
 * Use the [nfcFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class nfcFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            link = it.getString(LINK)
        }
        val intent = Intent(context,MyHostApduService::class.java)
        intent.putExtra("linkString", link);
        requireActivity().startService(intent)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emittingCirclesView:EmittingCirclesView = view.findViewById(R.id.emittingCirclesView)
        emittingCirclesView.startAnimation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nfc, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment nfcFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(link: String) =
            nfcFragment().apply {
                arguments = Bundle().apply {
                    putString(LINK, link)
                }
            }
    }
}