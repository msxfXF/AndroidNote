package cn.msxf0.note;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-10-25 下午3:55
 *  
 */
public class RecyclerViewFragment extends Fragment {
    private Context context;

    public static RecyclerViewFragment newInstance()
    {
        return new RecyclerViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = container.getContext();
        return inflater.inflate(R.layout.fragment1, container, false);
    }

}
