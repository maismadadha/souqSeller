package com.example.souqseller.activities.activities

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.souqseller.R
import com.example.souqseller.activities.adapters.MyAddressesAdapter
import com.example.souqseller.activities.adapters.MyCategoriesAdapter
import com.example.souqseller.activities.pojo.AddressDto
import com.example.souqseller.activities.pojo.StoreCategoriesItem
import com.example.souqseller.activities.viewModel.SellerProfileViewModel
import com.example.souqseller.databinding.ActivityStoreCategoriesBinding

class StoreCategoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreCategoriesBinding
    private lateinit var adapter: MyCategoriesAdapter
    private lateinit var viewModel: SellerProfileViewModel

    private var categories: List<AddressDto> = emptyList()
    private var storeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoreCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeId = getSharedPreferences("souq_prefs", MODE_PRIVATE)
            .getInt("SELLER_ID", 0)

        viewModel = ViewModelProvider(this)[SellerProfileViewModel::class.java]

        adapter = MyCategoriesAdapter(emptyList())

        binding.rvMyAddresses.layoutManager = LinearLayoutManager(this)
        binding.rvMyAddresses.adapter = adapter


        viewModel.observeStoreCategories().observe(this) { list ->
            adapter.updateList(list ?: emptyList())
        }

        viewModel.getStoreCategories(storeId)
        val swipeToDeleteCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val category = adapter.getItemAt(position)
                    showDeleteDialog(category)
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
                    val boxWidth = 70 * density
                    val maxSwipe = 80 * density

                    if (dX > 0) {
                        paint.color = Color.parseColor("#D32F2F")

                        val left = itemView.left.toFloat()
                        val right = left + boxWidth
                        val top = itemView.top.toFloat()
                        val bottom = itemView.bottom.toFloat()

                        c.drawRect(left, top, right, bottom, paint)

                        val icon = ContextCompat.getDrawable(
                            this@StoreCategoriesActivity,
                            R.drawable.delete
                        ) ?: return

                        val iconLeft = (left + (boxWidth - icon.intrinsicWidth) / 2).toInt()
                        val iconTop =
                            (top + (itemView.height - icon.intrinsicHeight) / 2).toInt()

                        icon.setBounds(
                            iconLeft,
                            iconTop,
                            iconLeft + icon.intrinsicWidth,
                            iconTop + icon.intrinsicHeight
                        )
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



        binding.back.setOnClickListener { finish() }

    }

    private fun showDeleteDialog(category: StoreCategoriesItem) {
        AlertDialog.Builder(this)
            .setTitle("حذف الفئة")
            .setMessage("هل أنت متأكد أنك تريد حذف فئة \"${category.name}\" ؟")
            .setPositiveButton("حذف") { _, _ ->
                viewModel.deleteStoreCategory(category.id, storeId)
            }
            .setNegativeButton("إلغاء") { dialog, _ ->
                dialog.dismiss()
                adapter.notifyDataSetChanged()
            }
            .show()
    }

}