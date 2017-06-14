package com.waterfairy.tool.widget.baseView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by water_fairy on 2017/6/13.
 * 995637517@qq.com
 */

public interface ViewCreateObserver {
    int TYPE_DATA = 1;
    int TYPE_VIEW = 2;

    void onUpdate(int type, boolean state);

}
