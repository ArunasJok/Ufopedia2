package ie.wit.ufopedia.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.ufopedia.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "ufos.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<UfoModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class UfoJSONStore(private val context: Context) : UfoStore {

    var ufos = mutableListOf<UfoModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<UfoModel> {
        logAll()
        return ufos
    }

    override fun create(ufo: UfoModel) {
        ufo.id = generateRandomId()
        ufos.add(ufo)
        serialize()
    }


    override fun update(ufo: UfoModel) {
        val ufosList = findAll() as ArrayList<UfoModel>
        var foundUfo: UfoModel? = ufosList.find { p -> p.id == ufo.id }
        if (foundUfo != null) {
            foundUfo.title = ufo.title
            foundUfo.description = ufo.description
            foundUfo.image = ufo.image
            foundUfo.lat = ufo.lat
            foundUfo.lng = ufo.lng
            foundUfo.zoom = ufo.zoom
            logAll()
        }
    }

    override fun delete(ufo: UfoModel) {
        ufos.remove(ufo)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(ufos, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        ufos = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        ufos.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}