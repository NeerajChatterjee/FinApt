package com.shrutislegion.finapt.Customer

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.shrutislegion.finapt.Modules.BillInfo
import com.shrutislegion.finapt.R


class PieChartActivity : AppCompatActivity() {
    lateinit var pieChart: PieChart
    lateinit var map: HashMap<String, Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        pieChart = findViewById<PieChart>(R.id.activity_main_piechart);
        setupPieChart();
        loadPieChartData();


    }

    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Spending by Category"
        pieChart.setCenterTextSize(24f)
        pieChart.description.isEnabled = false
        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = true
    }
    private fun loadPieChartData() {

        map = HashMap<String, Int>()
        val ref = FirebaseDatabase.getInstance().reference.child("ExpensesWithCategories").child(
            Firebase.auth.currentUser!!.uid)

        var overAllTotal = 0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //val value = snapshot.key
                    //Toast.makeText(view.context, value.toString(), Toast.LENGTH_SHORT).show()
                    for (dss in snapshot.children) {
                        val expense = dss.key
                        var total = 0
                        for (values in dss.children) {
                            val amount = (values.getValue<BillInfo>() as BillInfo).totalAmount.toInt()
                            total += amount
                        }
                        map[expense.toString()] = total
                        overAllTotal +=total
                    }
                    Toast.makeText(this@PieChartActivity, map.toString(), Toast.LENGTH_SHORT).show()
                    val keys = ArrayList<String>(map.keys)
                    //Toast.makeText(this, keys.count().toString(), Toast.LENGTH_SHORT).show()
                    val entries: ArrayList<PieEntry> = ArrayList()
                    for (value in 0 .. keys.size) {
                        if(value != keys.size) entries.add(PieEntry((map[keys[value]]!!.toFloat() / overAllTotal.toFloat()).toFloat(), keys[value]))
                    }
                    val colors: ArrayList<Int> = ArrayList()
                    for (color in ColorTemplate.MATERIAL_COLORS) {
                        colors.add(color)
                    }
                    for (color in ColorTemplate.VORDIPLOM_COLORS) {
                        colors.add(color)
                    }
                    val dataSet = PieDataSet(entries, "Expense Category")
                    dataSet.colors = colors
                    val data = PieData(dataSet)
                    data.setDrawValues(true)
                    data.setValueFormatter(PercentFormatter(pieChart))
                    data.setValueTextSize(12f)
                    data.setValueTextColor(Color.BLACK)
                    pieChart.setData(data)
                    pieChart.invalidate()
                    pieChart.animateY(1400, Easing.EaseInOutQuad)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("tag", error.message)
            }

        })


//        entries.add(PieEntry(0.2f,))
//        entries.add(PieEntry(0.15f, "Medical"))
//        entries.add(PieEntry(0.10f, "Entertainment"))
//        entries.add(PieEntry(0.25f, "Electricity and Gas"))
//        entries.add(PieEntry(0.3f, "Housing"))

    }
}