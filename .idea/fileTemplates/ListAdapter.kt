#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}#end

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

#parse("File Header.java")
class ${NAME}(private val interaction: Interaction? = null) : 
  RecyclerView.Adapter<RecyclerView.ViewHolder>(){

  val DIFF_CALLBACK = object: DiffUtil.ItemCallback<${Model_Class}>(){

    override fun areItemsTheSame(oldItem: ${Model_Class}, newItem: ${Model_Class}): Boolean {
      TODO("not implemented")
    }

    override fun areContentsTheSame(oldItem: ${Model_Class}, newItem: ${Model_Class}): Boolean {
      TODO("not implemented")
    }

  }
  private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

      return ${ViewHolder_Class}(
          LayoutInflater.from(parent.context).inflate(
              R.layout.${Item_Layout_ID},
              parent,
              false
          ),
          interaction
      )
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      when(holder){
          is ${ViewHolder_Class} -> {
              holder.bind(differ.currentList.get(position))
          }
      }
  }

  override fun getItemCount(): Int {
      return differ.currentList.size
  }

  fun submitList(list: List<${Model_Class}>){
      differ.submitList(list)
  }

  class ${ViewHolder_Class}
  constructor(
      itemView: View,
      private val interaction: Interaction?
  ) : RecyclerView.ViewHolder(itemView){

      fun bind(item: ${Model_Class}) = with(itemView) {
          itemView.setOnClickListener {
              interaction?.onItemSelected(adapterPosition, item)
          }

          TODO("bind view with data")
      }
  }

  interface Interaction {
      fun onItemSelected(position: Int, item: ${Model_Class})
  }
}