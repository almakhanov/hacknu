package kz.validol.hacknu.entities

data class ChatObject(
    var text: String?,
    var author: User?,
    var recipient: User?,
    var sended_time: String?,
    var status: Int?,
    var book: Book?
)

//  0 on request
//  1 accepted
//  2 declined
//  3 author