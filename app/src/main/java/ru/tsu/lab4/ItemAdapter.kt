package ru.tsu.lab4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_spaceships.view.*

data class Item(
    val title: String,
    @DrawableRes val icon: Int
)

class ItemAdapter(val itemClick: (position: Int,item: Item) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    private var items: List<Item> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =  // возвращает объект ViewHolder, который будет хранить данные по одному объекту Phone
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_spaceships, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {  // выполняет привязку объекта ViewHolder к объекту Phone по определенной позиции
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick(position, items[position])
        }
    }

    override fun getItemCount() = items.size  // возвращает количество объектов в списке

    fun setItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged() // уведомляет список об изменении данных для обновления списка на экране
    }
}

class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
        view.listItemText.text = item.title
        view.listItemIcon.setImageResource(item.icon)
    }
}