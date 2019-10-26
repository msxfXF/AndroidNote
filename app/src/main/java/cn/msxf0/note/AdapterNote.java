package cn.msxf0.note;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-10-26 上午10:30
 *  
 */
public class AdapterNote extends RecyclerView.Adapter<AdapterNote.VH> {
    private ArrayList<Note> items;
    private Context context;

    //    private View.OnClickListener listener;
    public AdapterNote(Context context, ArrayList<Note> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(View.inflate(this.context, R.layout.item_note, null));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        Note note = this.items.get(position);
        Date date = note.getDate();
        holder.card_title.setText(note.getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.card_time.setText((simpleDateFormat).format((Date) date));
        if (note.getBackground() == "") {
            Glide.with(context).load("http://image.tomatotime.cn:80/art_card" + (position % 30 + 1) + ".png").into(holder.card_backimg);
        } else {
        }
        holder.card_username.setText(note.getAuthor());
        if (note.getAuthor_face() == "") {
            Glide.with(context).load(R.drawable.nobody).into(holder.card_head);
        } else {
            Glide.with(context).load(note.getAuthor_face()).into(holder.card_head);
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View paramAnonymousView)
//            {
//                if (listener != null) {
//                    listener.onClick();
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView card_backimg;
        private ImageView card_head;
        private TextView card_time;
        private TextView card_title;
        private TextView card_username;
        private CardView card_view;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.card_view = (itemView.findViewById(R.id.card_view));
            this.card_title = (itemView.findViewById(R.id.card_title));
            this.card_time = (itemView.findViewById(R.id.card_time));
            this.card_head = (itemView.findViewById(R.id.card_head));
            this.card_backimg = (itemView.findViewById(R.id.card_backimg));
            this.card_username = (itemView.findViewById(R.id.card_username));
        }
    }
}
