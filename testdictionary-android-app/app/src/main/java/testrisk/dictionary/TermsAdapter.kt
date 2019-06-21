package testrisk.dictionary

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_term.view.*


class TermsAdapter(private val terms: List<Term>): RecyclerView.Adapter<TermsAdapter.TermViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        return TermViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_term, parent, false)
        )
    }

    override fun getItemCount(): Int = terms.size

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val termsSorted = terms.sortedBy { term -> term.term }

        val term = termsSorted[position]

        holder.view.tvTerm.text = term.term
        holder.view.tvMeaning.text = term.meaning

        if(position % 2 == 0) {
            holder.view.oneLineTerm.setBackgroundColor(Color.parseColor("#F3F0EF"))
        } else {
            holder.view.oneLineTerm.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }



    }

    class TermViewHolder(val view: View): RecyclerView.ViewHolder(view)
}