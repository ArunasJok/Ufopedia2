package ie.wit.ufopedia.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.wit.ufopedia.helpers.readImageFromPath
import timber.log.Timber.i
import java.io.ByteArrayOutputStream
import java.io.File

class UfoFireStore(val context: Context) : UfoStore {
    val ufos = ArrayList<UfoModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override suspend fun findAll(): List<UfoModel> {
        return ufos
    }

    override suspend fun findById(id: Long): UfoModel? {
        val foundUfo: UfoModel? = ufos.find { p -> p.id == id }
        return foundUfo
    }

    override suspend fun create(ufo: UfoModel) {
        val key = db.child("users").child(userId).child("ufos").push().key
        key?.let {
            ufo.fbId = key
            ufos.add(ufo)
            db.child("users").child(userId).child("ufos").child(key).setValue(ufo)
            updateImage(ufo)
        }
    }

    override suspend fun update(ufo: UfoModel) {
        val foundUfo: UfoModel? = ufos.find { p -> p.fbId == ufo.fbId }
        if (foundUfo != null) {
            foundUfo.title = ufo.title
            foundUfo.description = ufo.description
            foundUfo.image = ufo.image
            foundUfo.location = ufo.location
        }

        db.child("users").child(userId).child("ufos").child(ufo.fbId).setValue(ufo)
        if(ufo.image.length > 0){
            updateImage(ufo)
        }
    }

    override suspend fun delete(ufo: UfoModel) {
        db.child("users").child(userId).child("ufos").child(ufo.fbId).removeValue()
        ufos.remove(ufo)

    }

    override suspend fun clear() {
        ufos.clear()
    }

    fun fetchUfos(ufosReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(ufos) {
                    it.getValue<UfoModel>(
                        UfoModel::class.java
                    )
                }
                ufosReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance().reference
        ufos.clear()
        db.child("users").child(userId).child("ufos")
            .addListenerForSingleValueEvent(valueEventListener)
    }
    fun updateImage(ufo: UfoModel){
        if(ufo.image != ""){
            val fileName = File(ufo.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, ufo.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        ufo.image = it.toString()
                        db.child("users").child(userId).child("ufos").child(ufo.fbId).setValue(ufo)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }

        }
    }
}