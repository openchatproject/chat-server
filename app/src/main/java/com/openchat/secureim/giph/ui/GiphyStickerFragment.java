package com.openchat.secureim.giph.ui;


import android.os.Bundle;
import android.support.v4.content.Loader;

import com.openchat.secureim.giph.model.GiphyImage;
import com.openchat.secureim.giph.net.GiphyStickerLoader;

import java.util.List;

public class GiphyStickerFragment extends GiphyFragment {
  @Override
  public Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
    return new GiphyStickerLoader(getActivity(), searchString);
  }
}
