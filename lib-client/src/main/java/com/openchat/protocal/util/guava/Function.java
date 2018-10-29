package com.openchat.protocal.util.guava;





public interface Function<F, T> {
  
  T apply(F input);

  
  @Override
  boolean equals(Object object);
}
