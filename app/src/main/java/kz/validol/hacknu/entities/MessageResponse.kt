package kz.validol.hacknu.entities

data class MessageResponse(
    var code: Int?,
    var chat: List<ChatObject>?
)