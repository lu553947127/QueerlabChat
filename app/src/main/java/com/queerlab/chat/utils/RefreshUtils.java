package com.queerlab.chat.utils;

import com.queerlab.chat.R;
import com.queerlab.chat.bean.BaseBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: RefreshUtils
 * @Description: 上拉加载刷新数据工具
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:36 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:36 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RefreshUtils {

    /**
     * 上拉加载刷新数据 分页工具
     *
     * @param refresh
     * @param page
     * @param count
     */
    public static void setNoMore(SmartRefreshLayout refresh, int page, int count){
        if (page == 1) {
            if (page * 10 >= count) {
                if (refresh.getState() == RefreshState.None) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.finishRefreshWithNoMoreData();
                }
            } else {
                refresh.finishRefresh();
            }
        } else {
            if (page * 10 >= count) {
                refresh.finishLoadMoreWithNoMoreData();
            } else {
                refresh.finishLoadMore();
            }
        }
    }

    /**
     * 设置小组默认emoji表情库
     *
     * @return
     */
    public static String[] getEmojiDemo(){
        List<String> list = new ArrayList<>();
        list.add("\uD83C\uDFC0");
        list.add("⚽");
        list.add("\uD83C\uDFBE");
        list.add("\uD83C\uDFD0");
        list.add("\uD83C\uDFF8");
        list.add("\uD83C\uDFAD");
        list.add("\uD83C\uDFB9");
        return list.toArray(new String[list.size()]);
    }

    /**
     * 测试图片
     * @return
     */
    public static List<String> getImagesList(){
        List<String> list = new ArrayList();
        list.add("https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp");
        list.add("https://upload-images.jianshu.io/upload_images/5809200-736bc3917fe92142.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp");
        list.add("https://upload-images.jianshu.io/upload_images/5809200-7fe8c323e533f656.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp");
        list.add("https://upload-images.jianshu.io/upload_images/5809200-c12521fbde6c705b.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp");
        list.add("https://upload-images.jianshu.io/upload_images/5809200-caf66b935fd00e18.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp");
        return list;
    }

    //测试马克点定位
    public static List<BaseBean> getMarkerList(){
        List<BaseBean> list =new ArrayList<>();
        list.add(new BaseBean("1", "活动1", 36.686154,117.110479, R.drawable.icon_marker));
        list.add(new BaseBean("2", "活动2", 36.682289,117.129571, R.drawable.icon_marker));
        list.add(new BaseBean("3", "活动3", 36.676456,117.156483, R.drawable.icon_marker));
        list.add(new BaseBean("4", "活动4", 36.67106,117.124662, R.drawable.icon_marker));
        list.add(new BaseBean("5", "活动5", 36.6688,117.129117, R.drawable.icon_marker));
        list.add(new BaseBean("6", "活动6", 36.659538,117.159847, R.drawable.icon_marker));
        list.add(new BaseBean("7", "活动7", 36.654724,117.123116, R.drawable.icon_marker));
        list.add(new BaseBean("8", "活动8", 36.662892,117.143209, R.drawable.icon_marker));
        list.add(new BaseBean("9", "活动9", 36.660121,117.150482, R.drawable.icon_marker));
        list.add(new BaseBean("10", "活动10", 36.652682,117.162483, R.drawable.icon_marker));
        list.add(new BaseBean("11", "活动11", 36.656547,117.13439, R.drawable.icon_marker));
        return list;
    }

    //小组类型列表
    public static List<BaseBean> getGroupListType(){
        List<BaseBean> list =new ArrayList<>();
        list.add(new BaseBean("\uD83C\uDF08", "全部"));
        list.add(new BaseBean("\uD83D\uDCCD", "附近"));
        list.add(new BaseBean("\uD83C\uDFAE", "游戏"));
        list.add(new BaseBean("\uD83C\uDF39", "情人节"));
        list.add(new BaseBean("⚽", "运动"));
        list.add(new BaseBean("\uD83C\uDF39", "情人节"));
        list.add(new BaseBean("⚽", "运动"));
        return list;
    }

    /**
     * 设置小组默认emoji表情库
     *
     * @return
     */
    public static List getEmoji(){
        List<String> list = new ArrayList<>();
        list.add("\uD83C\uDFC0");
        list.add("⚽");
        list.add("\uD83C\uDFBE");
        list.add("\uD83C\uDFD0");
        list.add("\uD83C\uDFF8");
        list.add("\uD83C\uDFAD");
        list.add("\uD83C\uDFB9");
        list.add("\uD83C\uDFAC");
        list.add("\uD83D\uDE00");
        list.add("\uD83D\uDE18");
        list.add("\uD83D\uDE2D");
        list.add("\uD83D\uDE0E");
        list.add("\uD83D\uDE31");
        list.add("\uD83D\uDE08");
        list.add("\uD83E\uDD21");
        list.add("\uD83D\uDC7D");
        list.add("\uD83D\uDE3A");
        list.add("\uD83D\uDC36");
        list.add("\uD83D\uDC39");
        list.add("\uD83D\uDC30");
        list.add("\uD83D\uDC3C");
        list.add("\uD83D\uDC37");
        list.add("\uD83C\uDF3C");
        list.add("\uD83C\uDF38");
        list.add("\uD83C\uDF93");
        list.add("\uD83D\uDCBC");
        list.add("\uD83D\uDDA5️");
        list.add("\uD83D\uDCD6");
        list.add("\uD83D\uDCB0");
        list.add("⚖️");
        list.add("\uD83C\uDFA8");
        list.add("\uD83C\uDFE0");
        list.add("\uD83C\uDF4E");
        list.add("\uD83C\uDF4A");
        list.add("\uD83C\uDF5F");
        list.add("\uD83C\uDF54");
        list.add("\uD83C\uDF5C");
        list.add("\uD83C\uDF5A");
        list.add("\uD83C\uDF77");
        list.add("\uD83C\uDF7A");
        list.add("\uD83D\uDCAB");
        list.add("\uD83C\uDF89");
        list.add("\uD83D\uDCA1");
        list.add("❤️");
        list.add("\uD83D\uDC9B");
        list.add("\uD83D\uDC9A");
        list.add("\uD83D\uDC99");
        list.add("\uD83D\uDC9C");
        return list;
    }
}
