package com.olavo.popularmoviesappv2;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by Olavo on 2/28/2018.
 */

public class ListMovies extends AppCompatActivity {

    private String iv_leftUrl;

    public ListMovies(String iv_leftUrl) {
        this.iv_leftUrl = iv_leftUrl;
            }

    public String getIv_leftUrl() { return iv_leftUrl;
    }

}
