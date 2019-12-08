package cn.msxf0.note;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import cn.leancloud.AVObject;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-12-4 上午8:59
 *  
 */
public class AdapterMarkdown1 extends RecyclerView.Adapter<AdapterMarkdown1.VH> {
    private ArrayList<AVObject> items;
    private Context context;

    public void setListener(CardViewItemListener listener) {
        this.listener = listener;
    }

    private CardViewItemListener listener;

    //    private View.OnClickListener listener;
    public AdapterMarkdown1(Context context, ArrayList<AVObject> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(View.inflate(this.context, R.layout.item_markdown, null));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        AVObject markdown = this.items.get(position);
//        Date date = markdown.getDate();
        holder.card_title.setText(markdown.getString("title"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        holder.card_time.setText((simpleDateFormat).format((Date) date));
        String background = markdown.getString("background");
        if (background == null) {
            Glide.with(context).load("http://image.tomatotime.cn:80/art_card" + (position % 30 + 1) + ".png").into(holder.card_backimg);
        } else {
        }
        holder.card_username.setText(markdown.getString("author") == null ? "匿名用户" : markdown.getString("author"));
        if (markdown.getString("face") == null) {
            Glide.with(context).load(R.drawable.nobody).into(holder.card_head);
        } else {
            Glide.with(context).load(markdown.getString("face")).into(holder.card_head);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (listener != null) {
                    listener.onCardViewItemClick(position);
                } else {
                }
            }
        });


        holder.markdownView.addStyleSheet(new Github());
        holder.markdownView.loadMarkdown(markdown.getString("content"));
//        holder.markdownView.loadMarkdownFromAsset("test.md");


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder implements AnimateViewHolder {
        private ImageView card_backimg;
        private ImageView card_head;
        private TextView card_time;
        private TextView card_title;
        private TextView card_username;
        private MarkdownView markdownView;
        private CardView card_view;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.card_view = (itemView.findViewById(R.id.card_view));
            this.card_title = (itemView.findViewById(R.id.card_title));
            this.card_time = (itemView.findViewById(R.id.card_time));
            this.card_head = (itemView.findViewById(R.id.card_head));
            this.card_backimg = (itemView.findViewById(R.id.card_backimg));
            this.card_username = (itemView.findViewById(R.id.card_username));
            this.markdownView = (itemView.findViewById(R.id.MDV));

        }

        @Override
        public void preAnimateAddImpl(RecyclerView.ViewHolder viewHolder) {

            ViewCompat.setTranslationY(this.itemView, -this.itemView.getHeight() * 0.3F);
            ViewCompat.setAlpha(this.itemView, 0.0F);
        }

        @Override
        public void preAnimateRemoveImpl(RecyclerView.ViewHolder viewHolder) {

        }

        @Override
        public void animateAddImpl(RecyclerView.ViewHolder viewHolder, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
            ViewCompat.animate(this.itemView).translationY(0.0F).alpha(1.0F).setDuration(500L).start();

        }

        @Override
        public void animateRemoveImpl(RecyclerView.ViewHolder viewHolder, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
            ViewCompat.animate(this.itemView).translationY(-this.itemView.getHeight() * 0.3F).alpha(0.0F).setDuration(500L).start();

        }
    }
}
