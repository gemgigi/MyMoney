package com.graduation.jasonzhu.mymoney.util;

import com.graduation.jasonzhu.mymoney.model.Category;

import java.util.List;
import java.util.Objects;

/**
 * Created by gemha on 2016/3/3.
 */
public interface LoadDataCallBackListener {
    void onFinish(boolean isCreate);
    void onError(Exception e);
}
