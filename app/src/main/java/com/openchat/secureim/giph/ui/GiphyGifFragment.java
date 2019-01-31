package com.openchat.secureim.giph.ui;


import android.os.Bundle;
import android.support.v4.content.Loader;

import com.openchat.secureim.giph.model.GiphyImage;
import com.openchat.secureim.giph.net.GiphyGifLoader;

import java.util.List;

public class GiphyGifFragment extends GiphyFragment {

  @Override
  public Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
    return new GiphyGifLoader(getActivity(), searchString);
  }

}
