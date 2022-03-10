package mx.edu.itm.link.dadm_u3p4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import mx.edu.itm.link.dadm_u3p4.adapters.ContactsAdapter
import mx.edu.itm.link.dadm_u3p4.models.Contact
import mx.edu.itm.link.dadm_u3p4.models.DBManager

class MainActivity : AppCompatActivity() , interfaceView{

    lateinit var fabAgregar : FloatingActionButton
    lateinit var recyclerContactos : RecyclerView
    lateinit var editSearch : EditText
    lateinit var manager : DBManager
    lateinit var recyclerContactosAdapter: ContactsAdapter
    lateinit var contacts: ArrayList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAgregar = findViewById(R.id.fabAgregar)
        recyclerContactos = findViewById(R.id.recyclerContactos)
        editSearch = findViewById(R.id.editSearch)

        manager = DBManager(
            this,
            resources.getString(R.string.db_name),
            null,
            resources.getInteger(R.integer.db_version)
        )
        contacts = manager.find(editSearch.text)

        editSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    refreshContacts()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        fabAgregar.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
            refreshContacts()
        }

        refreshContacts()
    }

    private fun refreshContacts() {
        try {
            recyclerContactosAdapter=ContactsAdapter(this,R.layout.recylcer_row_contacts, contacts)
            recyclerContactos.adapter = recyclerContactosAdapter
            recyclerContactos.layoutManager = LinearLayoutManager(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun deletReady(id:String) {
        var deletedContact:Contact?=null
        contacts.map {
            if(it.id.toString()==id){
                deletedContact=it
            }
        }
        contacts.remove(deletedContact)
        recyclerContactosAdapter.notifyDataSetChanged()
    }

    override fun edited(id: String) {
        var editedContac:Contact?=null
        contacts.map {
            if(it.id.toString()==id){
                editedContac=it
            }
        }

    }


}