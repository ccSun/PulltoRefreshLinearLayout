# PulltoRefreshLinearLayout
Customized Linearlayout realizes PulltoRefresh and PulltoLoadMore. 

* Compatible with **RecyclerView** and **ScrollView**. **RecyclerView** is used to list data items. While **ScrollView** is used to refresh some info details.

* **PulltoRefreshLinearLayout** 兼容**RecyclerView**和**ScrollView**，像使用LinearLayout一样使用即可。


## Use:

Add module **mlib** to your module dependency. Then use **com.nsnv.mlib.RefreshLinearly** just like you use **android.widget.LinearLayout**.

### XML

You can show color by using attr **"refreshlinearly:colorShow"** like below:

```
<com.nsnv.mlib.RefreshLinearly xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:refreshlinearly="http://schemas.android.com/apk/res-auto"
    android:id="@+id/linearly_my_meetings"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:background="@color/bg_white"
    refreshlinearly:colorShow="@color/bg_color_900"
    >
```

## ScreenShots:
![image](https://github.com/ccSun/PulltoRefreshLinearLayout/blob/master/screenshots/refresh.gif?raw=true)
![image](https://github.com/ccSun/PulltoRefreshLinearLayout/blob/master/screenshots/loadmore.gif?raw=true)
