package im.zego.effectsexample.effectsonly.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.midsizemango.effectsonly.R;

import java.util.ArrayList;
import java.util.List;

import im.zego.effectsexample.effectsonly.bean.Resource;


public class ResourceAdapter extends RecyclerView.Adapter {

    private List<Resource> resourceList = new ArrayList<Resource>();
    private OnItemClickListener mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_menu, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final Resource resource = resourceList.get(position);
        myViewHolder.mllPendant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(v,resource);
                }
            }
        });

        myViewHolder.mIvMenuItem.setImageResource(resource.getImageRes());
        myViewHolder.mTvMenuItem.setText(resource.getName());

    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public void setData(List<Resource> data)
    {
        resourceList = data;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View mllPendant;
        private ImageView mIvMenuItem;
        private TextView mTvMenuItem;
        MyViewHolder(View itemView) {
            super(itemView);
            mllPendant = itemView.findViewById(R.id.ll_pendant);
            mIvMenuItem = itemView.findViewById(R.id.iv_menu_item);
            mTvMenuItem = itemView.findViewById(R.id.tv_menu_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view,Resource resource);
    }
}

