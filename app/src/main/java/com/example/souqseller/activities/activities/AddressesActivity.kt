package com.example.souqseller.activities.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.MyAddressesAdapter
import com.example.souqseller.activities.pojo.AddressDto
import com.example.souqseller.activities.viewModel.SellerProfileViewModel
import com.example.souqseller.databinding.ActivityAddressesBinding

class AddressesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressesBinding
    private lateinit var adapter: MyAddressesAdapter
    private lateinit var viewModel: SellerProfileViewModel

    private var addresses: List<AddressDto> = emptyList()
    private var storeId: Int = 0



    private val addAddressLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.getUserAddresses(storeId)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeId = getSharedPreferences("souq_prefs", MODE_PRIVATE)
            .getInt("SELLER_ID", 0)

        viewModel = ViewModelProvider(this)[SellerProfileViewModel::class.java]

        // ✅ Adapter
        adapter = MyAddressesAdapter(addresses) { address ->
            viewModel.setDefaultAddress(storeId, address.id)
        }

        binding.rvMyAddresses.layoutManager = LinearLayoutManager(this)
        binding.rvMyAddresses.adapter = adapter
        val swipeToDeleteCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val address = addresses[position]

                    showDeleteDialog(address)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val paint = Paint()

                    val density = recyclerView.context.resources.displayMetrics.density
                    val boxWidth = 70 * density      // نفس الكرت
                    val maxSwipe = 80 * density

                    if (dX > 0) {
                        paint.color = Color.parseColor("#D32F2F")

                        val left = itemView.left.toFloat()
                        val right = left + boxWidth
                        val top = itemView.top.toFloat()
                        val bottom = itemView.bottom.toFloat()

                        c.drawRect(left, top, right, bottom, paint)

                        val icon =
                            ContextCompat.getDrawable(this@AddressesActivity, R.drawable.delete)
                                ?: return

                        val iconWidth = icon.intrinsicWidth
                        val iconHeight = icon.intrinsicHeight

                        val iconLeft = (left + (boxWidth - iconWidth) / 2).toInt()
                        val iconTop = (top + (itemView.height - iconHeight) / 2).toInt()
                        val iconRight = iconLeft + iconWidth
                        val iconBottom = iconTop + iconHeight

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        icon.draw(c)
                    }

                    val clampedDX = dX.coerceIn(0f, maxSwipe)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        clampedDX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

        ItemTouchHelper(swipeToDeleteCallback)
            .attachToRecyclerView(binding.rvMyAddresses)


        observeAddresses()
        viewModel.getUserAddresses(storeId)

        binding.back.setOnClickListener { finish() }

        binding.addAddressBtn.setOnClickListener {
            val intent = Intent(this, AddNewAddressActivity::class.java)
            addAddressLauncher.launch(intent)
        }
    }

    private fun observeAddresses() {
        viewModel.observeAddresses().observe(this) { list ->
            addresses = list ?: emptyList()
            adapter.updateList(addresses)
        }
    }



    private fun showDeleteDialog(address: AddressDto) {
        AlertDialog.Builder(this)
            .setTitle("حذف العنوان")
            .setMessage("هل أنت متأكد أنك تريد حذف هذا العنوان؟")
            .setPositiveButton("حذف") { _, _ ->
                viewModel.deleteAddress(storeId, address.id)
            }
            .setNegativeButton("إلغاء") { _, _ ->
                adapter.notifyDataSetChanged() // 🔥 رجّع العنصر
            }
            .setOnCancelListener {
                adapter.notifyDataSetChanged()
            }
            .show()
    }

}
