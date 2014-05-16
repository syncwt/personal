import java.util.Iterator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class Lazy {

  static class IntIterator implements Iterator<Integer> {

    private int n = 0;

    @Override
    public boolean hasNext() {
      return true;
    }

    @Override
    public Integer next() {
      return n++;
    }

    @Override
    public void remove() {
    }
    
  }
  public static void main(String[] args) {
    Iterable<Integer> it = new Iterable<Integer>() {
      @Override
      public Iterator<Integer> iterator() {
        return new IntIterator();
      }
    };

    System.out.println(Iterables.filter(it, new Predicate<Integer>() {
      @Override
      public boolean apply(Integer input) {
        return input < 25;
      }
    }));
  }
}
