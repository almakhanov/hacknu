package kz.validol.hacknu.book


import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Window
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_book.*
import kz.validol.hacknu.R

class BookActivity : AppCompatActivity() {

    var expandMode = false

    val text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
    var shortDesc = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        descText.setAnimationDuration(750L)
        descText.setInterpolator(OvershootInterpolator())
        descText.expandInterpolator = OvershootInterpolator()
        descText.collapseInterpolator = OvershootInterpolator()
        if (text.length<180){
            descText.text = text
        }else{
            shortDesc = text.substring(0,200)
        }
        Blurry.with(this).from(BitmapFactory.decodeResource(resources,R.drawable.book_tmp)).into(bookBg)
        setData()
    }

    fun setData(){
        listUsers.adapter = ListReaderAdapter()
        listUsers.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
        expand.setOnClickListener {
            if (descText.isExpanded){
                descText.collapse()
                descText.text = text
                expand.text = "Read more"
                val img = ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_down_black_24dp)
                img?.setBounds(0,0,60,60)
                expand.setCompoundDrawables(null,null,img,null)
            }else{
                descText.expand()
                descText.text = text
                expand.text = "Less"
                val img = ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_up_black_24dp)
                img?.setBounds(0,0,60,60)
                expand.setCompoundDrawables(null,null,img!!,null)
            }

        }

        listComments.adapter = ListCommentsAdapter()
        listComments.layoutManager = LinearLayoutManager(this)
    }


}
