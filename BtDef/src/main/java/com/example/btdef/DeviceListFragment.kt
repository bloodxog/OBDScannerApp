package com.example.btdef

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btdef.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar

class DeviceListFragment : Fragment() {
    private lateinit var itemAdapter: ItemAdapter
    private var bAdapter: BluetoothAdapter? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var btLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBtLauncher()
        binding.imBluetoothOn.setOnClickListener {
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
        initRcViews()
        initBtAdapter()
        bluetoothState()
    }

    private fun initRcViews() = with(binding) {
        rcViewPaired.layoutManager = LinearLayoutManager(requireContext())
        itemAdapter = ItemAdapter()
        rcViewPaired.adapter = itemAdapter
    }

    private fun getPairedDevices(){
        try {
            val list = ArrayList<ListItem>()
            val deviceList = bAdapter?.bondedDevices as Set<BluetoothDevice>
            deviceList.forEach{
                list.add(
                    ListItem(
                        it.name,
                        it.address
                    )
                )
            }
            binding.tvEmptyPaired.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
            itemAdapter.submitList(list)
        } catch (e: SecurityException){

        }
    }

    private fun initBtAdapter(){
        val bManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }
    private fun bluetoothState(){
        if (bAdapter?.isEnabled == true) {
            changeButtonColor(binding.imBluetoothOn, Color.GREEN)
                getPairedDevices()
        }
    }

    private fun registerBtLauncher(){
        btLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                changeButtonColor(binding.imBluetoothOn, Color.GREEN)
                getPairedDevices()
                Snackbar.make(binding.root, "Bluetooth включен!", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(binding.root, "Необходимо включить Bluetooth!", Snackbar.LENGTH_LONG).show()
            }

        }
    }
}