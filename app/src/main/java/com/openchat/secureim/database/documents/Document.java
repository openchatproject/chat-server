package com.openchat.secureim.database.documents;

import java.util.List;

public interface Document<T> {

  public int size();
  public List<T> getList();

}
