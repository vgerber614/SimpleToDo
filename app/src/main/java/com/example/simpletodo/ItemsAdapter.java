package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

// the items adapter puts data into the recycler view holder (view for each item/row)
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    // define interfaces for event listeners
    public interface OnLongClickListener {
        void onItemLongClicked(int position);   // sends item position on long click
    }

    public interface OnClickListener {
        void onItemClicked(int position);       // sends item position on item click
    }

    List<String> items;                         // member variable for todo items
    OnLongClickListener longClickListener;      // declare listener variables
    OnClickListener clickListener;

    /**
     * ItemsAdapter Constructor
     * Constructs the ItemsAdapter by defining the items model and event listeners
     *
     * @param items
     * @param longClickListener
     * @param clickListener
     */
    public ItemsAdapter(List<String> items,
                        OnLongClickListener longClickListener,
                        OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }


    // define the ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        /**
         * ViewHolder Constructor
         * Constructs the ViewHolder by defining the itemView
         *
         * @param itemView - view for single item (single row) in the Recycler View
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // get text view reference from builtin android resource layout
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        /**
         * bind
         * binds item to this instance of the holder,
         * setting the view attributes based on the item data (name of the todo item)
         *
         * @param item  - item as a string name
         */
        public void bind(String item) {
            tvItem.setText(item);
            // set event listeners for this view holder to send data for this item
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // send position of item to activity for deletion
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send position of item to activity for edit
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }


    /**
     * onCreateViewHolder
     * inflates (renders) layout from xml and creates a view holder
     *
     * @param parent
     * @param viewType
     * @return ViewHolder wrapping the view
     */
    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create or "inflate" a view from builtin android resource layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View todoView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        // wrap view inside holder and return
        return new ViewHolder(todoView);
    }


    /**
     * onBindViewHolder
     * sets view of item based on data by binding it to a holder
     *
     * @param holder    - holder to bind to the item
     * @param position  - position of the item to bind
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get item from data model based on position
        String item = items.get(position);

        // bind item into specified holder
        holder.bind(item);
    }


    /**
     * getItemCount
     * gets the number of items in the model/adapter
     *
     * @return
     */
    @Override
    public int getItemCount() { return items.size(); }

}
