package com.queerlab.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: LocationUserBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/28/21 3:23 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/28/21 3:23 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LocationUserBean {
    /**
     * total : 14
     * list : [{"user_status":"忙碌1","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":"118.303000","user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":18.165858657660266,"user_id":1,"user_name":"用户1","user_phone":"15564559243","user_sign":"我还是从前那个少年1","lat":"33.909000"},{"user_status":"忙碌2","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":118.303997,"user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.622301195724468,"user_id":40,"user_name":"用户2","user_phone":"13127261717","user_sign":"我还是从前那个少年2","lat":"33.951880"},{"user_status":"忙碌3","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":118.302991,"user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":20.916844548057075,"user_id":41,"user_name":"用户3","user_phone":"13127261616","user_sign":"我还是从前那个少年3","lat":33.935828},{"user_status":"忙碌5","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":118.290081,"user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.983416445835644,"user_id":43,"user_name":"用户5","user_phone":"15564559876","user_sign":"我还是从前那个少年5","lat":33.959007},{"user_status":"忙碌1","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":"118.300000","user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.829427297925015,"user_id":44,"user_name":"用户1","user_phone":"15564559241","user_sign":"我还是从前那个少年1","lat":"33.955000"},{"user_status":"忙碌2","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":118.303997,"user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.622301195724468,"user_id":45,"user_name":"用户2","user_phone":"13127261712","user_sign":"我还是从前那个少年2","lat":"33.951880"},{"user_status":"忙碌3","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":118.302991,"user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":20.916844548057075,"user_id":46,"user_name":"用户3","user_phone":"13127261613","user_sign":"我还是从前那个少年3","lat":33.935828},{"user_status":"忙碌4","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":"118.282150","user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.824947353410458,"user_id":47,"user_name":"用户4","user_phone":"18663709254","user_sign":"我还是从前那个少年4","lat":33.959307},{"user_status":"忙碌5","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":118.290081,"user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.983416445835644,"user_id":48,"user_name":"用户5","user_phone":"15564559875","user_sign":"我还是从前那个少年5","lat":33.959007},{"user_status":"忙碌1","user_type":"兴趣1,兴趣2,兴趣3,兴趣4","phone_country_code":"86","lng":"118.300000","user_portrait":"https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg","distance":22.829427297925015,"user_id":49,"user_name":"用户1","user_phone":"15564559246","user_sign":"我还是从前那个少年1","lat":"33.955000"}]
     * pageNum : 1
     * totalPage : 2
     */

    private int total;
    private int pageNum;
    private int totalPage;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * user_status : 忙碌1
         * user_type : 兴趣1,兴趣2,兴趣3,兴趣4
         * phone_country_code : 86
         * lng : 118.303000
         * user_portrait : https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg
         * distance : 18.165858657660266
         * user_id : 1
         * user_name : 用户1
         * user_phone : 15564559243
         * user_sign : 我还是从前那个少年1
         * lat : 33.909000
         */

        private String user_status;
        private String user_type;
        private String phone_country_code;
        private String lng;
        private String user_portrait;
        private double distance;
        private int user_id;
        private String user_name;
        private String user_phone;
        private String user_sign;
        private String lat;
        public boolean isSelect;
        private String title;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getPhone_country_code() {
            return phone_country_code;
        }

        public void setPhone_country_code(String phone_country_code) {
            this.phone_country_code = phone_country_code;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getUser_portrait() {
            return user_portrait;
        }

        public void setUser_portrait(String user_portrait) {
            this.user_portrait = user_portrait;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_sign() {
            return user_sign;
        }

        public void setUser_sign(String user_sign) {
            this.user_sign = user_sign;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
