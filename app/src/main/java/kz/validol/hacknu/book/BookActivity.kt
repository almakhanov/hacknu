package kz.validol.hacknu.book

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_book.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.entities.BookComment
import kz.validol.hacknu.entities.User
import kz.validol.hacknu.home.AllBooksAdapter
import org.koin.android.ext.android.inject
import java.net.URL


class BookActivity : AppCompatActivity(), ListCommentsAdapter.OnItemClickListener,
    ListReaderAdapter.OnItemClickListener, AllBooksAdapter.OnItemClickListener {

    companion object {
        var BOOK_ISNB = "BOOK_ISNB"
    }

    var book: Book? = null

    val api: Api by inject()

    var expandMode = false

    var text = ""
    var shortDesc = ""
    var recommendedAdapter: AllBooksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow()
            .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(kz.validol.hacknu.R.layout.activity_book)

        backBtn.setOnClickListener{
            onBackPressed()
        }

        intent.getStringExtra(BOOK_ISNB)?.let {isbn->
            api.getBookByISBN(isbn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    book = it.book
                    text = it.book?.description!!
                    setData()
                }, {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    it.printStackTrace()
                })
        }

        addComment.setOnClickListener{
            if(editComment.text.toString().isNotEmpty()){
                api.addComment(editComment.text.toString(), book?.id!!, App.user?.id!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        commentsAdapter.addComment(it.comment)
                        editComment.setText("")
                    },{
                        it.printStackTrace()
                    })
            }
        }

        recommendedAdapter = AllBooksAdapter(this, this)
        getRecommendations()
    }

    @SuppressLint("CheckResult")
    private fun getRecommendations() {
        recommendRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendRecycler.adapter = recommendedAdapter
        api.getReletedBooks(intent.getStringExtra(BOOK_ISNB)!!).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                recommendedAdapter?.set(it.books as ArrayList<Book>)
                recommendedAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

    override fun onBookItemClicked(item: Book) {
        val intt = Intent(this, BookActivity::class.java)
        intt.putExtra(BookActivity.BOOK_ISNB, item.isbn)
        startActivity(intt)
    }



    @SuppressLint("SetTextI18n")
    fun setData() {
        descText.setAnimationDuration(750L)
        descText.setInterpolator(OvershootInterpolator())
        descText.expandInterpolator = OvershootInterpolator()
        descText.collapseInterpolator = OvershootInterpolator()

        if (text.length < 180) {
            descText.text = text
        } else {
            shortDesc = text.substring(0, 200)
            descText.text = shortDesc
        }


        val options = RequestOptions()
            .centerCrop()
            .placeholder(kz.validol.hacknu.R.drawable.ic_color_lens_black_24dp)
            .error(kz.validol.hacknu.R.drawable.ic_color_lens_black_24dp)

        Glide.with(this)
            .load(book?.photo)
            .apply(options)
            .into(bookImg)

//        val url = URL(book?.photo)
//        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        Blurry.with(this).from(image).into(bookBg)
        runObs()

        bookTitle.text = book?.name
        bookGenre.text = book?.genre!![0].name
        rating.rating = book?.rating?:2.7F
        author.text = book?.author


        ////////////////////


        if(book?.reader == null){
            NOTtakenBtn.visibility = View.VISIBLE
            takenBtn.visibility = View.GONE

            if(book?.belong == null){
                NOTtakenBtn.text = "Free in Library"
                NOTtakenBtn.setOnClickListener{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Borrow Book")
                    builder.setMessage("Please, go to the Library and Scan the code of a book")

                    builder.setPositiveButton("OK") { dialog, which ->

                    }
                    builder.show()
                }
            }else if(book?.belong?.id == App.user?.id){
                NOTtakenBtn.text = "It's your book"
                NOTtakenBtn.setOnClickListener{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Borrow Book")
                    builder.setMessage("You are already the owner of the book. Read how much you want")

                    builder.setPositiveButton("OK") { dialog, which ->

                    }
                    builder.show()
                }
            }else{
                val strs = book?.belong?.name?.split(' ')
                NOTtakenBtn.text = "Free: ${strs!![0]}"

                NOTtakenBtn.setOnClickListener{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Borrow Book")
                    builder.setMessage("Do you want to borrow book from ${book?.belong?.name}?")

                    builder.setPositiveButton("YES") { dialog, which ->
                        api.requestOwner(App.user?.id, book?.isbn)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                NOTtakenBtn.text = "Requested!"
                            },{error->
                                error.printStackTrace()
                            })
                    }
                    builder.setNegativeButton("NO") { dialog, which -> }
                    builder.show()
                }
            }
        }else{
            NOTtakenBtn.visibility = View.GONE
            takenBtn.visibility = View.VISIBLE

            if(book?.reader?.id == App.user?.id){
                takenBtn.text = "You are reading"
            }else{
                val strs = book?.reader?.name?.split(' ')
                takenBtn.text = "${strs!![0]} is reading"

                takenBtn.setOnClickListener{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Borrow Book")
                    builder.setMessage("Do you want to borrow book from ${book?.reader?.name}?")

                    builder.setPositiveButton("YES") { dialog, which ->
                        api.requestOwner(App.user?.id, book?.isbn)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                takenBtn.text = "Requested!"
                            },{error->
                                error.printStackTrace()
                            })
                    }
                    builder.setNegativeButton("NO") { dialog, which -> }
                    builder.show()
                }
            }
        }





        ///////////////////////






        expand.setOnClickListener {
            if (descText.isExpanded) {
                descText.collapse()
                descText.text = text
                expand.text = "Read more"
                val img = ContextCompat.getDrawable(this, kz.validol.hacknu.R.drawable.ic_arrow_drop_down_black_24dp)
                img?.setBounds(0, 0, 60, 60)
                expand.setCompoundDrawables(null, null, img, null)
            } else {
                descText.expand()
                descText.text = text
                expand.text = "Less"
                val img = ContextCompat.getDrawable(this, kz.validol.hacknu.R.drawable.ic_arrow_drop_up_black_24dp)
                img?.setBounds(0, 0, 60, 60)
                expand.setCompoundDrawables(null, null, img!!, null)
            }

        }

        setComments(book?.comments as ArrayList<BookComment>)
        setHistoryUsers(book?.history as ArrayList<User>)
    }

    lateinit var historyAdapter: ListReaderAdapter
    private fun setHistoryUsers(userList: ArrayList<User>){
        if(userList.size == 0){
            listUsers.visibility = View.GONE
            readers.visibility = View.GONE
        }else{
            listUsers.visibility = View.VISIBLE
            readers.visibility = View.VISIBLE
        }

        historyAdapter = ListReaderAdapter(this, this)
        listUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        listUsers.adapter = historyAdapter
        userList.let{list->
            historyAdapter.set(list)
            historyAdapter.notifyDataSetChanged()
        }
    }

    override fun onUserReaderItemClicked(item: User) {
    }



    lateinit var commentsAdapter: ListCommentsAdapter
    private fun setComments(comments: ArrayList<BookComment>?){
        commentsAdapter = ListCommentsAdapter(this, this)
        listComments.adapter = commentsAdapter
        listComments.layoutManager = LinearLayoutManager(this)
        comments?.let{list->
            list.reverse()
            commentsAdapter.set(list)
            commentsAdapter.notifyDataSetChanged()
        }
    }

    override fun onBookCommentItemClicked(item: BookComment) {

    }


    @SuppressLint("CheckResult")
    private fun runObs(){
        Observable.fromCallable{
            val url = URL(book?.photo)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Blurry.with(this).from(it).into(bookBg)
            },{
                it.printStackTrace()
            })
    }




}
