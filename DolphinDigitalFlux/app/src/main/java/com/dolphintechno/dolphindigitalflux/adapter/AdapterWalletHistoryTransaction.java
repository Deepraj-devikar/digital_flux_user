package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.WalletHistoryTransaction;

import java.util.List;

public class AdapterWalletHistoryTransaction extends RecyclerView.Adapter<AdapterWalletHistoryTransaction.WalletHistoryTransactionViewHolder> {

    SortedList<WalletHistoryTransaction> walletHistoryTransactionSortedList;

    Context context;


    public AdapterWalletHistoryTransaction(Context context) {


        this.context = context;

        walletHistoryTransactionSortedList = new SortedList<WalletHistoryTransaction>(WalletHistoryTransaction.class, new SortedList.Callback<WalletHistoryTransaction>() {
            @Override
            public int compare(WalletHistoryTransaction walletHistoryTransaction, WalletHistoryTransaction t21) {
                return t21.getDateTime().compareTo(walletHistoryTransaction.getDateTime());
            }

            @Override
            public void onChanged(int i, int i1) {

                notifyItemRangeChanged(i, i1);

            }

            @Override
            public boolean areContentsTheSame(WalletHistoryTransaction walletHistoryTransaction, WalletHistoryTransaction t21) {
                return walletHistoryTransaction.getDateTime().equals(t21.getDateTime());
            }

            @Override
            public boolean areItemsTheSame(WalletHistoryTransaction walletHistoryTransaction, WalletHistoryTransaction t21) {
                return walletHistoryTransaction.getDateTime().equals(t21.getDateTime());
            }

            @Override
            public void onInserted(int i, int i1) {

                notifyItemRangeInserted(i, i1);

            }

            @Override
            public void onRemoved(int i, int i1) {

                notifyItemRangeRemoved(i, i1);

            }

            @Override
            public void onMoved(int i, int i1) {

                notifyItemMoved(i, i1);

            }
        });

    }


    public void addAll(List<WalletHistoryTransaction> walletHistoryTransactionList){

//        Toast.makeText(context, "--"+walletHistoryTransactionList.size()+"--", Toast.LENGTH_LONG).show();

        walletHistoryTransactionSortedList.beginBatchedUpdates();
        for (int i = 0; i < walletHistoryTransactionList.size(); i++){
            walletHistoryTransactionSortedList.add(walletHistoryTransactionList.get(i));

        }
        walletHistoryTransactionSortedList.endBatchedUpdates();
    }

    public WalletHistoryTransaction get(int position){
        return walletHistoryTransactionSortedList.get(position);
    }

    public void clear(){
        walletHistoryTransactionSortedList.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while(walletHistoryTransactionSortedList.size() > 0){
            walletHistoryTransactionSortedList.removeItemAt(walletHistoryTransactionSortedList.size() - 1);
        }
        walletHistoryTransactionSortedList.endBatchedUpdates();
    }

    @NonNull
    @Override
    public WalletHistoryTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wallet_history, viewGroup, false);

        return new WalletHistoryTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletHistoryTransactionViewHolder walletHistoryTransactionViewHolder, int i) {

        WalletHistoryTransaction walletHistoryTransaction = walletHistoryTransactionSortedList.get(i);




        walletHistoryTransactionViewHolder.tv_item_wallet_history_transaction.setText(walletHistoryTransaction.getTransaction());
        walletHistoryTransactionViewHolder.tv_item_wallet_history_reason.setText(walletHistoryTransaction.getReason());


        walletHistoryTransactionViewHolder.tv_item_wallet_history_date_time.setText(walletHistoryTransaction.getDateTime());
        walletHistoryTransactionViewHolder.tv_item_wallet_history_balance.setText(String.valueOf(walletHistoryTransaction.getBalance()));
        walletHistoryTransactionViewHolder.tv_item_wallet_history_amount.setText(String.valueOf(walletHistoryTransaction.getAmount()));

    }

    @Override
    public int getItemCount() {
        return walletHistoryTransactionSortedList.size();
    }

    public class WalletHistoryTransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item_wallet_history_transaction, tv_item_wallet_history_reason, tv_item_wallet_history_date_time, tv_item_wallet_history_balance, tv_item_wallet_history_amount;

        public WalletHistoryTransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_item_wallet_history_transaction = (TextView) itemView.findViewById(R.id.tv_item_wallet_history_transaction);
            tv_item_wallet_history_reason = (TextView) itemView.findViewById(R.id.tv_item_wallet_history_reason);
            tv_item_wallet_history_date_time = (TextView) itemView.findViewById(R.id.tv_item_wallet_history_date_time);
            tv_item_wallet_history_balance = (TextView)itemView.findViewById(R.id.tv_item_wallet_history_balance);
            tv_item_wallet_history_amount = (TextView) itemView.findViewById(R.id.tv_item_wallet_history_amount);

        }
    }
}
