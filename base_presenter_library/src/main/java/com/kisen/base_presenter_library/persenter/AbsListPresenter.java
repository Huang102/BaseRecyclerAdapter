package com.kisen.base_presenter_library.persenter;

import android.content.Context;

import com.kisen.base_adapter_library.BaseQuickAdapter;
import com.kisen.base_adapter_library.BaseViewHolder;
import com.kisen.base_presenter_library.IData;

import java.util.ArrayList;
import java.util.List;

/**
 * 对应列表数据类
 *
 * @author Huangwy
 * @TIME 2016/8/12 17:09
 * <p/>
 * 该presenter必须配合CommonPresenterAdapter使用
 * 使用：
 * setList(List<IData> lists) 填充presenter
 * getPresenters()     获取生成好的presenter列表 CommonPresenterAdapter.addData(getPresenters())
 * 子类实现抽象方法即可
 */
public abstract class AbsListPresenter<D extends IData> extends AbsPresenter<D> {

    public static final int TYPE_VIEW_DEFAULT = 0;
    public static final int TYPE_VIEW_ONE = 1;
    public static final int TYPE_VIEW_TWO = 2;
    public static final int TYPE_VIEW_BLANK = 10;

    protected List<D> lists;

    protected List<AbsListPresenter> presenters;

    protected int positionInParent;

    /**
     * 只可以在调用onItemPresenterClick()方法的presenter中使用
     * 子presenter中不能直接使用
     */
    protected BaseQuickAdapter adapter;

    public AbsListPresenter(Context context) {
        super(context);
    }

    /**
     * 子presenter实现创建子类对象并绑定数据
     * 也可以绑定父presenter定义的属性
     *
     * @param data presenter对应数据
     * @return presenter
     */
    protected abstract AbsListPresenter createPresenterWithData(D data);

    /**
     * 需要实现，返回对应Item的布局文件Id 如果返回0，则使用适配器默认布局
     *
     * @return 返回当前数据类对应布局
     */
    public abstract int getViewResId();

    /**
     * 必须实现，在数据类中直接将数据适配到通过BaseViewHolder获取到的视图中
     *
     * @param context 适配器上下文
     * @param helper  用来获取Item的控件
     * @like BaseAdapter's getView()
     */
    public abstract void setViewData(Context context, BaseViewHolder helper);

    /**
     * 需要实现，默认返回0，同一列表中出现多种不同的布局时，必须返回不同的类型，
     * 如果返回相同的值，会因BaseViewHolder复用出现布局错乱，处理数据时异常
     * 在{@like getViewResId()}中已经把对应的布局返回给适配器
     *
     * @return 返回当前自定义Item类型
     * @like BaseAdapter's getItemViewType()
     */
    public abstract int getType();

    /**
     * 不可重写的方法，具体实现由onPresenterClick()方法完成
     * 更新列表对应position的item，带有动画效果
     *
     * @param adapter  界面适配器
     * @param position 变化对应position
     */
    public final void onItemPresenterClick(BaseQuickAdapter adapter, int position) {
        this.adapter = adapter;
        int[] oldPositions = this.onPresenterClick(data);
        if (position < 0 || position > adapter.getItemCount() - 1)
            return;
        for (int i = 0; i < oldPositions.length; i++) {
            if (oldPositions[i] != position)
                adapter.notifyItemChanged(oldPositions[i]);
        }
        adapter.notifyItemChanged(position);
    }

    /**
     * 点击事件回调方法
     *
     * @return 返回需要更新的position集合
     * @link setViewData()，调用onPresenterClick() 会刷新setViewData()方法
     * onPresenterClick方法只需要更新数据，在setViewData中根据数据对UI进行刷新
     */
    protected int[] onPresenterClick(D data) {
        return new int[]{-1};
    }

    /**
     * 创建子presenter列表
     * 列表试图使用 配合Adapter使用 创建数据对应的presenter列表
     *
     * @param lists 数据列表
     */
    public void setList(List<D> lists) {
        if (lists == null)
            return;
        if (this.lists == null) {
            this.lists = new ArrayList<>(lists);
            this.presenters = new ArrayList<>();
        } else {
            this.lists.addAll(lists);
        }
        makePresenters(lists);
    }

    /**
     * 列表试图使用 配合Adapter使用 创建数据对应的presenter列表
     *
     * @param position 位置
     * @param data     数据
     */
    public void insertData(int position, D data) {
        if (this.lists == null) {
            this.lists = new ArrayList<>();
            this.presenters = new ArrayList<>();
        }
        if (this.lists.size() == 0 || position > this.lists.size() - 1) {
            this.lists.add(data);
            makePresenter(-1, data);
        } else {
            this.lists.add(position, data);
            makePresenter(position, data);
        }
    }

    /**
     * 更新对应position下presenter的data数据和lists数据
     *
     * @param position
     * @param data
     */
    public void notifyListData(int position, D data) {
        if (this.lists.size() == 0 || position > this.lists.size() - 1) {
            throw new ArrayIndexOutOfBoundsException("数据组越界");
        } else {
            this.lists.remove(position);
            this.lists.add(position, data);
            this.presenters.get(position).setData(data);
        }
    }

    /**
     * 列表试图使用 配合Adapter使用 创建数据对应的presenter列表
     *
     * @param data
     */
    public void insertData(D data) {
        if (this.lists == null || this.lists.size() == 0)
            insertData(0, data);
        else
            insertData(lists.size() - 1, data);
    }

    /**
     * 创建数据列表对应的presenter列表
     *
     * @param lists 数据列表
     */
    private void makePresenters(List<D> lists) {
        for (int i = 0; i < lists.size(); i++) {
            AbsListPresenter presenter = createPresenterWithData(lists.get(i));
            presenter.positionInParent = i;
            this.presenters.add(presenter);
        }
    }

    /**
     * 创建data对应presenter并填充到presenters中
     *
     * @param position
     * @param data
     */
    private void makePresenter(int position, D data) {
        AbsListPresenter presenter = createPresenterWithData(data);
        if (position == -1) {
            this.presenters.add(presenter);
            presenter.positionInParent = this.presenters.size() - 1;
        } else {
            this.presenters.add(position, presenter);
            presenter.positionInParent = position;
        }
    }

    /**
     * 刷新数据
     * 清空presenters列表，并根据lists调用createPresenterWithData(lists)重新生成presenters
     */
    public void refresh() {
        if (this.presenters == null)
            return;
        this.presenters.clear();
        makePresenters(lists);
    }


    /**
     * 获取所有presenter列表，作为adapter's item 数据使用
     *
     * @return
     */
    public List<AbsListPresenter> getPresenters() {
        if (presenters == null)
            return new ArrayList<>();
        return presenters;
    }

    /**
     * 返回持有的数据列表
     *
     * @return
     */
    public List<D> getLists() {
        if (lists == null)
            return new ArrayList<>();
        return lists;
    }

    @Override
    public void clear() {
        super.clear();
        if (lists != null) {
            this.lists.clear();
            this.presenters.clear();
        }
    }
}
