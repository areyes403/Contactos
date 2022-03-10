package mx.edu.itm.link.dadm_u3p4.models

data class Contact(
    val id: Int,
    val name: String,
    val celphone: String,
    val favorite: Int?,
    val photo: ByteArray?
)