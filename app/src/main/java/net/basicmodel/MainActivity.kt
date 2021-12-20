package net.basicmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.utils.MyFragmentManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyFragmentManager.get().showFragment(0, supportFragmentManager)
        val navigationController = tab.material()
            .addItem(R.mipmap.map, "Map")
            .addItem(R.mipmap.near, "Nearby")
            .addItem(R.mipmap.street, "StreetView")
            .addItem(R.mipmap.interactive, "Interactive")
            .build()
        navigationController.addSimpleTabItemSelectedListener { index, old ->
            MyFragmentManager.get().showFragment(index, supportFragmentManager)
        }
    }
}