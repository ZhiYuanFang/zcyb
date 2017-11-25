package com.wzzc.NextTBSearch;

/**
 * Created by by Administrator on 2017/7/3.
 */

public interface TBrebackDelegate {
    void searchChange (String keyWords);
    void showList (String keyWords);
    void closeSelect ();
}
