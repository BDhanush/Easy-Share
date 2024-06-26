package com.example.easyshare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LINK = "link"

/**
 * A simple [Fragment] subclass.
 * Use the [qrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class qrFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            link = it.getString(LINK)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val qrgEncoder: QRGEncoder = QRGEncoder(link, null, QRGContents.Type.TEXT, dpWidth.toInt())
//        qrgEncoder.colorBlack = Color.RED
//        qrgEncoder.colorWhite = Color.BLUE

        // Getting QR-Code as Bitmap
        val bitmap = qrgEncoder.getBitmap(0)

        // Setting Bitmap to ImageView
        val qrImageView:ImageView = view.findViewById(R.id.qrImageView)
        qrImageView.setImageBitmap(bitmap)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment qrFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(link: String) =
            qrFragment().apply {
                arguments = Bundle().apply {
                    putString(LINK, link)
                }
            }
    }
}