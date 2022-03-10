package mx.edu.itm.link.dadm_u3p4.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Editable
import android.util.Log
import androidx.core.database.getBlobOrNull
import androidx.core.database.getIntOrNull
import mx.edu.itm.link.dadm_u3p4.interfaceView
import kotlin.jvm.Throws


class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    val viewVista=context

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = ""

        db?.let {
            sql = """
                CREATE TABLE contacts(
                    id INTEGER PRIMARY KEY NOT NULL,
                    name TEXT NOT NULL,
                    celphone TEXT NOT NULL,
                    favorite INT,
                    photo BLOB
                )
            """
            it.execSQL(sql)

            sql = "INSERT INTO contacts(name,celphone) VALUES('Yo','4433000000')"

            it.execSQL(sql)
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    @Throws
    fun find(name : Editable? = null) : ArrayList<Contact> {
        val db = readableDatabase

        var sql = "SELECT * FROM contacts"
        name?.let {
            if(it.isNotEmpty()) {
                sql += " WHERE name LIKE '%$it%'"
            }
        }

        val contacts = ArrayList<Contact>()

        val cursor = db.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val contact = Contact(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getIntOrNull(3),
                    cursor.getBlobOrNull(4)
            )

            contacts.add(contact)
        }
        db.close()

        return contacts
    }

    @Throws
    fun create(contact: Contact) {
        val db = writableDatabase

        var sql = ""
        if(contact.photo != null) {
            sql = "INSERT INTO contacts(name,celphone,favorite,photo) VALUES(?,?,?,?)"
        } else {
            sql = "INSERT INTO contacts(name,celphone,favorite) VALUES(?,?,?)"
        }
        val compileStm = db.compileStatement(sql)
        compileStm.bindString(1, contact.name)
        compileStm.bindString(2, contact.celphone)

        contact.favorite?.let{
            compileStm.bindLong(3, it.toLong())
        } ?: compileStm.bindNull(3)

        contact.photo?.let { compileStm.bindBlob(4, it) }

        compileStm.execute()
        val id=contact.id.toString()

        db.close()
    }

    @Throws
    fun delete(id:String){
        val db=writableDatabase
        val delete=
            db.delete("contacts","id="+id,null)

        if (delete >= 1) {
            (viewVista as interfaceView).deletReady(id)
            Log.v("@@@WWe", " Record deleted")
        } else {
            Log.v("@@@WWe", " Not deleted")
        }
        db.close()

    }

    @Throws
    fun update(contact: Contact){
        val db = this.writableDatabase
        val values = ContentValues()
        var id= contact.id
        var name=contact.name
        var celphone=contact.celphone
        var photo= null
        var favorite= contact.favorite
        values.put("name",name)
        values.put("celphone",celphone)
        values.put("favorite",favorite)

        if(contact.photo != null) {
            db.update("contacts",values,"id="+id,null)

        } else {
            db.update("contacts",values,"id="+id,null)
        }

        db.close()
    }

}