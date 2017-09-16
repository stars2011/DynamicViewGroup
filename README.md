动态适应布局，流式布局，支持以下功能：
1、自动换行。
2、设置横向/竖向排列子View。
3、设置子View的双端对齐，居中对齐，左对齐，右对齐，顶对齐，底对齐。
4、设置每行/列最大个数。
5、设置横向、竖向间距。

-----

### DynamicViewGroup
#### 1、自动换行
横/竖向排列子View的时候，当前行/列满了的话自动创建新的行/列排列子View。

横向排列自动换行：
(http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/a4qZckQXjmGre5CXlBLHJlQnKZ9NaoapAAL*CcBlrdw!/b/dPIAAAAAAAAA&bo=vwEkAQAAAAARAKw!&rf=viewer_4)

竖向排列自动换列：
(http://a1.qpic.cn/psb?/V13PyPWD4OxnNU/w69WtPLRMGgSqrCqgecAqr49lmlTB0tiyj0DZqeYMtM!/b/dD4BAAAAAAAA&bo=vwHZAQAAAAARAFE!&rf=viewer_4)

#### 2、设置横向/竖向排列子View
可以通过代码设置子View的排列方向：

        mDynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL); // 横向模式
        mDynamicViewGroup.setOrientation(DynamicViewGroup.VERTICAL); // 竖向模式

也可以通过xml的方式设置排列方向：

        <com.stars2011.dynamicviewgroup.DynamicViewGroup
         android:id="@+id/dynamic_view_group"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         app:orientation="horizontal/vertical"  设置横向/竖向


----------

***排列方式（horizontal/vertical）切换gif效果如下：***

![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/cPOa7rcK5B7AOTQVmSIB52w0XdralQyodsenGsHXxP8!/b/dPIAAAAAAAAA&bo=vwEbA78BGwMCb0s!&rf=viewer_4)

----------


横向排列：

![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/a4qZckQXjmGre5CXlBLHJlQnKZ9NaoapAAL*CcBlrdw!/b/dPIAAAAAAAAA&bo=vwEkAQAAAAARAKw!&rf=viewer_4)

竖向排列：

![enter image description here](http://a1.qpic.cn/psb?/V13PyPWD4OxnNU/w69WtPLRMGgSqrCqgecAqr49lmlTB0tiyj0DZqeYMtM!/b/dD4BAAAAAAAA&bo=vwHZAQAAAAARAFE!&rf=viewer_4)

#### 3、设置子View的对齐方式
支持的对齐方式：
>* 双端对齐 （横、竖向排列均可使用）
>* 居中对齐 （横、竖向排列均可使用）
>* 左对齐 （仅横向排列使用）
>* 右对齐 （仅横向排列使用）
>* 顶对齐 （仅竖向排列使用）
>* 底对齐 （仅竖向排列使用）

可以通过代码设置对齐方式：

        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_BOTH); // 双端对齐
        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_CENTER); // 居中对齐        
        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_LEFT); // 左对齐
        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_RIGHT); // 右对齐
        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_TOP); // 顶对齐
        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_BOTTOM); // 底对齐

也可以通过xml的方式设置对齐方式：

        <com.stars2011.dynamicviewgroup.DynamicViewGroup
         android:id="@+id/dynamic_view_group"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         app:gravity="gravity_left" 设置左对齐


----------

***横向模式对齐方式（gravity）切换gif效果：***
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/zx3dPAiEn2eI6Zgh*.KqDvNjG1k0agYUIKawUXY8Ap0!/b/dPIAAAAAAAAA&bo=vwEbA78BGwMCfVk!&rf=viewer_4)

***竖向模式对齐方式（gravity）切换gif效果：***
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/.OiUhPvtL6yZT4wjmGhK4j7bwy*if..1BRMYHCyMscA!/b/dPIAAAAAAAAA&bo=vwEbA78BGwMCoYU!&rf=viewer_4)

----------


##### 双端对齐
###### （1）横向排列的双端对齐：
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/NWmWG676gSZOxSzuXfEczb84hJgAT3CNGTeNNgw6esI!/b/dPIAAAAAAAAA&bo=vwEsAQAAAAADALY!&rf=viewer_4)

###### （2）竖向排列的双端对齐：
![enter image description here](http://a2.qpic.cn/psb?/V13PyPWD4OxnNU/g8wuT4.cGnca1lmXoAq5ZGA9PJ9VO*f3k4hQKAzsdZI!/b/dD8BAAAAAAAA&bo=vwHWAQAAAAADAEw!&rf=viewer_4)

##### 居中对齐
###### （1）横向排列的居中对齐：
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/a0CZlLnHWhZrIW*hSjq6jKh0GQPCZ1stTSKoSY.KX78!/b/dPIAAAAAAAAA&bo=vwEsAQAAAAADALY!&rf=viewer_4)

###### （2）竖向排列的居中对齐：
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/CUTrfhExc8c2eeH8ASRDvxasdUm7K21yp1L4mROrImo!/b/dPIAAAAAAAAA&bo=vwHWAQAAAAADAEw!&rf=viewer_4)

##### 左对齐
![enter image description here](http://a2.qpic.cn/psb?/V13PyPWD4OxnNU/uE6p2VbbrmNxLIlkZ0*Ngm192ImWsfSMzemyHMDl3I4!/b/dGkBAAAAAAAA&ek=1&kp=1&pt=0&bo=vwEsAQAAAAADF6E!&vuin=361630609&tm=1505538000&sce=60-2-2&rf=viewer_4)

##### 右对齐
![enter image description here](http://a1.qpic.cn/psb?/V13PyPWD4OxnNU/LcbzzDSQjyCzr4yCFu4nGF*m2G474FsGUzq*LvpbCnk!/b/dGsBAAAAAAAA&bo=vwEsAQAAAAADALY!&rf=viewer_4)

##### 顶对齐
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/6n0D7y0aYZRQSNQV2sut8CDXoBHnLiP5pkDKK4oQgG4!/b/dGoBAAAAAAAA&bo=vwHWAQAAAAADAEw!&rf=viewer_4)

##### 底对齐
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/MbE4GnmFI*UEELUcQlrsAjflexWWx6RnSZ0Ep83z6eg!/b/dPIAAAAAAAAA&bo=vwHWAQAAAAADAEw!&rf=viewer_4)

#### 4、设置每行/列最大个数（当每行/列子View个数超过设置的数值则自动换行/列）

可以通过代码设置每行/列最大个数：

        // 最大行数，当每列子View个数超过则自动换列（用于 竖向排列模式 模式）
        mDynamicViewGroup.setMaxLineNum(3); 
        // 最大列数，当每行子View个数超过则自动换行（用于 横向排列 模式）
        mDynamicViewGroup.setMaxColumnNum(3);
        
也可以通过xml设置每行/列最大个数：

        <com.stars2011.dynamicviewgroup.DynamicViewGroup
         android:id="@+id/dynamic_view_group"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         app:max_line_num="3"   设置最大行数（用于 竖向排列模式 模式）
         app:max_column_num="3" 设置最大列数（用于 横向排列 模式）


----------

***设置每行最大的子View个数gif效果（到达最大行个数换行）：***

![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/Da*8VFG8oqFK3hSg*KEVC5Pr31SLrLPjMBgQu65gKCo!/b/dPIAAAAAAAAA&bo=vwEbA78BGwMCxuI!&rf=viewer_4)

***设置每列最大的子View个数gif效果（到达最大列个数换列）：***

![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/bi.SYtkIEe*OJJ.lIUOM*No7cc6Sd3ZCE.VuUF.v9Oo!/b/dPIAAAAAAAAA&bo=vwEbA78BGwMC8dU!&rf=viewer_4)

----------


#### 5、设置横向、竖向间距

可以通过代码设置横向/竖向间距：

        mDynamicViewGroup.setHorizontalSpacing(6); // 设置横向间距
        mDynamicViewGroup.setVerticalSpacing(6); // 设置竖向间距

也可以通过xml设置横向/竖向间距：

        <com.stars2011.dynamicviewgroup.DynamicViewGroup
         android:id="@+id/dynamic_view_group"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         app:horizontal_spacing="10dp" 设置横向间距
         app:vertical_spacing="10dp" 设置竖向间距


----------

***设置横向和竖向间距gif效果：***
![enter image description here](http://a3.qpic.cn/psb?/V13PyPWD4OxnNU/4.hQTNNxqn7.rSn*2RHOyUX5.QOnmVGHPJzmZHXa8Y8!/b/dPIAAAAAAAAA&bo=vwEbA78BGwMC7so!&rf=viewer_4)

----------
