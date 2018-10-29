package com.openchat.protocal.util.guava;

import static com.openchat.protocal.util.guava.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;



public abstract class Optional<T> implements Serializable {
  
  @SuppressWarnings("unchecked")
  public static <T> Optional<T> absent() {
    return (Optional<T>) Absent.INSTANCE;
  }

  
  public static <T> Optional<T> of(T reference) {
    return new Present<T>(checkNotNull(reference));
  }

  
  public static <T> Optional<T> fromNullable(T nullableReference) {
    return (nullableReference == null)
        ? Optional.<T>absent()
        : new Present<T>(nullableReference);
  }

  Optional() {}

  
  public abstract boolean isPresent();

  
  public abstract T get();

  
  public abstract T or(T defaultValue);

  
  public abstract Optional<T> or(Optional<? extends T> secondChoice);

  
  public abstract T or(Supplier<? extends T> supplier);

  
  public abstract T orNull();

  
  public abstract Set<T> asSet();

  
 
  public abstract <V> Optional<V> transform(Function<? super T, V> function);

  
  @Override public abstract boolean equals(Object object);

  
  @Override public abstract int hashCode();

  
  @Override public abstract String toString();

  

//  public static <T> Iterable<T> presentInstances(
//      final Iterable<? extends Optional<? extends T>> optionals) {
//    checkNotNull(optionals);
//    return new Iterable<T>() {
//      @Override public Iterator<T> iterator() {
//        return new AbstractIterator<T>() {
//          private final Iterator<? extends Optional<? extends T>> iterator =
//              checkNotNull(optionals.iterator());
//
//          @Override protected T computeNext() {
//            while (iterator.hasNext()) {
//              Optional<? extends T> optional = iterator.next();
//              if (optional.isPresent()) {
//                return optional.get();
//              }
//            }
//            return endOfData();
//          }
//        };
//      };
//    };
//  }

  private static final long serialVersionUID = 0;
}
