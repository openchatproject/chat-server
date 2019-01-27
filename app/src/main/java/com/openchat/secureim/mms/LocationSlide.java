package com.openchat.secureim.mms;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.openchat.secureim.components.location.openchatPlace;
import com.openchat.libim.util.guava.Optional;

public class LocationSlide extends ImageSlide {

  @NonNull
  private final openchatPlace place;

  public LocationSlide(@NonNull  Context context, @NonNull  Uri uri, long size, @NonNull openchatPlace place)
  {
    super(context, uri, size);
    this.place = place;
  }

  @Override
  @NonNull
  public Optional<String> getBody() {
    return Optional.of(place.getDescription());
  }

  @NonNull
  public openchatPlace getPlace() {
    return place;
  }

  @Override
  public boolean hasLocation() {
    return true;
  }

}
