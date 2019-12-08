package cn.msxf0.note;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.livequery.AVLiveQuery;
import cn.leancloud.livequery.AVLiveQueryEventHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservableCache;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;


/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-10-25 下午3:55
 *  
 */
public class FragmentNote extends Fragment implements CardViewItemListener, Observer<AVObject> {
    private Context context;
    private RecyclerView recyclerView;
    private List<AVObject> items;
    private SlideInRightAnimationAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private ArrayList a;
    private StoreHouseHeader storeHouseHeader;
    private MaterialDialog dialog;
    private View dialogView;
    private TextView dialogTitle;
    private EditText dialogEditContent;
    private EditText dialogEditTitle;
    private Button dialogBtnY;
    private Button dialogBtnDel;


    public static FragmentNote newInstance() {
        return new FragmentNote();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        ptrFrameLayout = view.findViewById(R.id.store_house_ptr_frame);
        initLogo();
//        for (int i = 0; i < 30; i++) {
//            items.add(new Note("title" + i, "test content"));
//        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
//        AdapterNote adapter0 = new AdapterNote(getContext(), items);
        items = new ArrayList();
        AdapterNote1 adapter0 = new AdapterNote1(getContext(), items);
        adapter0.setListener(FragmentNote.this);
        adapter = new SlideInRightAnimationAdapter(adapter0);
        adapter.setFirstOnly(false);
        adapter.setDuration(800);
        recyclerView.setAdapter(this.adapter);
        dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_note, false)
                .backgroundColor(0)
                .canceledOnTouchOutside(true)
                .build();

        dialogView = dialog.getCustomView();
        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogEditTitle = dialogView.findViewById(R.id.dialog_edit_title);
        dialogEditContent = dialogView.findViewById(R.id.dialog_edit_content);
        dialogBtnY = dialogView.findViewById(R.id.dialog_btn_y);
        dialogBtnDel = dialogView.findViewById(R.id.dialog_btn_del);


    }

    private void updateItems(final PtrFrameLayout ptr) {
        int a = items.size();
        items.clear();
        adapter.notifyDataSetChanged();
        adapter.notifyItemRangeRemoved(0, a);

        AVQuery<AVObject> query = new AVQuery<>("Notes");
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<AVObject> avObjects) {
                items.addAll(avObjects);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                adapter.notifyItemRangeChanged(0, FragmentNote.this.items.size());
                adapter.notifyDataSetChanged();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ptr != null) ptr.refreshComplete();

                    }
                });
            }
        });
    }


    private void initLogo() {
        initLogoData();
        storeHouseHeader = new StoreHouseHeader(getContext());
        storeHouseHeader.setPadding(0, PtrLocalDisplay.dp2px(20), 0, 0);
        storeHouseHeader.initWithPointList(a);
        this.ptrFrameLayout.setDurationToCloseHeader(1500);
        this.ptrFrameLayout.setHeaderView(storeHouseHeader);
        this.ptrFrameLayout.addPtrUIHandler(storeHouseHeader);
        this.ptrFrameLayout.postDelayed(new Runnable() {
            public void run() {
                FragmentNote.this.ptrFrameLayout.autoRefresh(false);
            }
        }, 100L);
        this.ptrFrameLayout.addPtrUIHandler(new PtrUIHandler() {
            private int mLoadTime = 0;

            @Override
            public void onUIPositionChange(PtrFrameLayout paramAnonymousPtrFrameLayout, boolean paramAnonymousBoolean, byte paramAnonymousByte, PtrIndicator paramAnonymousPtrIndicator) {
            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout paramAnonymousPtrFrameLayout) {
            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout paramAnonymousPtrFrameLayout) {

            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout paramAnonymousPtrFrameLayout) {
            }

            @Override
            public void onUIReset(PtrFrameLayout paramAnonymousPtrFrameLayout) {
            }
        });
        this.ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout paramAnonymousPtrFrameLayout, View paramAnonymousView1, View paramAnonymousView2) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout paramAnonymousPtrFrameLayout) {
                updateItems(paramAnonymousPtrFrameLayout);
            }
        });

        //处理滚动条和Pull-To-Refresh
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt) {
                super.onScrollStateChanged(paramAnonymousRecyclerView, paramAnonymousInt);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt1, int paramAnonymousInt2) {
                super.onScrolled(paramAnonymousRecyclerView, paramAnonymousInt1, paramAnonymousInt2);
                if (FragmentNote.this.recyclerView.canScrollVertically(-1)) {
                    FragmentNote.this.ptrFrameLayout.setEnabled(false);
                } else {
                    FragmentNote.this.ptrFrameLayout.setEnabled(true);
                }
            }
        });
    }

    private void initLogoData() {
        this.a = new ArrayList();
        this.a.add(new float[]{11.0F, 36.0F, 0.0F, 25.0F});
        this.a.add(new float[]{0.0F, 25.0F, 0.0F, 11.0F});
        this.a.add(new float[]{0.0F, 11.0F, 11.0F, 0.0F});
        this.a.add(new float[]{11.0F, 0.0F, 25.0F, 0.0F});
        this.a.add(new float[]{25.0F, 0.0F, 36.0F, 11.0F});
        this.a.add(new float[]{36.0F, 11.0F, 36.0F, 25.0F});
        this.a.add(new float[]{36.0F, 25.0F, 25.0F, 36.0F});
        this.a.add(new float[]{25.0F, 36.0F, 11.0F, 36.0F});
        this.a.add(new float[]{3.0F, 34.0F, 18.0F, 6.0F});
        this.a.add(new float[]{18.0F, 6.0F, 25.0F, 28.0F});
        this.a.add(new float[]{12.0F, 20.0F, 22.0F, 21.0F});
    }

    @Override
    public void onCardViewItemClick(final int pos) {
        editItem(pos);

    }

    public void editItem(final int pos) {
        System.out.println(pos);
        dialogTitle.setText("编辑便签");
        dialogBtnDel.setText("删  除");
        dialogEditContent.setText(items.get(pos).getString("content"));
        dialogEditContent.setHint("请输入内容");
        dialogEditTitle.setText(items.get(pos).getString("title"));
        dialogEditTitle.setHint("请输入标题");
        dialogBtnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject note0 = items.get(pos);
                note0.put("content", dialogEditContent.getText().toString());
                note0.put("title", dialogEditTitle.getText().toString());
                note0.put("date", new Date());
                items.set(pos, note0);
                note0.saveInBackground().subscribe(FragmentNote.this);
                adapter.notifyItemChanged(pos);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialogBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(pos);
                adapter.notifyItemRemoved(pos);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void addItem() {
        dialogTitle.setText("添加便签");
        dialogBtnDel.setText("取  消");
        dialogEditContent.setHint("请输入内容");
        dialogEditContent.setText("");
        dialogEditTitle.setHint("请输入标题");
        dialogEditTitle.setText("");

        dialogBtnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AVObject note0 = new Note(dialogEditTitle.getText().toString(),dialogEditContent.getText().toString());
                AVObject note0 = new AVObject("Notes");
                note0.put("date", new Date());
                note0.put("title", dialogEditTitle.getText().toString());
                note0.put("content", dialogEditContent.getText().toString());
                note0.saveInBackground().subscribe(FragmentNote.this);

//                items.add(note0);
//                System.out.println(items.size());
//                adapter.notifyItemInserted(items.size() - 1);
                dialog.dismiss();
            }
        });
        dialogBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(AVObject avObject) {
        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
        updateItems(ptrFrameLayout);

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRangeChanged(0, items.size());
                adapter.notifyDataSetChanged();
            }
        });

    }
}
