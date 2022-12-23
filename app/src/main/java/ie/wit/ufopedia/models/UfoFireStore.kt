package ie.wit.ufopedia.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UfoFireStore(val context: Context) : UfoStore {
    val ufos = ArrayList<UfoModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

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
        }
    }

    override suspend fun update(ufo: UfoModel) {
        var foundUfo: UfoModel? = ufos.find { p -> p.fbId == ufo.fbId }
        if (foundUfo != null) {
            foundUfo.title = ufo.title
            foundUfo.description = ufo.description
            foundUfo.image = ufo.image
            foundUfo.location = ufo.location
        }

        db.child("users").child(userId).child("ufos").child(ufo.fbId).setValue(ufo)

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
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(ufos) {
                    it.getValue<UfoModel>(
                        UfoModel::class.java
                    )
                }
                ufosReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        ufos.clear()
        db.child("users").child(userId).child("ufos")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}