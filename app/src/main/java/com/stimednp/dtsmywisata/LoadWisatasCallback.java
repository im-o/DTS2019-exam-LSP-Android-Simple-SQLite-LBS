package com.stimednp.dtsmywisata;

import java.util.ArrayList;

/**
 * Created by rivaldy on 7/29/2019.
 */

public interface LoadWisatasCallback {
    void preExecute();
    void postExecute(ArrayList<Wisatas> wisatas);
}
