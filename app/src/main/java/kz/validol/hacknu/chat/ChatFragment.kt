package kz.validol.hacknu.chat


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chat.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.ChatObject
import org.koin.android.ext.android.inject

class ChatFragment : Fragment(), ChatAdapter.OnItemClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)

    }

    val api: Api by inject()

    var chatAdapter: ChatAdapter? = null
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter(context!!, this)
        requestList.adapter = chatAdapter
        requestList.layoutManager = LinearLayoutManager(context!!)

        api.getChatObjects(App.user?.id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                chatAdapter?.set(it.chat as ArrayList<ChatObject>)
                chatAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

    @SuppressLint("CheckResult")
    override fun onRequsetAccept(item: ChatObject, position: Int) {
        api.acceptRequest(App.user?.id, item.book?.isbn).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

    @SuppressLint("CheckResult")
    override fun onRequestDecline(item: ChatObject, position: Int) {
        api.declineRequest(App.user?.id, item.book?.isbn).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }



}
