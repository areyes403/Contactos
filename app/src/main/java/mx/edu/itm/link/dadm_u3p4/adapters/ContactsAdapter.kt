package mx.edu.itm.link.dadm_u3p4.adapters

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.view.DragAndDropPermissionsCompat.request
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import mx.edu.itm.link.dadm_u3p4.ContactActivity
import mx.edu.itm.link.dadm_u3p4.R
import mx.edu.itm.link.dadm_u3p4.models.Contact
import mx.edu.itm.link.dadm_u3p4.models.DBManager


class ContactsAdapter(var context:Context, val res: Int, val contacts:ArrayList<Contact>)
    : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(res, null)
        var holder=ContactsViewHolder(view)
        holder.bind()
        return holder
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        holder.textName?.text= contacts.get(position).name
        holder.textCel?.text=contacts.get(position).celphone
        contact.favorite?.let {imgFav->
            holder.imgFav?.setImageResource(
                if(imgFav == 1) android.R.drawable.star_big_on
                else android.R.drawable.star_big_off
            )

        }

        contact.photo?.let { imgPhoto->
            val bmp = BitmapFactory.decodeByteArray(imgPhoto, 0, imgPhoto.size)
            holder.imgPhoto?.setImageBitmap(
                Bitmap.createScaledBitmap(bmp, bmp.width, bmp.height, false)
            )

        }

        holder.fabOptions!!.setOnClickListener {
            holder.fabEdit?.visibility = if(holder.fabEdit?.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            holder.fabEdit?.setOnClickListener {
               val b = Bundle()
                b.putInt("id", contact.id)
                b.putString("name",contact.name)
                b.putString("celphone",contact.celphone)
                val intent = Intent(context, ContactActivity::class.java)
                intent.putExtras(b)
                context.startActivity(intent)

            }

            holder.fabDelete?.visibility = if(holder.fabDelete?.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            holder.fabDelete?.setOnClickListener{
                var manager = DBManager(
                    context,
                    "BD_ContactsApp",
                    null,
                    1
                )
                var id=contact.id.toString()
                manager.delete(id)
                Snackbar.make(it, "Contacto Eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()

            }
            holder.fabCall?.visibility = if(holder.fabCall?.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            holder.fabCall?.setOnClickListener{


                try {

                    val numero=contact.celphone.toString()
                    val i = Intent(Intent.ACTION_CALL)
                    i.data = Uri.parse("tel:"+numero)
                    context.startActivity(i)
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            holder.fabMsg?.visibility = if(holder.fabMsg?.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            holder.fabMsg?.setOnClickListener {

                val telefono=contact.celphone
                val sms_uri = Uri.parse("smsto:{$telefono}")
                val sms_intent = Intent(Intent.ACTION_SENDTO, sms_uri)
                sms_intent.putExtra("sms_body","" )
                context.startActivity(sms_intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgPhoto : ImageView?=null
        var textName :TextView?=null
        var textCel : TextView?=null
        var imgFav :ImageView?=null
        var fabOptions : FloatingActionButton?=null
        var fabEdit : FloatingActionButton?=null
        var fabDelete : FloatingActionButton?=null
        var fabCall : FloatingActionButton?=null
        var fabMsg : FloatingActionButton?=null


        fun bind() {
            imgPhoto = itemView.findViewById(R.id.imgRowPhoto) as ImageView
            textName = itemView.findViewById(R.id.textRowName) as TextView
            textCel = itemView.findViewById(R.id.textRowCel) as TextView
            imgFav = itemView.findViewById(R.id.imgRowFav) as ImageView
            fabOptions = itemView.findViewById(R.id.fabRowOptions) as FloatingActionButton
            fabEdit = itemView.findViewById(R.id.fabRowEdit) as FloatingActionButton
            fabDelete = itemView.findViewById(R.id.fabRowDelete) as FloatingActionButton
            fabCall = itemView.findViewById(R.id.fabRowCall) as FloatingActionButton
            fabMsg = itemView.findViewById(R.id.fabRowMsg) as FloatingActionButton

       }

    }

}