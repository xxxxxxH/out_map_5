package net.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import kotlinx.android.synthetic.main.fragment_map.*
import net.basicmodel.R
import net.event.MessageEvent
import net.utils.MapManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MapFragment : BaseFragment() {

    var map: GoogleMap? = null

    override fun getLayout(): Int {
        return R.layout.fragment_map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        mapview.onCreate(savedInstanceState)
        mapview.onResume()
        MapsInitializer.initialize(activity)
        initView()
    }

    private fun initView() {
        val code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)
        if (code == ConnectionResult.SUCCESS) {
            MapManager.getMapAsync(mapview, requireContext())
        } else {
            MapManager.showErrorDlg(code, requireActivity())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                map?.let {
                    MapManager.mapActivityResult(requireActivity(),data!!,it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0]) {
            "map" -> {
                map = msg[1] as GoogleMap
                map?.let {
                    MapManager.setMap(it)
                }
            }
            "location" -> {
                map?.let {
                    MapManager.setLocation(it, msg[1] as Double, msg[2] as Double)
                }
            }
        }
    }
}