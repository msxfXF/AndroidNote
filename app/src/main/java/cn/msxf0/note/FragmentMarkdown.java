package cn.msxf0.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;


/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-12-4 上午8:59
 *  
 */
public class FragmentMarkdown extends Fragment implements CardViewItemListener, Observer<AVObject> {
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<AVObject> items;
    private SlideInRightAnimationAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private ArrayList a;
    private StoreHouseHeader storeHouseHeader;


    public static FragmentMarkdown newInstance() {
        return new FragmentMarkdown();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_markdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        ptrFrameLayout = view.findViewById(R.id.store_house_ptr_frame);
        initLogo();

//        for (int i = 0; i < 30; i++) {
//            items.add(new MarkDown("title" + i, "**test content**"));
//        }


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        items = new ArrayList();
        AdapterMarkdown1 adapter0 = new AdapterMarkdown1(getContext(), items);
        adapter0.setListener(FragmentMarkdown.this);
        this.adapter = new SlideInRightAnimationAdapter(adapter0);
        this.adapter.setFirstOnly(false);
        this.adapter.setDuration(800);
        recyclerView.setAdapter(this.adapter);


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
                FragmentMarkdown.this.ptrFrameLayout.autoRefresh(false);
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
                if (FragmentMarkdown.this.recyclerView.canScrollVertically(-1)) {
                    FragmentMarkdown.this.ptrFrameLayout.setEnabled(false);
                } else {
                    FragmentMarkdown.this.ptrFrameLayout.setEnabled(true);
                }
            }
        });
    }

    private void updateItems(final PtrFrameLayout ptr) {
        int a = items.size();
        items.clear();
        adapter.notifyDataSetChanged();
        adapter.notifyItemRangeRemoved(0, a);

        AVQuery<AVObject> query = new AVQuery<>("Markdowns");
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
                adapter.notifyItemRangeChanged(0, FragmentMarkdown.this.items.size());
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

    public void addItem() {
        Intent intent = new Intent();
        intent.setClass(getContext(), MarkdownActivity.class);
        intent.putExtra("title", "");
        intent.putExtra("content", "");
        startActivityForResult(intent, 101);
    }

    public void editItem(final int pos) {
        Intent intent = new Intent();
        intent.setClass(getContext(), MarkdownActivity.class);
        intent.putExtra("title", items.get(pos).getString("title"));
        intent.putExtra("content", items.get(pos).getString("content"));
        intent.putExtra("pos", pos);
        startActivityForResult(intent, 102);
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
    public void onCardViewItemClick(int pos) {
        editItem(pos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 100:
                break;
            case 101:
                if (requestCode == 101) {//添加
                    AVObject markdown0 = new AVObject("Markdowns");
                    markdown0.put("date", new Date());
                    markdown0.put("title", data.getStringExtra("title"));
                    markdown0.put("content", data.getStringExtra("content"));
                    markdown0.saveInBackground().subscribe(FragmentMarkdown.this);

                } else if (requestCode == 102) { //编辑
                    int pos = data.getIntExtra("pos", 0);
                    AVObject markdown0 = items.get(pos);
                    markdown0.put("title", data.getStringExtra("title"));
                    markdown0.put("content", data.getStringExtra("content"));
                    markdown0.put("date", new Date());
                    items.set(pos, markdown0);
                    markdown0.saveInBackground().subscribe(FragmentMarkdown.this);
                    adapter.notifyItemChanged(pos);
                    adapter.notifyDataSetChanged();
                }


        }
        super.onActivityResult(requestCode, resultCode, data);
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
