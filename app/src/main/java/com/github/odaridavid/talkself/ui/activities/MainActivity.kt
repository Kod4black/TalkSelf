package com.github.odaridavid.talkself.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.ui.fragments.edituser.EditUserFragment
import com.github.odaridavid.talkself.utils.Coroutines
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
//        var images = mutableListOf<String>()
//
//        storageRef.listAll().addOnSuccessListener { listResult ->
//            for (file in listResult.items) {
//                file.downloadUrl.addOnSuccessListener { uri -> // adding the url in the arraylist
//                    Log.e("Itemvalue", uri.toString())
//                }.addOnSuccessListener {
//                    Log.i("image",it.toString())
//                    images.add(it.toString())
//                }
//            }
//
//
//
//        }.addOnCompleteListener {
//            Coroutines.io {
//                delay(7000)
//
//            }
//        }

    }
}


