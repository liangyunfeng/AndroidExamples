package com.github.liangyunfeng.picasso.demo3;

import java.util.List;

/**
 * Created by yunfeng.l on 2018/6/1.
 */

//bean类，通过网址的json数据用插件GsonFromt自动勾选需要的数据生成
public class TestBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ID : 10745507
         * PicName : 10745507.jpg
         * pic_path : /picture1/M00/0A/FA/wKiFR1NRAsiAF85CAAXZwUlD8Tk121.jpg
         * BigCategoryId : 1041
         * SecondCategoryId : 2661
         * CreateTime : 2014-04-18 18:44:01
         * passtime : 2014-03-19 18:47:34
         * UserName : sprit_admin
         * DownloadCount : 9
         * GoodCount : 9
         * tags :
         * WallPaperMiddle : http://bzpic.spriteapp.cn/250x445/picture1/M00/0A/FA/wKiFR1NRAsiAF85CAAXZwUlD8Tk121.jpg
         * WallPaperBig : http://bzpic.spriteapp.cn/1080x1920/picture1/M00/0A/FA/wKiFR1NRAsiAF85CAAXZwUlD8Tk121.jpg
         * WallPaperDownloadPath : http://bzpic.spriteapp.cn/1080x1920/picture1/M00/0A/FA/wKiFR1NRAsiAF85CAAXZwUlD8Tk121.jpg
         * WallPaperSource : http://bzpic.spriteapp.cn/744x1392/picture1/M00/0A/FA/wKiFR1NRAsiAF85CAAXZwUlD8Tk121.jpg
         * weixin_url : http://www.budejie.com/bz/bdj-10745507.html?wx.qq.com
         */

        private List<WallpaperListInfoBean> WallpaperListInfo;

        public List<WallpaperListInfoBean> getWallpaperListInfo() {
            return WallpaperListInfo;
        }

        public void setWallpaperListInfo(List<WallpaperListInfoBean> WallpaperListInfo) {
            this.WallpaperListInfo = WallpaperListInfo;
        }

        public static class WallpaperListInfoBean {
            private int ID;
            private String tags;
            private String WallPaperMiddle;
            private String WallPaperBig;
            private String WallPaperDownloadPath;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getWallPaperMiddle() {
                return WallPaperMiddle;
            }

            public void setWallPaperMiddle(String WallPaperMiddle) {
                this.WallPaperMiddle = WallPaperMiddle;
            }

            public String getWallPaperBig() {
                return WallPaperBig;
            }

            public void setWallPaperBig(String WallPaperBig) {
                this.WallPaperBig = WallPaperBig;
            }

            public String getWallPaperDownloadPath() {
                return WallPaperDownloadPath;
            }

            public void setWallPaperDownloadPath(String WallPaperDownloadPath) {
                this.WallPaperDownloadPath = WallPaperDownloadPath;
            }

            @Override
            public String toString() {
                return "WallpaperListInfoBean{" +
                        "ID=" + ID +
                        ", tags='" + tags + '\'' +
                        ", WallPaperMiddle='" + WallPaperMiddle + '\'' +
                        ", WallPaperBig='" + WallPaperBig + '\'' +
                        ", WallPaperDownloadPath='" + WallPaperDownloadPath + '\'' +
                        '}';
            }
        }
    }
}

